package com.configcenter.admin.entity;

import com.configcenter.admin.util.Role;
import lombok.Data;

@Data
public class UserAppRole {

    private Integer appId;

    private String appName;

    private Integer roleId;

    private String roleDesc;

    private String permission;

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
        setPermission(roleId == Role.PM.getType() ? "只读" : "可读可写");
    }
}