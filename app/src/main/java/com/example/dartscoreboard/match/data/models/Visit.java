package com.example.dartscoreboard.match.data.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "visit",
        foreignKeys = {
                @ForeignKey(
                        entity = Leg.class,
                        parentColumns = "legId",
                        childColumns = "legId"
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId"
                )
        },
        indices = {
                @Index(value = "legId"),
                @Index(value = "userId")
        }
)
public class Visit {

    @PrimaryKey(autoGenerate = true)
    public long visitId;

    public int userId;

    public String legId;
    public int score;
    public boolean checkout = false;

    public Visit() {
    }

    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
