package com.example.dartscoreboard.match.data.models;

import androidx.room.DatabaseView;

@DatabaseView(
        viewName = "valid_match_view",
        value = "SELECT m.matchId " +
                "FROM `match` m " +
                "JOIN matchusers mu ON mu.matchId = m.matchId " +
                "GROUP BY m.matchId " +
                "HAVING COUNT(mu.userId) > 1 AND m.winnerId != 0"
)
public class ValidMatchView {
    String matchId;
}
