package com.example.dartscoreboard.match.data.local;

import androidx.room.Dao;
import androidx.room.Query;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface StatsDao {

    @Query("SELECT COUNT(DISTINCT matchId) FROM valid_match_performance_view WHERE matchWinner = :userId")
    Single<Integer> getUserMatchWins(int userId);

    @Query("SELECT COUNT(DISTINCT matchId) " +
            "FROM valid_match_performance_view " +
            "WHERE matchWinner != :userId " +
            "AND userId = :userId"
    )
    Single<Integer> getUserMatchLosses(int userId);

    @Query("SELECT COUNT(DISTINCT matchId) " +
            "FROM valid_match_performance_view " +
            "WHERE userId = :userId")
    Single<Integer> getUserMatchesPlayed(int userId);

    @Query("SELECT COALESCE( (" +
            "SELECT COUNT(DISTINCT matchId) * 100.0 / (SELECT COUNT(DISTINCT matchId) FROM valid_match_performance_view WHERE userId = :userId)" +
            "FROM valid_match_performance_view " +
            "WHERE matchWinner = :userId " +
            "), 0)")
    Single<Integer> getMatchWinRate(int userId);

    @Query(
            "SELECT COALESCE( (" +
                    "SELECT SUM(score) / (SELECT COUNT(visitId))" +
                    "FROM valid_match_performance_view " +
                    "WHERE userId = :userId " +
                    ")" +
                    ", 0)"
    )
    Single<Integer> getAvgAllMatches(int userId);

    @Query("SELECT COUNT(DISTINCT legWinner) FROM valid_match_performance_view WHERE legWinner = :userId")
    Single<Integer> getLegsWon(int userId);

    @Query("SELECT COALESCE(" +
            "(" +
            "SELECT (COUNT(*) * 100.0 / (" +
            "SELECT COUNT(visitId) " +
            "FROM valid_match_performance_view " +
            "WHERE userId = :userId " +
            "AND checkout = 1)" +
            ")" +
            "FROM valid_match_performance_view " +
            "WHERE legWinner = :userId " +
            "AND userId = :userId " +
            "AND checkout = 1" +
            ")" +
            ", 0)")
    Single<Integer> getCheckoutRate(int userId);

    @Query("SELECT COALESCE( (" +
            "SELECT COUNT(legWinner) * 100.0 / COUNT(DISTINCT legId) " +
            "FROM valid_match_performance_view " +
            "WHERE legWinner = :userId " +
            "AND userId = :userId" +
            "), 0)")
    Single<Integer> getLegWinRate(int userId);


    //TODO count of visits > 60, 100, 120, 140?
    // NON checkout average

}
