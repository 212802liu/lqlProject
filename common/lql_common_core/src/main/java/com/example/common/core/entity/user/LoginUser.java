package com.example.common.core.entity.user;

import lombok.Data;

import java.util.Set;

@Data
public class LoginUser  {

    /**
     * 用户唯一标识
     */
    private String Uuid;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> roles;
}
