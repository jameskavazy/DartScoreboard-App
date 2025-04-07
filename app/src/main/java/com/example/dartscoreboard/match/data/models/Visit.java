package com.example.dartscoreboard.match.data.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.user.User;

@Entity(tableName = "visit",
        foreignKeys = {
                @ForeignKey(
                        entity = Leg.class,
                        parentColumns = "legId",
                        childColumns = "legId"
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userID",
                        childColumns = "userID"
                )
        },
        indices = {
                @Index(value = "legId"),
                @Index(value = "userID")
        }
)
public class Visit {

    @PrimaryKey(autoGenerate = true)
    public long visitId;

    public int userID;

    public String legId;
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

    public String getLegId() {
        return legId;
    }

    public void setLegId(String legId) {
        this.legId = legId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



}
