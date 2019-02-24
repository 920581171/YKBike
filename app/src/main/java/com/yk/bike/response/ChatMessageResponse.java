package com.yk.bike.response;

import com.yk.bike.websocket.ChatMessage;

public class ChatMessageResponse extends BaseResponse{

    /**
     * data : {"id":5,"chatId":"8237840638","fromId":"1901139739","toId":"admin47185","chatContent":"在吗？","chatType":"0"}
     */

    private ChatMessage data;

    public ChatMessage getData() {
        return data;
    }

    public ChatMessageResponse setData(ChatMessage data) {
        this.data = data;
        return this;
    }
}
