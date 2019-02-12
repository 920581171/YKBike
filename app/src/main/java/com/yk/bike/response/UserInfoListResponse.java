package com.yk.bike.response;

import java.util.List;

public class UserInfoListResponse extends BaseResponse {

    /**
     * data : {"id":14,"userId":"1901139739","userName":"bbq","userPassword":"25d55ad283aa400af464c76d713c07ad","deposit":0,"balance":0}
     */

    private List<UserInfoResponse.UserInfo> data;

    public List<UserInfoResponse.UserInfo> getData() {
        return data;
    }

    public void setData(List<UserInfoResponse.UserInfo> data) {
        this.data = data;
    }

}
