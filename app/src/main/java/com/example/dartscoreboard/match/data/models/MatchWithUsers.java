package com.example.dartscoreboard.match.data.models;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MatchWithUsers {
    @Embedded public Match match;
    @Relation(
            parentColumn = "matchId",
            entityColumn = "matchId",
            entity = MatchUsers.class
    )
    public List<MatchUserWithUser> matchUserWithUserData;

    @Relation(
            parentColumn = "matchId",
            entityColumn = "matchId"
    )
    public List<Set> sets;

    @Relation(
            parentColumn = "matchId",
            entityColumn = "matchId"
    )
    public List<Leg> legs;

    @Ignore
    public List<User> getOrderedUsers(){
        return matchUserWithUserData.stream()
                .sorted(Comparator.comparingInt(mu -> mu.matchUser.position))
                .map(mu -> mu.user)
                .collect(Collectors.toList());
    }
}

