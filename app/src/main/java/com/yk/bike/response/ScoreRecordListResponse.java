package com.yk.bike.response;

import java.util.List;

public class ScoreRecordListResponse extends BaseResponse{

    /**
     * data : {"id":1,"recordId":"BR23662353","userId":"1901139739","balance":10,"createTime":"2019-03-01 11:56:53"}
     */

    private List<ScoreRecordResponse.ScoreRecord> data;

    public List<ScoreRecordResponse.ScoreRecord> getData() {
        return data;
    }

    public void setData(List<ScoreRecordResponse.ScoreRecord> data) {
        this.data = data;
    }
}
