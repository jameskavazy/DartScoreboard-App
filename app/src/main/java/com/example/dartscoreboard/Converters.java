package com.example.dartscoreboard;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public GameState gameStateFromString (String gameStateString){
        return new Gson().fromJson(gameStateString,GameState.class);
    }

    @TypeConverter
    public String gameStateToString (GameState gameState){
        return new Gson().toJson(gameState);
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
