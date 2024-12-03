package com.example.publictenniscourtavailabilitytracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(primaryKeys = {"user_id","court_name"})
public class Comment {
    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;

    @ColumnInfo(name="author")
    public String author;
    @ColumnInfo(name="rating")
    public float rating;
    @ColumnInfo(name="comment_text")
    public String commentText;
    @ColumnInfo(name="date")
    @TypeConverters(DateConverter.class)
    public Date date;
    @NonNull
    @ColumnInfo(name="court_name")
    public String court;


    public Comment(String userId, String author, float rating, String commentText, String court){
        this(userId, author, rating, commentText, new Date(), court);
    }

    private Comment(@NonNull String userId, String author, float rating, String commentText, Date date, @NonNull String court) {
        this.userId = userId;
        this.author = author;
        this.rating = rating;
        this.commentText = commentText;
        this.date = date;
        this.court = court;
    }
}
