package com.yk.bike.response;

import java.util.List;

public class BikeInfoListResponse extends BaseResponse {

    /**
     * data : {"id":5,"bikeId":"123456","latitude":123456,"longitude":123456,"mileage":0,"fix":"0"}
     */

    private List<BikeInfoResponse.BikeInfo> data;

    public List<BikeInfoResponse.BikeInfo> getData() {
        return data;
    }

    public void setData(List<BikeInfoResponse.BikeInfo> data) {
        this.data = data;
    }
}
