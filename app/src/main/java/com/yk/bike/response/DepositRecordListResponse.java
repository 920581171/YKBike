package com.yk.bike.response;

import java.util.List;

public class DepositRecordListResponse extends BaseResponse{

    /**
     * data : {"id":1,"recordId":"DR86484582","userId":"123456","deposit":12,"depositStatus":"1","createTime":"2019-02-28 22:58:39","updateTime":"2019-02-28 23:08:36"}
     */

    private List<DepositRecordResponse.DepositRecord> data;

    public List<DepositRecordResponse.DepositRecord> getData() {
        return data;
    }

    public void setData(List<DepositRecordResponse.DepositRecord> data) {
        this.data = data;
    }
}
