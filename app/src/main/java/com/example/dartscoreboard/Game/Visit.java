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

    @PrimaryKey(autoGenerate = true)
    public long visitId;

    public int userID;

    public String gameId;
    public int score;

    public Visit() {
    }

    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



}
