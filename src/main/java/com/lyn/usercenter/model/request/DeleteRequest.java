package com.lyn.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的删除请求参数
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = -4162304142710323660L;
    private long id;
}
