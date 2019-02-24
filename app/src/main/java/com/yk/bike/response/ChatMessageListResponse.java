package com.yk.bike.response;

import com.yk.bike.websocket.ChatMessage;

import java.util.List;

public class ChatMessageListResponse extends BaseResponse{

    /**
     * data : [{"id":5,"chatId":"8237840638","fromId":"1901139739","toId":"admin47185","chatContent":"在吗？","chatType":"0"}]
     */

    private List<ChatMessage> data;

    public List<ChatMessage> getData() {
        return data;
    }

    public ChatMessageListResponse setData(List<ChatMessage> data) {
        this.data = data;
        return this;
    }
}
