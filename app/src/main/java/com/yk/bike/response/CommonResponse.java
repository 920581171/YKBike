package com.yk.bike.response;

public class CommonResponse {
    private int code;
    private String msg;
    private Object data;

    public int getCode() {
        return code;
    }

    public CommonResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public CommonResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public CommonResponse setData(Object data) {
        this.data = data;
        return this;
    }
}
