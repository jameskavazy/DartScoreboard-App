package com.example.dartscoreboard.Game;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.User.User;

@Entity(tableName = "match_leg_set",
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
})
public class MatchLegsSets {
    @PrimaryKey (autoGenerate = true)
    public int matchLegSetId;

    public String gameId;

    public int userID;

    public Type type;

    public enum Type {
        Leg, Set
    }
    public MatchLegsSets(String gameId, int userID, Type type) {
        this.gameId = gameId;
        this.userID = userID;
        this.type = type;
    }

}
