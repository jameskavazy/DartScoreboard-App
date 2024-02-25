package com.example.dartscoreboard.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;

public final class PreferencesController {

    public static final String GAME_SELECT = "GAME_SELECT";
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

    private static SharedPreferences getSharedPreferences() {
        Context context = DartsScoreboardApplication.getContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
