package com.yk.bike.response;

public class BikeInfoResponse extends BaseResponse{

    /**
     * data : {"id":5,"bikeId":"123456","latitude":123456,"longitude":123456,"mileage":0,"fix":"0"}
     */

    private BikeInfo data;

    public BikeInfo getData() {
        return data;
    }

    public void setData(BikeInfo data) {
        this.data = data;
    }

    public static class BikeInfo {
        /**
         * id : 5
         * bikeId : 123456
         * latitude : 123456
         * longitude : 123456
         * mileage : 0
         * fix : 0
         */

        private int id;
        private String bikeId;
        private String bikeType;
        private String userId;
        private double latitude;
        private double longitude;
        private float mileage;
        private String fix;

        public int getId() {
            return id;
        }

        public BikeInfo setId(int id) {
            this.id = id;
            return this;
        }

        public String getBikeId() {
            return bikeId;
        }

        public BikeInfo setBikeId(String bikeId) {
            this.bikeId = bikeId;
            return this;
        }

        public String getBikeType() {
            return bikeType;
        }

        public BikeInfo setBikeType(String bikeType) {
            this.bikeType = bikeType;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        public BikeInfo setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public double getLatitude() {
            return latitude;
        }

        public BikeInfo setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public double getLongitude() {
            return longitude;
        }

        public BikeInfo setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public float getMileage() {
            return mileage;
        }

        public BikeInfo setMileage(float mileage) {
            this.mileage = mileage;
            return this;
        }

        public String getFix() {
            return fix;
        }

        public BikeInfo setFix(String fix) {
            this.fix = fix;
            return this;
        }
    }
}
