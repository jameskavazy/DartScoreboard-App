package com.example.dartscoreboard.match.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "set")
public class Set {

    @NonNull
    @PrimaryKey
    public String setId;

    public String matchId;

    public int winnerId;

    public int setNumber; //order of set

    public Set(String setId, String matchId, int setNumber) {
        this.setId = setId;
        this.matchId = matchId;
        this.setNumber = setNumber;
    }
}
