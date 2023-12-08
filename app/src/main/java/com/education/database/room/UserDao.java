package com.education.database.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(Users users);
    @Update
    void update(Users users);
    @Delete
    void delete(Users users);
    @Query("SELECT * FROM users WHERE id = :userId")
    Users getUserById(long userId);
    @Query("SELECT * FROM users ORDER BY id DESC")
    List<Users> getAllNotes();
}
