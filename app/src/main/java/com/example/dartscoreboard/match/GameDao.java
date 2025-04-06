package com.example.dartscoreboard.match;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameDao {

    @Insert
    void insertMatch(Match match);
    @Update
    void updateMatch(Match match);

    @Delete()
    void deleteMatch(Match match);

    @Insert
    Completable insertGame(Game game);

    @Query("DELETE FROM `match`")
    void deleteAllMatchHistory();

    @Query("SELECT * FROM `match` ORDER BY datetime DESC")
    LiveData<List<Match>> getAllMatchHistory();

    @Query("SELECT * FROM `match` WHERE winnerId = 0 ORDER BY datetime DESC")
    LiveData<List<Match>> getUnfinishedGameHistory();

    @Query("SELECT * FROM game WHERE gameId = :id")
    LiveData<Game> findGameByID(String id);
    @Query("SELECT * FROM game WHERE matchId = :matchId")
    Flowable<List<Game>> getGamesInMatch(String matchId);

    @Query("DELETE FROM game WHERE matchId = :matchId AND winnerId = :winnerId ")
    void deleteGameStateByID(String matchId, int winnerId);

    @Query("SELECT * FROM visit WHERE gameId = :gameId")
    LiveData<List<Visit>> getVisitsInMatch(String gameId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVisit(Visit visit);

    @Query("SELECT COALESCE(SUM(score), 0) FROM visit WHERE gameId = :gameId AND userID = :userId")
    Single<Integer> getGameTotalScoreByUser(String gameId, int userId);

    @Query("DELETE FROM visit WHERE visitId = (SELECT MAX(visitId) FROM visit)")
    void deleteLastVisit();

    @Query("UPDATE game SET winnerId = :userId, set_number = :setNumber WHERE gameId = :gameId")
    void setGameWinner(int userId, String gameId, int setNumber);

    @Query("UPDATE `match` SET winnerId = :userId WHERE matchId = :matchId")
    void setMatchWinner(int userId, String matchId);

    @Query("SELECT winnerId FROM game WHERE gameId = :gameId")
    Single<Integer> getWinner(String gameId);
    @Query("SELECT COUNT(*) FROM game WHERE setId = :setId AND matchId = :matchId AND winnerId = :userId ")
    Single<Integer> legsWon(String setId, String matchId, int userId);
    @Query("UPDATE game SET turn_index = :index WHERE gameId = :gameId")
    void updateTurnIndex(int index, String gameId);

    @Query("UPDATE game SET leg_index = :index WHERE gameId = :gameId")
    void updateLegIndex(int index, String gameId);

    @Query("UPDATE game SET set_index = :index WHERE gameId = :gameId")
    void updateSetIndex(int index, String gameId);

    @Transaction
    @Query("SELECT * FROM game WHERE matchId = :matchId ORDER BY created_at DESC LIMIT 1")
    Flowable<GameWithVisits> getLatestGameWithVisits(String matchId);

    @Transaction
    @Query("SELECT * FROM `match` WHERE matchId = :matchId")
    Flowable<MatchWithUsers> getMatchData(String matchId);

    @Query("UPDATE `set` SET winnerId = :winnerId WHERE setId = :setId")
    void addSetWinner(String setId, int winnerId);

    @Query("SELECT COUNT(*) FROM `set` WHERE winnerId = :userId AND matchId = :matchId")
    Single<Integer> getSetsWon(int userId, String matchId);


    @Insert
    void insertSet(Set set);

    @Update
    void updateSet(Set set);

    @Query("SELECT * FROM `set` WHERE matchId = :matchId")
    Flowable<List<Set>> getSetsInMatch(String matchId);



}
