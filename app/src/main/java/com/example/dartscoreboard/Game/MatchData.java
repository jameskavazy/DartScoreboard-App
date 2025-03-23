package com.example.dartscoreboard.Game;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.dartscoreboard.User.User;

import java.util.List;

public class MatchData {
    @Embedded public Match match;
    @Relation(
            parentColumn = "matchId",
            entityColumn = "userID",
            associateBy = @Junction(MatchUsers.class)
    )
    public List<User> users;

    @Relation(
            parentColumn = "matchId",
            entityColumn = "gameId"
    )
    public List<Game> games;




}
