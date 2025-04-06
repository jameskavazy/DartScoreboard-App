package com.example.dartscoreboard.match.data.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.dartscoreboard.match.data.models.Game;
import com.example.dartscoreboard.match.data.models.Visit;

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
