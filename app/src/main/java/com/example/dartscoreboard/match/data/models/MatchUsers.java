package com.example.dartscoreboard.match.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"userId", "matchId"}, indices = {@Index(value = "matchId")})
public class MatchUsers {

    public long userId;
    @NonNull
    public String matchId;
    public int position;

    public MatchUsers(long userId, @NonNull String matchId, int position){
        this.userId = userId;
        this.matchId = matchId;
        this.position = position;
    }
}
