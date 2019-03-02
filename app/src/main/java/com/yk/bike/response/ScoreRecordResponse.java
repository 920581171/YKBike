package com.yk.bike.response;

import java.util.Date;

public class ScoreRecordResponse extends BaseResponse{

    /**
     * data : {"id":1,"recordId":"BR23662353","userId":"1901139739","balance":10,"createTime":"2019-03-01 11:56:53"}
     */

    private ScoreRecord data;

    public ScoreRecord getData() {
        return data;
    }

    public void setData(ScoreRecord data) {
        this.data = data;
    }

    public static class ScoreRecord {
        /**
         * id : 1
         * recordId : BR23662353
         * userId : 1901139739
         * balance : 10
         * createTime : 2019-03-01 11:56:53
         */

        private int id;
        private String recordId;
        private String userId;
        private int score;
        private Date createTime;

        public int getId() {
            return id;
        }

        public ScoreRecord setId(int id) {
            this.id = id;
            return this;
        }

        public String getRecordId() {
            return recordId;
        }

        public ScoreRecord setRecordId(String recordId) {
            this.recordId = recordId;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        public ScoreRecord setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public int getScore() {
            return score;
        }

        public ScoreRecord setScore(int score) {
            this.score = score;
            return this;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public ScoreRecord setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }
    }
}
