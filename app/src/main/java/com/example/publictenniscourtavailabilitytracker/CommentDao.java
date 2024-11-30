package com.example.publictenniscourtavailabilitytracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment")
    List<Comment> getAll();

    @Query("SELECT * FROM comment WHERE id IN (:userIds)")
    List<Comment> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM comment WHERE author = :author")
    Comment findByName(String author);

    @Query("SELECT * FROM comment WHERE court_name LIKE :court")
    List<Comment> listByCourt(String court);

    @Insert
    void insertAll(Comment... comments);

    @Insert
    void insert(Comment comment);

    @Delete
    void delete(Comment comment);
}
