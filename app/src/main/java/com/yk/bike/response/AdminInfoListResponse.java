package com.yk.bike.response;

import java.util.List;

public class AdminInfoListResponse extends BaseResponse{

    /**
     * data : {"id":1,"adminId":"admin47185","adminName":"admin","adminPassword":"25d55ad283aa400af464c76d713c07ad","adminPhone":"13800138000"}
     */

    private List<AdminInfoResponse.AdminInfo> data;

    public List<AdminInfoResponse.AdminInfo> getData() {
        return data;
    }

    public void setData(List<AdminInfoResponse.AdminInfo> data) {
        this.data = data;
    }

}
