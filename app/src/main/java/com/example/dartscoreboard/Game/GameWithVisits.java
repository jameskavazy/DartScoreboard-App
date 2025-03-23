package com.example.dartscoreboard.Game;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GameWithVisits {
    @Embedded
    public Game game;
    @Relation(
            parentColumn = "gameId",
            entityColumn = "visitId"
    )
    public List<Visit> visits;

}
