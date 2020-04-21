package com.configcenter.common.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private int code;
    private Object data;
    private String msg;

    public ApiResponse(int code, Object data, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResponse ok() {
        return new ApiResponse(ApiCode.OK.getCode(), null, ApiCode.OK.getDesc());
    }

    public static ApiResponse ok(Object data) {
        return new ApiResponse(ApiCode.OK.getCode(), data, ApiCode.OK.getDesc());
    }

    public static ApiResponse error() {
        return error(ApiCode.INTERNAL_ERROR);
    }

    public static ApiResponse error(ApiCode apiCode) {
        return error(apiCode.getCode(), apiCode.getDesc());
    }

    public static ApiResponse error(String badReqMsg) {
        return error(ApiCode.BAD_REQUEST.getCode(), badReqMsg);
    }

    public static ApiResponse error(int code, String msg) {
        return new ApiResponse(code, null, msg);
    }

    public static ApiResponse error(ApiCode apiCode, String msg) {
        return error(apiCode.getCode(), msg);
    }

}

