package com.example.dartscoreboard.Game;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

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

    @Query("SELECT * FROM `match` WHERE gameId = :id")
    LiveData<Game> findGameByID(String id);

    @Query("DELETE FROM `match` WHERE gameId = :id")
    void deleteGameStateByID(String id);

    @Query("SELECT * FROM visit WHERE gameId = :gameId")
    LiveData<List<Visit>> getVisitsInMatch(String gameId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVisit(Visit visit);
}
