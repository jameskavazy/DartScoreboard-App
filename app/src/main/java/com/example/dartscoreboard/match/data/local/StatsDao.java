package com.example.dartscoreboard.match.data.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.dartscoreboard.match.models.Statistics;

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


    @Query("SELECT COALESCE(" +
            "( " +
                "SELECT SUM(score) / COUNT(DISTINCT visitId) " +
                    "FROM visit_stats_view " +
                    "WHERE userId = :userId " +
                    "AND checkout = 0" +
            ")" +
            ", 0)")
    Single<Integer> getNonCheckoutAvg(int userId);


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


    @Query("SELECT COUNT(visitId) " +
            "FROM visit_stats_view " +
            "WHERE score BETWEEN :scoreA AND :scoreB " +
            "AND userId = :userId")
    Single<Integer> getScoreSegments(int userId, int scoreA, int scoreB);



    @Query(
            "SELECT " +
                    "COUNT(DISTINCT CASE WHEN ms.winnerId = :userId THEN ms.matchId END) AS wins, " +
                    "COUNT(DISTINCT CASE WHEN ms.winnerId != :userId AND ms.userId = :userId THEN ms.matchId END) AS losses, " +
                    "COUNT(DISTINCT ms.matchId) AS matchesPlayed, " +
                    "(SELECT SUM(score) * 1.0 / COUNT(DISTINCT visitId) FROM visit_stats_view WHERE userId = :userId) AS averageScore,"+
                    "COALESCE( COUNT(DISTINCT CASE WHEN ms.winnerId = :userId THEN ms.matchId END) * 100.0 / COUNT(DISTINCT ms.matchId) , 0) AS matchWinRate, " +
                    "COALESCE(SUM(CASE WHEN vs.checkout = 0 THEN vs.score ELSE 0 END) / COUNT(DISTINCT CASE WHEN checkout = 0 THEN visitId END), 0) AS nonCheckoutAvg," +
                    "COUNT(DISTINCT CASE WHEN ls.winnerId = :userId THEN ls.legId END) AS legsWon, " +
                    "COALESCE( (COUNT(DISTINCT CASE WHEN ls.winnerId = :userId THEN ls.legId END) * 100.0 / COUNT(DISTINCT CASE WHEN vs.checkout = 1 THEN ls.legId END)), 0) AS checkoutRate, " +
                    "COALESCE( (COUNT(DISTINCT CASE WHEN ls.winnerId = :userId THEN ls.legId END) * 100.0 / COUNT(DISTINCT CASE WHEN ms.userId = :userId THEN ls.legId END)), 0) AS legWinRate, " +
                    "COUNT(CASE WHEN vs.score BETWEEN 0 AND 59 THEN visitId END) AS segmentBelow60, " +
                    "COUNT(CASE WHEN vs.score BETWEEN 60 AND 99 THEN visitId END) AS segment60To99, " +
                    "COUNT(CASE WHEN vs.score BETWEEN 100 AND 139 THEN visitId END) AS segment100To139, " +
                    "COUNT(CASE WHEN vs.score BETWEEN 140 AND 179 THEN visitId END) AS segment140To179, " +
                    "COUNT(CASE WHEN vs.score = 180 THEN visitId END) AS segment180 " +
                    "FROM match_stats_view ms " +
                    "JOIN visit_stats_view vs ON vs.matchId = ms.matchId " +
                    "JOIN leg_stats_view ls ON ls.matchId = ms.matchId " +
                    "WHERE ms.userId = :userId"
    )
    Single<Statistics> getUserStats(int userId);

}
