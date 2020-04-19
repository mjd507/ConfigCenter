package com.configcenter.admin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserAppRole {

    private Integer appId;

    private String appName;

    private Integer roleId;

    private String roleDesc;

}