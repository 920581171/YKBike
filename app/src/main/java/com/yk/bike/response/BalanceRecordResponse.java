package com.yk.bike.response;

import java.util.Date;

public class BalanceRecordResponse extends BaseResponse{

    /**
     * data : {"id":1,"recordId":"BR23662353","userId":"1901139739","balance":10,"createTime":"2019-03-01 11:56:53"}
     */

    private BalanceRecord data;

    public BalanceRecord getData() {
        return data;
    }

    public void setData(BalanceRecord data) {
        this.data = data;
    }

    public static class BalanceRecord {
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
        private float balance;
        private Date createTime;
        private String isExchange;

        public int getId() {
            return id;
        }

        public BalanceRecord setId(int id) {
            this.id = id;
            return this;
        }

        public String getRecordId() {
            return recordId;
        }

        public BalanceRecord setRecordId(String recordId) {
            this.recordId = recordId;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        public BalanceRecord setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public float getBalance() {
            return balance;
        }

        public BalanceRecord setBalance(float balance) {
            this.balance = balance;
            return this;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public BalanceRecord setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public String getIsExchange() {
            return isExchange;
        }

        public BalanceRecord setIsExchange(String isExchange) {
            this.isExchange = isExchange;
            return this;
        }
    }
}
