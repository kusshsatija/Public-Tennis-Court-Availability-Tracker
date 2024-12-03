package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment WHERE court_name LIKE :court")
    List<Comment> listByCourt(String court);

    @Query(("SELECT * FROM comment WHERE court_name = :court AND user_id = :userId"))
    Comment findByUserIdAndCourt(String userId, String court);

    @Insert
    void insertAll(Comment... comments);

    @Update
    void update(Comment comment);

    @Delete
    void delete(Comment comment);
}
