package com.lyn.usercenter.service;

import com.lyn.usercenter.config.RedissonConfig;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 运行发现redis3库里面已经存入数据
 * 3.定时任务+锁
 *
 * @author Nick
 * @create 2023--07-15:36
 */
@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    public void test() {
        //数据存在本地JVM
        List<String> list = new ArrayList<>();
        list.add("Rokie");
        System.out.println("list:" + list.get(0));
        list.remove(0);
        //数据存在Redis
        RList<String> rList = redissonClient.getList("test-list");
//        rList.add("Rokie");
        System.out.println("rList:" + rList.get(0));
        rList.remove(0);
    }

    @Test
    void testWatchDog() {
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");
        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {//是否拿到锁
                Thread.sleep(3000000);
                System.out.println("getLock:" + Thread.currentThread().getId());
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
//释放自己的锁
            if (lock.isHeldByCurrentThread()) {//是否是当前线程
                System.out.println("unlock: " + Thread.currentThread().getId());
                lock.unlock();
            }
//            注意：
//            1. 锁的存在时间要设置为 - 1（开启开门狗），默认锁的过期时间是30秒，通过sleep实现
//            2. 运行，通过quickredis观察，可以发现 每 10 秒续期一次（补到 30 秒）
//            3. 踩坑处：不要用debug启动，会被认为是宕机
        }
    }
}