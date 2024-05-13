package com.lyn.usercenter.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 队伍和用户封装类(脱敏)
 *
 * @author Nick
 * @create 2023--09-15:43
 */
@Data
public class TeamUserVO implements Serializable {
    private static final long serialVersionUID = -4432828954813281541L;
    /**
     * id
     */
    private Long id;
    /**
     * 队伍名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 最大人数
     */
    private Integer maxNum;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 队伍创建人用户信息
     */
    private UserVO createdUser;
    /**
     * 当前用户是否加入
     */
    private boolean hasJoin = false;
    /**
     * 已加入的用户数量
     */
    private Integer hasJoinNum;
}