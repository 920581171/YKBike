package com.yk.bike.response;

public class BikeTypeResponse extends BaseResponse{

    private BikeType data;

    public BikeType getData() {
        return data;
    }

    public void setData(BikeType data) {
        this.data = data;
    }

    public static class BikeType {
        /**
         * id : 0
         * typeId : TYPE100154
         * typeValue : type_01
         * typeName : 公路车
         * unitPrice : 1
         */

        private int id;
        private String typeId;
        private String typeValue;
        private String typeName;
        private float unitPrice;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getTypeValue() {
            return typeValue;
        }

        public void setTypeValue(String typeValue) {
            this.typeValue = typeValue;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public float getUnitPrice() {
            return unitPrice;
        }

        public BikeType setUnitPrice(float unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }
    }
}
