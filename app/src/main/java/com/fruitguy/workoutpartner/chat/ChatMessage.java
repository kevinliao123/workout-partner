package com.fruitguy.workoutpartner.chat;

public class ChatMessage {
    String message, type, from;
    long timestamp;

    public ChatMessage(String message, String type, long timestamp, String from) {
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.from = from;
    }

    public ChatMessage() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
