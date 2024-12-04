package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Comment.class, Message.class}, version = 15)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommentDao userDao();
    public abstract MessageDao messageDao();
}