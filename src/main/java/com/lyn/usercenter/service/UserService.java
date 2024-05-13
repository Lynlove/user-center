package com.lyn.usercenter.service;

import com.lyn.usercenter.constant.UserConstant;
import com.lyn.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author lyn
* @description 针对表【user】的数据库操作Service
* @createDate 2024-04-28 18:17:49
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     * @param userAccount 用户名
     * @param userPassword 密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏用户信息
     * @param user
     * @return
     */
    User getSafetyUser(User user);


    /**
     * 用户注销
     *
     * @param request
     */
    int userLogout(HttpServletRequest request);

    List<User> searchUserByTags(List<String> tagList);

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request);
    /**
     * 是否为管理员
     * @param loginUser
     * @return
     */
    public boolean isAdmin(User loginUser);

    /**
     * 更新用户信息
     * @param user
     * @param loginUser
     * @return
     */
    public int updateUser(User user, User loginUser);

    /**
     * 获取当前用户
     * @param request
     * @return
     */
    public User getCurrentUser(HttpServletRequest request);

    /**
     * 获取最匹配的用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
