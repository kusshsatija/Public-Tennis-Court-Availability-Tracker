package com.example.publictenniscourtavailabilitytracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="author")
    public String author;
    @ColumnInfo(name="rating")
    public float rating;
    @ColumnInfo(name="comment_text")
    public String commentText;
    @ColumnInfo(name="date")
    @TypeConverters(DateConverter.class)
    public Date date;
    @ColumnInfo(name="court_name")
    public String court;


    public Comment(String author, float rating, String commentText, String court){
        this(author, rating, commentText, new Date(), court);
    }

    private Comment(String author, float rating, String commentText, Date date, String court) {
        this.author = author;
        this.rating = rating;
        this.commentText = commentText;
        this.date = date;
        this.court = court;
    }
}
