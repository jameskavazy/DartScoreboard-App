package com.example.dartscoreboard.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.dartscoreboard.Game.GameUsers;
import com.example.dartscoreboard.Game.GameWithUsers;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM user")
    void deleteAllUsers();

    @Transaction
    @Query("SELECT * FROM `match` WHERE gameID = :gameId")
    LiveData<GameWithUsers> getUsersFromGame(String gameId);

    @Transaction
    @Insert
    void insertToGame(GameUsers gameUsers);

}
