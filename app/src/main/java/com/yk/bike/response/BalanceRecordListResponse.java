package com.yk.bike.response;

import java.util.List;

public class BalanceRecordListResponse extends BaseResponse{

    /**
     * data : {"id":1,"recordId":"BR23662353","userId":"1901139739","balance":10,"createTime":"2019-03-01 11:56:53"}
     */

    private List<BalanceRecordResponse.BalanceRecord> data;

    public List<BalanceRecordResponse.BalanceRecord> getData() {
        return data;
    }

    public void setData(List<BalanceRecordResponse.BalanceRecord> data) {
        this.data = data;
    }
}
