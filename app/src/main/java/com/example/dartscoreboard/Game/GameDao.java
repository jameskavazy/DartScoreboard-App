package com.example.dartscoreboard.Game;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameDao {

    @Update
    void updateGameState(Game game);

    @Insert
    void insertGameState(Game game);

    @Delete
    void deleteGameState(Game game);

    @Query("DELETE FROM `match`")
    void deleteAllMatchHistory();

    @Query("SELECT * FROM `match` ORDER BY datetime DESC")
    LiveData<List<Game>> getAllMatchHistory();

    @Query("SELECT * FROM `match` WHERE gameID = :id")
    LiveData<Game> findGameByID(int id);

    @Query("DELETE FROM `match` WHERE gameID = :id")
    void deleteGameStateByID(String id);
}
