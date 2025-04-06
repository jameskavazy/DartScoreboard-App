package com.example.dartscoreboard.match.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"userID", "matchId"}, indices = {@Index(value = "matchId")})
public class MatchUsers {
    public long userID;
    @NonNull
    public String matchId;
    public int position;

    public MatchUsers(long userID, @NonNull String matchId, int position){
        this.userID = userID;
        this.matchId = matchId;
        this.position = position;
    }
}
