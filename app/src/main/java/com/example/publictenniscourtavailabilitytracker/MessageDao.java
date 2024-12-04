package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT MAX(rowid) FROM messages WHERE conversation = :conversation")
    int getLastMessagePosition(String conversation);

    @Insert
    void insert(Message... messages);

    @Insert
    void insertAll(Message... messages);

    // Mark all messages of a specific conversation as read
    @Query("UPDATE messages SET isUnread = 0 WHERE conversation = :conversation")
    void markMessagesAsRead(String conversation);

    @Query("SELECT * FROM messages ORDER BY id ASC")
    List<Message> getAllMessages(); // Fetches all messages from the database

    @Query("SELECT * FROM messages WHERE conversation = :conversation ORDER BY id ASC")
    List<Message> getMessagesByConversation(String conversation);

    @Query("SELECT conversation, content AS mostRecentMessage, recipientPic, isUnread, MAX(id) AS lastMessagePosition " +
            "FROM messages GROUP BY conversation ORDER BY lastMessagePosition DESC")
    List<ConversationSummary> getConversationSummaries();
}
