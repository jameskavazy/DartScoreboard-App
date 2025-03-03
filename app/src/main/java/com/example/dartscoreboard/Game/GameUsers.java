package com.example.dartscoreboard.Game;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"userID", "gameId"})
public class GameUsers {
    public long userID;
    @NonNull
    public String gameId;
    public int position;

    public GameUsers(long userID, @NonNull String gameId, int position){
        this.userID = userID;
        this.gameId = gameId;
        this.position = position;
    }
}
