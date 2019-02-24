package com.yk.bike.websocket;

import java.util.Date;

public class ChatMessage {
    private int id;
    private String chatId;
    private String fromId;
    private String toId;
    private String chatContent;
    private String chatType;
    private Date sendTime;

    public int getId() {
        return id;
    }

    public ChatMessage setId(int id) {
        this.id = id;
        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public ChatMessage setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getFromId() {
        return fromId;
    }

    public ChatMessage setFromId(String fromId) {
        this.fromId = fromId;
        return this;
    }

    public String getToId() {
        return toId;
    }

    public ChatMessage setToId(String toId) {
        this.toId = toId;
        return this;
    }

    public String getChatContent() {
        return chatContent;
    }

    public ChatMessage setChatContent(String chatContent) {
        this.chatContent = chatContent;
        return this;
    }

    public String getChatType() {
        return chatType;
    }

    public ChatMessage setChatType(String chatType) {
        this.chatType = chatType;
        return this;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public ChatMessage setSendTime(Date sendTime) {
        this.sendTime = sendTime;
        return this;
    }
}
