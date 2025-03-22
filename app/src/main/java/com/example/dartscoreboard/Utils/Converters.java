package com.example.dartscoreboard.Utils;

import androidx.room.TypeConverter;

import com.example.dartscoreboard.Game.GameData;
import com.example.dartscoreboard.Game.GameSettings;
import com.example.dartscoreboard.Game.GameType;
import com.example.dartscoreboard.User.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Converters {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public static OffsetDateTime timestampFromString(String localDateTimeString){
        return formatter.parse(localDateTimeString, OffsetDateTime::from);
    }

    @TypeConverter
    public static String timestampToString (OffsetDateTime timestamp){
        return timestamp.format(formatter);
    }

    @TypeConverter
    public List<User> userListFromString (String userListJsonString){
        Type type = new TypeToken<List<User>>() {}.getType();
        return new Gson().fromJson(userListJsonString, type);
    }
    @TypeConverter
    public String userListToString (List<User> usersList){
        return new Gson().toJson(usersList);
    }

    //type converter using clone to help keep Guy User intact.

    @TypeConverter
    public GameSettings gameSettingsFromJsonString(String gameSettingsJsonString){
        return new Gson().fromJson(gameSettingsJsonString, GameSettings.class);
    }
    @TypeConverter
    public String gameSettingsToJsonString(GameSettings gameSettings){
        return new Gson().toJson(gameSettings);
    }

    @TypeConverter
    public GameType gameTypeFromString (String gameTypeJsonString){
        return new Gson().fromJson(gameTypeJsonString, GameType.class);
    }

    @TypeConverter
    public String gameTypeToString (GameType gameType){
        return new Gson().toJson(gameType);
    }

    @TypeConverter
    public List<GameData> gameWithUsersFromJSON(String json) {
        return new Gson().fromJson(json, new TypeToken<List<GameData>>() {}.getType());
    }

    @TypeConverter
    public String gameWithUsersToJsonString(List<GameData> gameWithUsers) {
        return new Gson().toJson(gameWithUsers);
    }



}
