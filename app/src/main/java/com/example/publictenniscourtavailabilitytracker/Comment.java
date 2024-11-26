package com.example.publictenniscourtavailabilitytracker;

import java.util.Date;

public class Comment {
    public String author;
    public int rating;
    public String comment;
    public Date date;


    public Comment(String author, int rating, String comment){
        this(author, rating, comment, new Date());
    }

    private Comment(String author, int rating, String comment, Date date) {
        this.author = author;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }
}
