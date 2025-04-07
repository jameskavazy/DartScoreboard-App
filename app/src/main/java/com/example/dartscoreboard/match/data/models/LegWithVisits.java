package com.example.dartscoreboard.match.data.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class LegWithVisits {
    @Embedded
    public Leg leg;
    @Relation(
            parentColumn = "legId",
            entityColumn = "legId"
    )
    public List<Visit> visits;

}
