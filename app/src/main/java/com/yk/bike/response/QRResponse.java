package com.yk.bike.response;

public class QRResponse{

    /**
     * bikeId : BIKE123456
     * bikeType : type_02
     */

    private String bikeId;
    private String bikeType;

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }
}
