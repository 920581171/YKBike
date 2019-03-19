package com.yk.bike.response;

import java.util.List;

public class BikeTypeListResponse extends BaseResponse{

    private List<BikeTypeResponse.BikeType> data;

    public List<BikeTypeResponse.BikeType> getData() {
        return data;
    }

    public void setData(List<BikeTypeResponse.BikeType> data) {
        this.data = data;
    }
}
