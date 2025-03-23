package com.example.dartscoreboard.Game;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.User.User;

import java.time.OffsetDateTime;
import java.util.List;

@Entity(tableName = "match")
public class Match {

    @PrimaryKey
    @NonNull
    public String matchId;

    @ColumnInfo(name = "datetime")
    public OffsetDateTime offsetDateTime = OffsetDateTime.now();

    @ColumnInfo(name = "game_type")
    public MatchType matchType;

    @ColumnInfo(name = "settings")
    public MatchSettings matchSettings;

    public String playersCSVString;

    public Match(@NonNull String matchId, MatchType matchType, MatchSettings matchSettings, OffsetDateTime offsetDateTime){
        this.matchId = matchId;
        this.matchType = matchType;
        this.matchSettings = matchSettings;
        this.offsetDateTime = offsetDateTime;
    }

    public void setCreatedDate(OffsetDateTime offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }
    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public MatchSettings getMatchSettings() {
        return matchSettings;
    }

    public void setMatchSettings(MatchSettings matchSettings) {
        this.matchSettings = matchSettings;
    }

    public OffsetDateTime getOffsetDateTime() {
        return offsetDateTime;
    }

    public long winnerId;

    public void setPlayersCSV(List<User> players) {

        if (players == null) {
            this.playersCSVString = "";
            return;
        }
        String[] namesOfGame = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            namesOfGame[i] = players.get(i).getUsername();
        }
        this.playersCSVString = String.join(", ", namesOfGame);
    }

}
