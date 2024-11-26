package com.example.publictenniscourtavailabilitytracker;

import java.io.Serializable;

public class Message implements Serializable {
    private final String content;
    private final String sender;

    public Message(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }
}
