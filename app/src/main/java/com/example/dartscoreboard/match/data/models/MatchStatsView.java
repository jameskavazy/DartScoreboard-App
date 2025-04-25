package com.example.dartscoreboard.match.data.models;

import androidx.room.DatabaseView;

@DatabaseView(
        value = "SELECT " +
                    "m.matchId, " +
                    "m.winnerId, " +
                    "mu.userId, " +
                    "m.game_type AS gameType " +
                "FROM `match` m " +
                "JOIN matchusers mu ON mu.matchId = m.matchId " +
                "WHERE m.matchId IN (SELECT vm.matchId FROM valid_match_view vm)",

        viewName = "match_stats_view"
)
public class MatchStatsView {
    String matchId;
    String winnerId;
    String userId;
    MatchType gameType;
}
