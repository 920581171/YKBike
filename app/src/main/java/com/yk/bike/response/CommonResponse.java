package com.yk.bike.response;

public class CommonResponse extends BaseResponse {
    private Object data;

    public Object getData() {
        return data;
    }

    public CommonResponse setData(Object data) {
        this.data = data;
        return this;
    }
}
