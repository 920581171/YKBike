package com.yk.bike.response;

public class AdminInfoResponse extends BaseResponse {

    /**
     * data : {"id":1,"adminId":"admin47185","adminName":"admin","adminPassword":"25d55ad283aa400af464c76d713c07ad","adminPhone":"13800138000"}
     */

    private AdminInfo data;

    public AdminInfo getData() {
        return data;
    }

    public void setData(AdminInfo data) {
        this.data = data;
    }

    public static class AdminInfo {
        /**
         * id : 1
         * adminId : admin47185
         * adminName : admin
         * adminPassword : 25d55ad283aa400af464c76d713c07ad
         * adminPhone : 13800138000
         */

        private int id;
        private String adminId;
        private String adminAccount;
        private String adminName;
        private String adminPassword;
        private String adminPhone;
        private String adminType;
        private String adminOnline;

        public int getId() {
            return id;
        }

        public AdminInfo setId(int id) {
            this.id = id;
            return this;
        }

        public String getAdminId() {
            return adminId;
        }

        public AdminInfo setAdminId(String adminId) {
            this.adminId = adminId;
            return this;
        }

        public String getAdminAccount() {
            return adminAccount;
        }

        public AdminInfo setAdminAccount(String adminAccount) {
            this.adminAccount = adminAccount;
            return this;
        }

        public String getAdminName() {
            return adminName;
        }

        public AdminInfo setAdminName(String adminName) {
            this.adminName = adminName;
            return this;
        }

        public String getAdminPassword() {
            return adminPassword;
        }

        public AdminInfo setAdminPassword(String adminPassword) {
            this.adminPassword = adminPassword;
            return this;
        }

        public String getAdminPhone() {
            return adminPhone;
        }

        public AdminInfo setAdminPhone(String adminPhone) {
            this.adminPhone = adminPhone;
            return this;
        }

        public String getAdminType() {
            return adminType;
        }

        public AdminInfo setAdminType(String adminType) {
            this.adminType = adminType;
            return this;
        }

        public String getAdminOnline() {
            return adminOnline;
        }

        public AdminInfo setAdminOnline(String adminOnline) {
            this.adminOnline = adminOnline;
            return this;
        }

        public AdminInfo copy() {
            return new AdminInfo()
                    .setId(id)
                    .setAdminId(adminId)
                    .setAdminAccount(adminAccount)
                    .setAdminName(adminName)
                    .setAdminPassword(adminPassword)
                    .setAdminPhone(adminPhone)
                    .setAdminType(adminType);
        }
    }
}
