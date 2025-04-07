package com.example.dartscoreboard.match.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.OffsetDateTime;

@Entity(tableName = "set")
public class Set {

    @NonNull
    @PrimaryKey
    public String setId;

    public String matchId;
    public int winnerId;

    @ColumnInfo(name = "created_at")
    public OffsetDateTime createdAt = OffsetDateTime.now();


    public Set(@NonNull String setId, String matchId) {
        this.setId = setId;
        this.matchId = matchId;
    }
}
