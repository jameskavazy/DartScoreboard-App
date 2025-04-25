package com.example.dartscoreboard.match.data.models;

import androidx.room.DatabaseView;

@DatabaseView(
        viewName = "visit_stats_view",
        value = "SELECT " +
                    "v.visitId, " +
                    "v.legId," +
                    "v.score, " +
                    "v.userId," +
                    "v.checkout, " +
                    "l.winnerId AS legWinner, " +
                    "m.matchId," +
                    "m.game_type AS gameType " +
                "FROM visit v " +
                "JOIN leg l ON l.legId = v.legId " +
                "JOIN `match` m ON m.matchId = l.matchId " +
                "WHERE m.matchId IN (SELECT vm.matchId FROM valid_match_view vm) "
)
public class VisitStatsView {
    int visitId;
    String legId;
    int score;
    int userId;
    boolean checkout;
    int legWinner;
    String matchId;
    MatchType gameType;

}
