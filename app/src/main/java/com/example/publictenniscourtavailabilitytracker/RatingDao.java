package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RatingDao {
    @Query("SELECT * FROM rating WHERE user_id = :userId AND court_name = :court")
    Rating findByUserIdAndCourt(String userId, String court);

    @Insert
    void insert(Rating rating);

    @Update
    void update(Rating rating);

    @Delete
    void delete(Rating rating);

}
