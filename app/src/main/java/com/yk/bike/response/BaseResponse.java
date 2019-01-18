package com.yk.bike.response;

public class BaseResponse {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public BaseResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
