package com.yk.bike.response;

import java.util.List;

public class ChatRoomListResponse extends BaseResponse{

    private List<ChatRoom> data;

    public List<ChatRoom> getData() {
        return data;
    }

    public void setData(List<ChatRoom> data) {
        this.data = data;
    }

    public static class ChatRoom {
        /**
         * id : 3
         * roomId : ROOM393379
         * fristId : admin47185
         * secondId : 1901139739
         */

        private int id;
        private String roomId;
        private String fristId;
        private String secondId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getFristId() {
            return fristId;
        }

        public void setFristId(String fristId) {
            this.fristId = fristId;
        }

        public String getSecondId() {
            return secondId;
        }

        public void setSecondId(String secondId) {
            this.secondId = secondId;
        }
    }
}
