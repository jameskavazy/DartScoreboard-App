package com.example.dartscoreboard;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import kotlin.jvm.JvmStatic;

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
    public ArrayList<User> userListFromString (String userListJsonString){
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        return new Gson().fromJson(userListJsonString, type);
    }
    @TypeConverter
    public String userListToString (ArrayList<User> usersList){
        return new Gson().toJson(usersList);
    }

    @TypeConverter
    public GameSettings gameSettingsFromJsonString(String gameSettingsJsonString){
        return new Gson().fromJson(gameSettingsJsonString, GameSettings.class);
    }
    @TypeConverter
    public String gameSettingsToJsonString(GameSettings gameSettings){
        return new Gson().toJson(gameSettings);
    }

    @TypeConverter
    public SelectGameActivity.GameType gameTypeFromString (String gameTypeJsonString){
        return new Gson().fromJson(gameTypeJsonString, SelectGameActivity.GameType.class);
    }

    @TypeConverter
    public String gameTypeToString (SelectGameActivity.GameType gameType){
        return new Gson().toJson(gameType);
    }




}
