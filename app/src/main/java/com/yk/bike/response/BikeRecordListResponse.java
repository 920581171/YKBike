package com.yk.bike.response;

import java.security.Timestamp;
import java.util.List;

public class BikeRecordListResponse extends BaseResponse {

    private List<BikeRecordResponse.BikeRecord> data;

    public List<BikeRecordResponse.BikeRecord> getData() {
        return data;
    }

    public void setData(List<BikeRecordResponse.BikeRecord> data) {
        this.data = data;
    }

}
