package com.configcenter.common.util;

/**
 * Created by mjd on 2020/4/12 14:47
 */
public class CCException extends RuntimeException {

    private int code = ApiCode.INTERNAL_ERROR.getCode();
    private String msg;

    public CCException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CCException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public CCException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CCException(int code, String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
