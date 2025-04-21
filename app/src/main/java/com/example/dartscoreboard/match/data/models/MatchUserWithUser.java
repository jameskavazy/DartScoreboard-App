package com.example.dartscoreboard.match.data.models;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MatchUserWithUser {
    @Embedded
    public MatchUsers matchUser;

    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"

    )
    public User user;

}
