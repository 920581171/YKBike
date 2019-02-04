package com.yk.bike.response;

import com.yk.bike.utils.NullObjectUtils;

public class UserInfoResponse extends BaseResponse {

    /**
     * data : {"id":14,"userId":"1901139739","userName":"bbq","userPassword":"25d55ad283aa400af464c76d713c07ad","deposit":0,"balance":0}
     */

    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public static class UserInfo {
        /**
         * id : 14
         * userId : 1901139739
         * userName : bbq
         * userPassword : 25d55ad283aa400af464c76d713c07ad
         * deposit : 0
         * balance : 0
         */

        private int id;
        private String userId;
        private String userName;
        private String userPhone;
        private String userPassword;
        private float deposit;
        private float balance;

        public int getId() {
            return id;
        }

        public UserInfo setId(int id) {
            this.id = id;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        public UserInfo setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        public UserInfo setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public UserInfo setUserPhone(String userPhone) {
            this.userPhone = userPhone;
            return this;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public UserInfo setUserPassword(String userPassword) {
            this.userPassword = userPassword;
            return this;
        }

        public float getDeposit() {
            return deposit;
        }

        public UserInfo setDeposit(float deposit) {
            this.deposit = deposit;
            return this;
        }

        public float getBalance() {
            return balance;
        }

        public UserInfo setBalance(float balance) {
            this.balance = balance;
            return this;
        }

        public UserInfo copy() {
            return new UserInfoResponse.UserInfo()
                    .setId(getId())
                    .setUserId(getUserId())
                    .setUserName(getUserName())
                    .setUserPassword(getUserPassword())
                    .setUserPhone(getUserPhone())
                    .setDeposit(getDeposit())
                    .setBalance(getBalance());
        }
    }
}
