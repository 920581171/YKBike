package com.yk.bike.response;

public class MessageBroadResponse extends BaseResponse {

    /**
     * data : {"id":2,"messageId":"msg5941355","senderId":"123456","messageContent":"123456","messageStatus":"0"}
     */

    private MessageBroad data;

    public MessageBroad getData() {
        return data;
    }

    public void setData(MessageBroad data) {
        this.data = data;
    }

    public static class MessageBroad {
        /**
         * id : 2
         * messageId : msg5941355
         * senderId : 123456
         * messageContent : 123456
         * messageStatus : 0
         */

        private int id;
        private String messageId;
        private String senderId;
        private String handlerId;
        private String handlerName;
        private String messageContent;
        private String messageReply;
        private String messageStatus;
        private String messageType;

        public int getId() {
            return id;
        }

        public MessageBroad setId(int id) {
            this.id = id;
            return this;
        }

        public String getMessageId() {
            return messageId;
        }

        public MessageBroad setMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public String getSenderId() {
            return senderId;
        }

        public MessageBroad setSenderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public String getHandlerId() {
            return handlerId;
        }

        public MessageBroad setHandlerId(String handlerId) {
            this.handlerId = handlerId;
            return this;
        }

        public String getHandlerName() {
            return handlerName;
        }

        public MessageBroad setHandlerName(String handlerName) {
            this.handlerName = handlerName;
            return this;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public MessageBroad setMessageContent(String messageContent) {
            this.messageContent = messageContent;
            return this;
        }

        public String getMessageReply() {
            return messageReply;
        }

        public MessageBroad setMessageReply(String messageReply) {
            this.messageReply = messageReply;
            return this;
        }

        public String getMessageStatus() {
            return messageStatus;
        }

        public MessageBroad setMessageStatus(String messageStatus) {
            this.messageStatus = messageStatus;
            return this;
        }

        public String getMessageType() {
            return messageType;
        }

        public MessageBroad setMessageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public MessageBroad copy() {
            return new MessageBroad()
                    .setId(id)
                    .setMessageId(messageId)
                    .setSenderId(senderId)
                    .setHandlerId(handlerId)
                    .setHandlerName(handlerName)
                    .setMessageContent(messageContent)
                    .setMessageReply(messageReply)
                    .setMessageStatus(messageStatus)
                    .setMessageType(messageType);
        }
    }
}
