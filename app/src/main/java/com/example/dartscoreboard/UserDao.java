package com.example.dartscoreboard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM user_table")
    void deleteAllUsers();

    @Query("SELECT * FROM user_table WHERE active = :active")
    LiveData<List<User>> getActiveUsers(boolean active);

}
