package com.example.dartscoreboard.MatchHistory;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dartscoreboard.Game.GameState;

import java.util.List;



@Dao
public interface MatchesDao {

    @Update
    void updateGameState(GameState gameState);

    @Insert
    long insertGameState(GameState gameState);

    @Delete
    void deleteGameState(GameState gameState);

    @Query("DELETE FROM match_history")
    void deleteAllMatchHistory();

    @Query("SELECT * FROM match_history ORDER BY offsetDateTime DESC")
    LiveData<List<GameState>> getAllMatchHistory();

    @Query("SELECT * FROM match_history WHERE gameID LIKE :id")
    LiveData<GameState> findGameByID(int id);

    @Query("DELETE FROM match_history WHERE gameID = :id")
    void deleteGameStateByID(long id);
}
