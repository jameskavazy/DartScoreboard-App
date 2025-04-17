package com.example.dartscoreboard.match.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.dartscoreboard.match.data.models.LegWithVisits;
import com.example.dartscoreboard.match.data.models.Leg;
import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchWithUsers;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.match.data.models.Visit;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MatchDao {

    //Matches
    @Insert
    void insertMatch(Match match);
    @Update
    void updateMatch(Match match);

    @Delete
    void deleteMatch(Match match);

    @Query("SELECT * FROM `match` ORDER BY datetime DESC")
    LiveData<List<Match>> getAllMatchHistory();

    @Query("DELETE FROM `match` WHERE winnerId != 0")
    void deleteAllUnfinishedMatches();

    @Query("SELECT * FROM `match` WHERE winnerId = 0 ORDER BY datetime DESC")
    LiveData<List<Match>> getUnfinishedGameHistory();

    @Query("UPDATE `match` SET winnerId = :userId WHERE matchId = :matchId")
    void setMatchWinner(int userId, String matchId);

    @Transaction
    @Query("SELECT * FROM `match` WHERE matchId = :matchId")
    Flowable<MatchWithUsers> getMatchData(String matchId);


    //Sets
    @Insert
    void insertSet(Set set);

    @Query("DELETE FROM `set` WHERE setId = :setId")
    void deleteSet(String setId);

    @Query("UPDATE `set` SET winnerId = :winnerId WHERE setId = :setId")
    void addSetWinner(String setId, int winnerId);

    @Query("SELECT COUNT(*) FROM `set` WHERE winnerId = :userId AND matchId = :matchId")
    Single<Integer> getSetsWon(int userId, String matchId);

    @Query("SELECT setId FROM `set` WHERE matchId = :matchId ORDER BY created_at DESC LIMIT 1")
    Single<String> getLatestSetId(String matchId);

    // Legs
    @Insert
    Completable insertLeg(Leg leg);

    @Query("DELETE FROM leg WHERE legId = :legId")
    void deleteLegById(String legId);

    @Query("UPDATE leg SET winnerId = :userId WHERE legId = :legId")
    void setLegWinner(int userId, String legId);

    @Query("SELECT COALESCE(SUM(score), 0) FROM visit WHERE legId = :gameId AND userId = :userId")
    Single<Integer> getLegTotalScore(String gameId, int userId);

    @Query("SELECT COUNT(*) FROM leg WHERE setId = :setId AND matchId = :matchId AND winnerId = :userId ")
    Single<Integer> legsWon(String setId, String matchId, int userId);
    @Query("UPDATE leg SET turn_index = :index WHERE legId = :gameId")
    void updateTurnIndex(int index, String gameId);

    @Transaction
    @Query("SELECT legId FROM leg WHERE matchId = :matchId ORDER BY created_at DESC LIMIT 1")
    Single<String> getLatestLegId(String matchId);

    // Visits
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVisit(Visit visit);

    @Query("DELETE FROM visit WHERE visitId = (SELECT MAX(visitId) FROM visit)")
    void deleteLastVisit();
    @Transaction
    @Query("SELECT * FROM leg WHERE matchId = :matchId ORDER BY created_at DESC LIMIT 1")
    Flowable<LegWithVisits> getLatestGameWithVisits(String matchId);

    @Query("SELECT COUNT(visitId) FROM visit WHERE legId = :legId AND userId = :userId")
    LiveData<Integer> countUserVisits(String legId, int userId);



    @Query("SELECT COUNT(winnerId) FROM `match` WHERE winnerId = :userId")
    Single<Integer> getUserMatchWins(int userId);

    @Query("SELECT COUNT(winnerId) " +
            "FROM `match` m " +
            "JOIN matchusers mu ON m.matchId = mu.matchId " +
            "WHERE winnerId != :userId AND winnerId != 0 AND mu.userId = :userId")
    Single<Integer> getUserMatchLosses(int userId);

    @Query("SELECT COUNT(userId) FROM MatchUsers WHERE userId = :userId")
    Single<Integer> getUserMatchesPlayed(int userId);

    @Query("SELECT COALESCE( (" +
            "SELECT COUNT(winnerId) * 100.0 / NULLIF((SELECT COUNT(matchId) FROM `match` WHERE winnerId != 0), 0)" +
                "FROM `match`" +
                "WHERE winnerId = :userId " +
            "), 0)")
    Single<Integer> getMatchWinRate(int userId);

    @Query("SELECT COALESCE( (" +
            "SELECT SUM(score) / IFNULL((SELECT COUNT(visitId) FROM visit WHERE userId = :userId), 0)" +
            "FROM visit" +
            " WHERE userId = :userId " +
            "), 0)")
    Single<Integer> getAvgAllMatches(int userId);

    @Query("SELECT COUNT(winnerId) FROM leg WHERE winnerId = :userId")
    Single<Integer> getLegsWon(int userId);

    @Query("SELECT COALESCE(" +
            "(" +
                "SELECT COUNT(winnerId) * 100.0 / (SELECT COUNT(visitId) FROM visit WHERE checkout = 1 AND userId = :userId) " +
                "FROM leg" +
                " WHERE winnerId =:userId" +
            ")" +
            ", 0)")
    Single<Integer> getCheckoutRate(int userId);

    @Query("SELECT COALESCE( (" +
            "SELECT COUNT(winnerId) * 100.0 / IFNULL(" +
            "(" +
                "SELECT COUNT(DISTINCT v.legId) " +
                "FROM visit v " +
                "JOIN leg l ON v.legId = l.legId " +
                "JOIN MatchUsers mu ON l.matchId = mu.matchId " +
                "WHERE v.userId = :userId AND mu.userId = :userId AND l.winnerId != 0" +
            ")" +
            ", 0)" +
            "FROM leg" +
            " WHERE winnerId = :userId" +
            "), 0)")
    Single<Integer> getLegWinRate(int userId);

}
