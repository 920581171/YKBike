package com.yk.bike.response;

import java.util.Date;

public class DepositRecordResponse extends BaseResponse{

    /**
     * data : {"id":1,"recordId":"DR86484582","userId":"123456","deposit":12,"depositStatus":"1","createTime":"2019-02-28 22:58:39","updateTime":"2019-02-28 23:08:36"}
     */

    private DepositRecord data;

    public DepositRecord getData() {
        return data;
    }

    public void setData(DepositRecord data) {
        this.data = data;
    }

    public static class DepositRecord {
        /**
         * id : 1
         * recordId : DR86484582
         * userId : 123456
         * deposit : 12
         * depositStatus : 1
         * createTime : 2019-02-28 22:58:39
         * updateTime : 2019-02-28 23:08:36
         */

        private int id;
        private String recordId;
        private String userId;
        private int deposit;
        private Date createTime;

        public int getId() {
            return id;
        }

        public DepositRecord setId(int id) {
            this.id = id;
            return this;
        }

        public String getRecordId() {
            return recordId;
        }

        public DepositRecord setRecordId(String recordId) {
            this.recordId = recordId;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        public DepositRecord setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public int getDeposit() {
            return deposit;
        }

        public DepositRecord setDeposit(int deposit) {
            this.deposit = deposit;
            return this;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public DepositRecord setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }
    }
}
