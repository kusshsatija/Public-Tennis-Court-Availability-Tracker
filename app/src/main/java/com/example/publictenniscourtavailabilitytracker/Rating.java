package com.example.publictenniscourtavailabilitytracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(primaryKeys = {"user_id","court_name"})
public class Rating {

    @NonNull
    @ColumnInfo(name="user_id")
    public String userId;
    @ColumnInfo(name="rating")
    public float rating;
    @ColumnInfo(name="date")
    @TypeConverters(DateConverter.class)
    public Date date;
    @NonNull
    @ColumnInfo(name="court_name")
    public String court;


    public Rating(String userId, float rating, String court){
        this(userId, rating, new Date(), court);
    }

    private Rating(@NonNull String userId, float rating, Date date, @NonNull String court) {
        this.userId = userId;
        this.rating = rating;
        this.date = date;
        this.court = court;
    }
}
