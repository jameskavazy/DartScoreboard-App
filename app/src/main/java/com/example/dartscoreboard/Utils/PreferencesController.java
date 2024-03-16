package com.example.dartscoreboard.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;

public final class PreferencesController {

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

    private static SharedPreferences getSharedPreferences() {
        Context context = DartsScoreboardApplication.getContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}