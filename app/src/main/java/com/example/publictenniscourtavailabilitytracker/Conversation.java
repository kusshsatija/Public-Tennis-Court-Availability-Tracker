package com.example.publictenniscourtavailabilitytracker;

import java.io.Serializable;
import java.util.List;

public class Conversation implements Serializable {
    private final List<Message> messages; // List of messages in the conversation
    private final String recipient;
    private final int recipientPic;

    public Conversation(String recipient, int recipientPic, List<Message> messages) {
        this.recipient = recipient;
        this.recipientPic = recipientPic;
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Message getMostRecentMessage() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    public String getRecipient() {
        return recipient;
    }

    public int getRecipientPic() {
        return recipientPic;
    }

    // Method to add a new message to the conversation
    public void addMessage(Message message) {
        messages.add(message);
    }
}
