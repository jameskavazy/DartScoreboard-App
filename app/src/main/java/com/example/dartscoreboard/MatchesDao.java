package com.example.dartscoreboard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MatchesDao {

    @Update
    void updateGameState(GameState gameState);

    @Insert
    long insertGameState(GameState gameState); //todo fix long

    @Delete
    void delete(GameState gameState);

    @Query("DELETE FROM match_history")
    void deleteAllMatchHistory();

    @Query("SELECT * FROM match_history")
    LiveData<List<GameState>> getAllMatchHistory();

    @Query("SELECT * FROM match_history WHERE gameID LIKE :id")
    LiveData<GameState> findGameByID(int id);

    @Query("DELETE FROM match_history WHERE gameID = :id")
    void deleteGameStateByID(long id);

//    @Query("SELECT * FROM matches WHERE gameID LIKE :id")
//    Maybe<GameState> findByGameID(int id);


//    @Query("SELECT * FROM matches")
//    Maybe<ArrayList<GameState>> getAllMatches(); //todo Flowable instead of maybe

}
