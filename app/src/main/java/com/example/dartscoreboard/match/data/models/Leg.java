package com.example.dartscoreboard.match.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.OffsetDateTime;


@Entity (tableName = "leg")
public class Leg implements Serializable {
    @PrimaryKey
    @NonNull
    public String legId;

    public String setId;

    public String matchId;

    @ColumnInfo(name = "turn_index")
    public int turnIndex;
    public int winnerId;
    @ColumnInfo(name = "created_at")
    public OffsetDateTime createdAt = OffsetDateTime.now();

    public Leg(@NonNull String legId, String setId, String matchId, int turnIndex) {
        this.legId = legId;
        this.setId = setId;
        this.matchId = matchId;
        this.turnIndex = turnIndex;
    }

    public void setLegId(@NonNull String legId) {
        this.legId = legId;
    }

    @NonNull
    public String getLegId() {
        return legId;
    }


    public int getTurnIndex() {
        return turnIndex;
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

}
