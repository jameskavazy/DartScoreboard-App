package com.example.dartscoreboard.match.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        primaryKeys = {"userId", "matchId"},
        indices = {@Index(value = "matchId"), @Index(value = "userId")},
        foreignKeys = {@ForeignKey(
                entity = Match.class,
                parentColumns = "matchId",
                childColumns = "matchId",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )}
)
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
