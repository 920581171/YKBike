package com.yk.bike.websocket;

public class WebSocketLoaction {
    private String userId;
    private double latitude;
    private double longitude;

    public String getUserId() {
        return userId;
    }

    public WebSocketLoaction setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public WebSocketLoaction setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public WebSocketLoaction setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
