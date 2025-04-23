package com.example.dartscoreboard.match.data.models;

import androidx.room.DatabaseView;

@DatabaseView(
        value = "SELECT " +
                    "v.visitId, " +
                    "v.score, " +
                    "v.userId," +
                    "v.legId, " +
                    "v.checkout, " +
                    "l.matchId, " +
                    "l.setId, " +
                    "l.winnerId AS legWinner, " +
                    "m.game_type, " +
                    "m.winnerId AS matchWinner, " +
                "COUNT(mu.userId) AS player_count " +
                "FROM visit v " +
                "JOIN leg l ON l.legId = v.legId " +
                "JOIN `match` m ON m.matchId = l.matchId " +
                "JOIN matchusers mu ON mu.matchId = m.matchId " +
                "GROUP BY v.visitId " +
                "HAVING player_count > 1 and m.winnerId != 0",
        viewName = "valid_match_performance_view"
)
public class ValidMatchPerformanceView {
    String visitId;
    int score;
    String userId;
    String legId;
    boolean checkout;
    String matchId;
    int matchWinner;
    int legWinner;
}
