package com.hust.baseweb.applications.chat.chattext.model;

public class ChatMessage {
    private String id;
    private String content;
    private String sender;
    private String destination;
    private MessageType type;
    private ChatMessage replyTo;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatMessage getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(ChatMessage replyTo) {
        this.replyTo = replyTo;
    }

    public enum MessageType {
        CHAT, LEAVE, JOIN, PRIVATE_CHAT, GROUP_CHAT, ERROR
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
