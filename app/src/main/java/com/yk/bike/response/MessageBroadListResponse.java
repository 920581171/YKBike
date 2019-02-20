package com.yk.bike.response;

import java.util.List;

public class MessageBroadListResponse extends BaseResponse {

    /**
     * data : {"id":2,"messageId":"msg5941355","senderId":"123456","messageContent":"123456","messageStatus":"0"}
     */

    private List<MessageBroadResponse.MessageBroad> data;

    public List<MessageBroadResponse.MessageBroad> getData() {
        return data;
    }

    public void setData(List<MessageBroadResponse.MessageBroad> data) {
        this.data = data;
    }

}
