package com.example.dartscoreboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public final class PreferencesController {
    public static final String userPrefs = "MY_PREFS";
    public static final String PLAYER_FOR_GAME = "player_for_game_prefs";

    public static final String GAME_SELECT = "GAME_SELECT";

    private static PreferencesController preferenceController;


    private PreferencesController() {}

    public static PreferencesController getInstance() {
        if (preferenceController == null) {
            preferenceController = new PreferencesController();
        }

        return preferenceController;
    }

    public void saveGameState(GameState gameState, String key) {
        // save to shared pref
        String jsonString = new Gson().toJson(gameState);
        getSharedPreferences().edit().putString(key, jsonString).apply();
//        for (User user : readGameState(key).getPlayerList()) {
//            Log.d("dom test", "saveGameState " + user.username + " " + user.playerScore);
//        }
    }

    public GameState readGameState(String key) {
        String gameStateJsonString = getSharedPreferences().getString(key, null);
        return new Gson().fromJson(gameStateJsonString, GameState.class);
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

//    public void serializeStack(Stack<GameState> gameStateStack){
//        String jsonString = new Gson().toJson(gameStateStack);
//        getSharedPreferences().edit().putString(GameActivity.STACK_KEY,jsonString).apply();
//    }
//
//    public Stack<GameState> deSerializeStack(){
//        String stackJsonString = getSharedPreferences().getString(GameActivity.STACK_KEY,null);
//        Gson gson = new Gson();
//        Type type = new TypeToken<Stack<GameState>>() {
//        }.getType();
//        return gson.fromJson(stackJsonString, type);
//    }




    public void clearGameState(String key) { // todo any more places this needs to be called.
        getSharedPreferences().edit().remove(key).apply();
    }

    public void clearUsersForGameSP(){
        getSharedPreferences().edit().remove(PLAYER_FOR_GAME).apply();
    }



    public static void updateSPUserList(Context context, ArrayList<User> usersList) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(usersList);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userPrefs, jsonString);
        editor.apply();
    }

    public static ArrayList<User> readSPUserList(Context context) {
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);

        String jsonString = sharedPreferences1.getString(userPrefs, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        ArrayList<User> usersList = gson.fromJson(jsonString, type);
        return usersList;
    }

    public static void saveUsersForGameSP(Context context, ArrayList<User> usersList) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(usersList);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PLAYER_FOR_GAME, jsonString);
        editor.apply();
    }

    public static ArrayList<User> readUsersForGameSP(Context context) {
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = sharedPreferences1.getString(PLAYER_FOR_GAME, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        ArrayList<User> usersList = gson.fromJson(jsonString, type);
        return usersList;
    }

    private static SharedPreferences getSharedPreferences() {
        Context context = DartsScoreboardApplication.getContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
