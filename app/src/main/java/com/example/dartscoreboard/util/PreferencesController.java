package com.example.dartscoreboard.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.dartscoreboard.application.DartsScoreboardApplication;
import com.example.dartscoreboard.match.data.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public final class PreferencesController {

    public static final String PLAYER_LIST = "PLAYER_LIST";
    public static final String GAME_SELECT = "GAME_SELECT";
    public static final String REMINDER_TIME = "REMINDER_TIME";
    private static PreferencesController preferenceController;

    private PreferencesController() {
    }

    public static PreferencesController getInstance() {
        if (preferenceController == null) {
            preferenceController = new PreferencesController();
        }
        return preferenceController;
    }

    public void saveSelectedGame(String game){
        getSharedPreferences().edit().putString(GAME_SELECT,game).apply();
    }
    public String getGameSelected(){
        return getSharedPreferences().getString(GAME_SELECT,null);
    }

    public void clearSelectedGame(){
        getSharedPreferences().edit().remove(GAME_SELECT).apply();
    }


    public void saveReminderTime(String time){
        getSharedPreferences().edit().putString(REMINDER_TIME, time).apply();
    }

    public String getReminderTime(){
        return getSharedPreferences().getString(REMINDER_TIME,null);
    }

    public void clearReminderTime(){
        getSharedPreferences().edit().remove(REMINDER_TIME).apply();
    }

    public List<User> getPlayers() {
        String jsonString = getSharedPreferences().getString(PLAYER_LIST,null);
        return new Gson().fromJson(jsonString, new TypeToken<ArrayList<User>>(){}.getType());
    }

    public void savePlayers(List<User> playerList){
        String jsonString = new Gson().toJson(playerList);
        getSharedPreferences().edit().putString(PLAYER_LIST,jsonString).apply();
    }


    private static SharedPreferences getSharedPreferences() {
        Context context = DartsScoreboardApplication.getContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


}
