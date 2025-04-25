package com.example.dartscoreboard.match.data.models;

import androidx.room.DatabaseView;

@DatabaseView(
        viewName = "leg_stats_view",
        value = "SELECT " +
                    "l.legId, " +
                    "l.winnerId, " +
                    "mu.userId," +
                    "m.matchId," +
                    "m.game_type AS gameType " +
                "FROM leg l " +
                "JOIN `match` m ON m.matchId = l.matchId " +
                "JOIN matchusers mu ON mu.matchId = m.matchId " +
                "WHERE l.matchId IN (SELECT matchId FROM valid_match_view)"
)
public class LegStatsView {
    String legId;
    String winnerId;
    String userId;

    String matchId;

    MatchType gameType;
}
