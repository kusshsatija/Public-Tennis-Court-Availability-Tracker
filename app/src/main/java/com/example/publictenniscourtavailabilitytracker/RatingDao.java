package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RatingDao {
    @Query("SELECT * FROM rating")
    List<Rating> getAll();

    @Query("SELECT * FROM rating WHERE user_id = :userId LIMIT 1")
    Rating findByUserId(String userId);

    @Insert
    void insert(Rating rating);

    @Delete
    void delete(Rating rating);
}
