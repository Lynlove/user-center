package com.lyn.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn.usercenter.model.domain.User;
import com.lyn.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: shayu
 * @date: 2022/12/11
 * @ClassName: yupao-backend01
 * @Description: 数据预热
 */

@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;


    // 重点用户
    private List<Long> mainUserList = Arrays.asList(4L);

    // 每天执行，预热推荐用户
    @Scheduled(cron = "30 56 13 * * *")   //自己设置时间测试
    public void doCacheRecommendUser() {
        //查数据库
        for (Long userId : mainUserList) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            String redisKey = String.format("yupao:user:recommend:%s", userId);
            ValueOperations<String, Object> valueOperations =
                    redisTemplate.opsForValue();
            //写缓存 30s过期
            try {
                valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("redis set key error", e);
            }
        }
    }

    //每天执行，预热推荐用户(使用分布式锁解决多个实例运行多次执行这个定时任务的问题)
    @Scheduled(cron = "0 31 19 * * ? ")
    public void doCacheRecommendUserForDistributed() {
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");
        try {
            //waitTime:其他线程等待的时间，因为我们缓存预热每天只做一次，所以只要有一个线程拿到锁就行
            //leaseTime：锁过期时间
            if (lock.tryLock(0, 30000L, TimeUnit.MILLISECONDS)) {//是否拿到锁
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20),
                            queryWrapper);
                    String redisKey = String.format("yupao:user:recommend:%s", userId);
                    ValueOperations<String, Object> valueOperations =
                            redisTemplate.opsForValue();
//写缓存
                    try {
                        valueOperations.set(redisKey, userPage, 30000,
                                TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
//                        同时开启三个线程进行测试，结果只有一个拿到了锁，打印出了日志
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
//释放自己的锁
            if (lock.isHeldByCurrentThread()) {//是否是当前线程
                System.out.println("unlock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }


}