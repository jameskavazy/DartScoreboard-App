package com.example.dartscoreboard.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.dartscoreboard.match.data.models.MatchUsers;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

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
    @Insert
    void insertToGame(MatchUsers matchUsers);
    @Query("SELECT username FROM user WHERE userId = :userId")
    Single<String> getUsernameById(int userId);

}
