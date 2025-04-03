package com.example.dartscoreboard.match;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GameWithVisits {
    @Embedded
    public Game game;
    @Relation(
            parentColumn = "gameId",
            entityColumn = "gameId"
    )
    public List<Visit> visits;

}
