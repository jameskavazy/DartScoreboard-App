package com.example.dartscoreboard.Game;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"userID", "gameID"})
public class GameUsers {
    public long userID;
    public long gameID;
    public int position;
}
