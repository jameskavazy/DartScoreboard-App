package com.example.dartscoreboard.Game;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameDao {

    @Insert
    void insertMatch(Match match);
    @Update
    void updateMatch(Match match);


    @Delete
    void deleteMatch(Match match);

    @Insert
    void insertGame(Game game);

    @Query("DELETE FROM `match`")
    void deleteAllMatchHistory();

    @Query("SELECT * FROM `match` ORDER BY datetime DESC")
    LiveData<List<Match>> getAllMatchHistory();

    @Query("SELECT * FROM `match` WHERE winnerId = 0 ORDER BY datetime DESC")
    LiveData<List<Match>> getUnfinishedGameHistory();

    @Query("SELECT * FROM game WHERE gameId = :id")
    LiveData<Game> findGameByID(String id);

    @Query("DELETE FROM game WHERE gameId = :id")
    void deleteGameStateByID(String id);

    @Query("SELECT * FROM visit WHERE gameId = :gameId")
    LiveData<List<Visit>> getVisitsInMatch(String gameId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVisit(Visit visit);

    @Query("SELECT COALESCE(SUM(score), 0) FROM visit WHERE gameId = :gameId AND userID = :userId")
    Single<Integer> getGameTotalScoreByUser(String gameId, int userId);

    @Query("DELETE FROM visit WHERE visitId = (SELECT MAX(visitId) FROM visit)")
    void deleteLastVisit();

    @Query("UPDATE game SET winnerId = :userId WHERE gameId = :gameId")
    void setWinner(int userId, String gameId);

    @Query("SELECT winnerId FROM game WHERE gameId = :gameId")
    Single<Integer> getWinner(String gameId);

    @Query("UPDATE game SET turn_index = :index WHERE gameId = :gameId")
    void updateTurnIndex(int index, String gameId);

    @Query("UPDATE game SET leg_index = :index WHERE gameId = :gameId")
    void updateLegIndex(int index, String gameId);

    @Query("UPDATE game SET set_index = :index WHERE gameId = :gameId")
    void updateSetIndex(int index, String gameId);

    @Transaction
    @Query("SELECT * FROM `match` WHERE matchId = :matchId")
    Flowable<MatchData> getGameData(String matchId);


}
