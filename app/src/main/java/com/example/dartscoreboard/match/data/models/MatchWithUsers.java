package com.example.dartscoreboard.match.data.models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchUsers;
import com.example.dartscoreboard.user.User;
import com.example.dartscoreboard.match.data.models.Game;
import com.example.dartscoreboard.match.data.models.Set;

import java.util.List;

public class MatchWithUsers {
    @Embedded public Match match;
    @Relation(
            parentColumn = "matchId",
            entityColumn = "userID",
            associateBy = @Junction(MatchUsers.class)
    )
    public List<User> users;

    @Relation(
            parentColumn = "matchId",
            entityColumn = "matchId"
    )
    public List<Set> sets;

    @Relation(
            parentColumn = "matchId",
            entityColumn = "matchId"
    )
    public List<Game> games;
}

