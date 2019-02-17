package com.yk.bike.websocket;

public class WebSocketMessage {
    private int type;
    private Object data;

    public int getType() {
        return type;
    }

    public WebSocketMessage setType(int type) {
        this.type = type;
        return this;
    }

    public Object getData() {
        return data;
    }

    public WebSocketMessage setData(Object data) {
        this.data = data;
        return this;
    }
}
