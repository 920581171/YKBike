package com.yk.bike.websocket;

public class WebSocketParam {
    private String loginId;
    private String loginType;
    private String password;
    private String loginDevice;

    public String getLoginId() {
        return loginId;
    }

    public WebSocketParam setLoginId(String loginId) {
        this.loginId = loginId;
        return this;
    }

    public String getLoginType() {
        return loginType;
    }

    public WebSocketParam setLoginType(String loginType) {
        this.loginType = loginType;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public WebSocketParam setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getLoginDevice() {
        return loginDevice;
    }

    public WebSocketParam setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
        return this;
    }
}
