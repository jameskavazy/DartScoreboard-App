package com.example.dartscoreboard.Game;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"userID", "matchId"})
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
