package com.example.publictenniscourtavailabilitytracker;

public class ConversationSummary {

    private String conversation;
    private String mostRecentMessage;
    private int recipientPic;
    private boolean isUnread;
    private int lastMessagePosition;  // This field to track the last message position

    // Constructor
    public ConversationSummary(String conversation, String mostRecentMessage, int recipientPic, boolean isUnread, int lastMessagePosition) {
        this.conversation = conversation;
        this.mostRecentMessage = mostRecentMessage;
        this.recipientPic = recipientPic;
        this.isUnread = isUnread;
        this.lastMessagePosition = lastMessagePosition;
    }

    // Getters and setters
    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public String getMostRecentMessage() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(String mostRecentMessage) {
        this.mostRecentMessage = mostRecentMessage;
    }

    public int getRecipientPic() {
        return recipientPic;
    }

    public void setRecipientPic(int recipientPic) {
        this.recipientPic = recipientPic;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }

    public int getLastMessagePosition() {
        return lastMessagePosition;
    }

    public void setLastMessagePosition(int lastMessagePosition) {
        this.lastMessagePosition = lastMessagePosition;
    }
}
