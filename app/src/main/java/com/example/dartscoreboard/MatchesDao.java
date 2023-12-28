package com.example.dartscoreboard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MatchesDao {

    @Upsert
    void upsertGameState(GameState gameState);

    @Delete
    void delete(GameState gameState);

    @Query("DELETE FROM match_history")
    void deleteAllMatchHistory();

    @Query("SELECT * FROM match_history")
    LiveData<List<GameState>> getAllMatchHistory();


//    @Query("SELECT * FROM matches WHERE gameID LIKE :id")
//    Maybe<GameState> findByGameID(int id);


//    @Query("SELECT * FROM matches")
//    Maybe<ArrayList<GameState>> getAllMatches(); //todo Flowable instead of maybe

}
