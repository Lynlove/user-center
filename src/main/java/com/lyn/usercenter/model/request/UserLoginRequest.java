package com.lyn.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 5865324806022205767L;
    private String userAccount;
    private String userPassword;
}
