package com.configcenter.common.util;

/**
 * Create by majiandong on 2019/11/15 17:54
 */
public enum ApiCode {
    OK(200, "success"),
    BAD_REQUEST(400, "bad request"),
    UNAUTHORISED(401, "Unauthorised"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_ERROR(500, "Internal Server error"),

    ;

    ApiCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
