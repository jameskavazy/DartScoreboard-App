package com.example.dartscoreboard.match.data.local;

import androidx.room.Dao;
import androidx.room.Query;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface StatsDao {

    @Query("SELECT COUNT(DISTINCT matchId) FROM match_stats_view WHERE winnerId = :userId")
    Single<Integer> getUserMatchWins(int userId);

    @Query("SELECT COUNT(DISTINCT matchId) " +
            "FROM match_stats_view " +
            "WHERE winnerId != :userId " +
            "AND userId = :userId"
    )
    Single<Integer> getUserMatchLosses(int userId);

    @Query("SELECT COUNT(DISTINCT matchId) " +
            "FROM match_stats_view " +
            "WHERE userId = :userId")
    Single<Integer> getUserMatchesPlayed(int userId);

    @Query("SELECT COALESCE( (" +
            "SELECT COUNT(DISTINCT matchId) * 100.0 / (SELECT COUNT(DISTINCT matchId) FROM match_stats_view WHERE userId = :userId)" +
            "FROM match_stats_view " +
            "WHERE winnerId = :userId " +
            "), 0)")
    Single<Integer> getMatchWinRate(int userId);

    @Query(
            "SELECT COALESCE( (" +
                    "SELECT SUM(score) / COUNT(DISTINCT visitId) " +
                    "FROM visit_stats_view " +
                    "WHERE userId = :userId " +
                    ")" +
                    ", 0)"
    )
    Single<Integer> getAvgAllMatches(int userId);

    @Query("SELECT COUNT(DISTINCT legId) " +
            "FROM leg_stats_view " +
            "WHERE winnerId = :userId")
    Single<Integer> getLegsWon(int userId);

    @Query("SELECT COALESCE(" +
            "(" +
                "SELECT COUNT(DISTINCT legId) * 100.0 / (SELECT COUNT(*) FROM visit_stats_view WHERE userId = :userId AND checkout = 1)" +
                "FROM visit_stats_view " +
                "WHERE legWinner = :userId " +
            ")" +
            ", 0)")
    Single<Integer> getCheckoutRate(int userId);

    @Query("SELECT COALESCE( (" +
            "SELECT COUNT(DISTINCT legId) * 100.0 / (" +
                "SELECT COUNT(DISTINCT legId) " +
                "FROM leg_stats_view " +
                "WHERE userId = :userId) " +
            "FROM leg_stats_view " +
            "WHERE winnerId = :userId " +
            "), 0)")
    Single<Integer> getLegWinRate(int userId);


    //TODO count of visits > 60, 100, 120, 140?
    // NON checkout average

}
