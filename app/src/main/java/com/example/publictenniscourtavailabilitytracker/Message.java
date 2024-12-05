package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "conversation")
    private String conversation;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "sender")
    private String sender;

    @ColumnInfo(name = "recipientPic")
    private int recipientPic;

    @ColumnInfo(name = "isUnread")
    private boolean isUnread;

    // Constructors
    public Message(String conversation, String content, String sender, int recipientPic, boolean isUnread) {
        this.conversation = conversation;
        this.content = content;
        this.sender = sender;
        this.recipientPic = recipientPic;
        this.isUnread = isUnread;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getRecipientPic() {
        return recipientPic;
    }

    public void setRecipientPic(int recipientPic) {
        this.recipientPic = recipientPic;
    }

    public boolean getIsUnread() {
        return isUnread;
    }

    public void setUnread(boolean isUnread) {
        this.isUnread = isUnread;
    }
}
