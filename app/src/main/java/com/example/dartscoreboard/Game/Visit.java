package com.example.dartscoreboard.Game;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.User.User;

@Entity(tableName = "visit",
        foreignKeys = {
                @ForeignKey(
                        entity = Game.class,
                        parentColumns = "gameId",
                        childColumns = "gameId"
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userID",
                        childColumns = "userID"
                )
        },
        indices = {
                @Index(value = "gameId"),
                @Index(value = "userID")
        }
)
public class Visit {

    @PrimaryKey
    @NonNull
    public String visitId;

    public int userID;

    public String gameId;

    public int score;

    public Visit(String visitId) {
        this.visitId = visitId;
    }
}
