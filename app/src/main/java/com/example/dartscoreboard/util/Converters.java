package com.example.dartscoreboard.util;

import androidx.room.TypeConverter;

import com.example.dartscoreboard.match.data.models.MatchWithUsers;
import com.example.dartscoreboard.match.data.models.MatchSettings;
import com.example.dartscoreboard.match.data.models.MatchType;
import com.example.dartscoreboard.match.data.models.User;
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

    //type converter using clone to help keep Guy User intact.

    @TypeConverter
    public MatchSettings gameSettingsFromJsonString(String gameSettingsJsonString){
        return new Gson().fromJson(gameSettingsJsonString, MatchSettings.class);
    }
    @TypeConverter
    public String gameSettingsToJsonString(MatchSettings matchSettings){
        return new Gson().toJson(matchSettings);
    }

    @TypeConverter
    public MatchType gameTypeFromString (String gameTypeJsonString){
        return new Gson().fromJson(gameTypeJsonString, MatchType.class);
    }

    @TypeConverter
    public String gameTypeToString (MatchType matchType){
        return new Gson().toJson(matchType);
    }




}
