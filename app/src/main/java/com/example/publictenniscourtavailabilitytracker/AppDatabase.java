package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Comment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommentDao userDao();
}
