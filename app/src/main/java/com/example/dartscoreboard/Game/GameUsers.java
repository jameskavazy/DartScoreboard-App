package com.example.dartscoreboard.Game;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(primaryKeys = {"userID", "gameID"})
public class GameUsers {
    public long userID;
    @NonNull
    public String gameID;
    public int position;

    public GameUsers(long userID, String gameID, int position){
        this.userID = userID;
        this.gameID = gameID;
        this.position = position;
    }
}
