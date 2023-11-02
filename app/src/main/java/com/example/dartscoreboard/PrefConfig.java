package com.example.dartscoreboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefConfig {

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;
    public static final String userPrefs = "MY_PREFS";
    public static final String PLAYER_FOR_GAME = "player_for_game_prefs";

    public static final String GAME_STATE = "GAME_STATE";

    public static void updateSPUserList(Context context, ArrayList<User> usersList){

        Gson gson = new Gson();
        String jsonString = gson.toJson(usersList);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userPrefs,jsonString);
        editor.apply();
    }

    public static ArrayList<User> readSPUserList(Context context){
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);

        String jsonString = sharedPreferences1.getString(userPrefs,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        ArrayList<User> usersList = gson.fromJson(jsonString,type);
        return usersList;
    }

    public static void saveUsersForGameSP(Context context, ArrayList<User> usersList){
        Gson gson = new Gson();
        String jsonString = gson.toJson(usersList);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PLAYER_FOR_GAME,jsonString);
        editor.apply();
    }

    public static ArrayList<User> readUsersForGameSP(Context context){
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = sharedPreferences1.getString(PLAYER_FOR_GAME,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        ArrayList<User> usersList = gson.fromJson(jsonString,type);
        return usersList;
    }

//    public static void saveGameState (Context context, ArrayList<User> usersList){
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(usersList);
//        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString(GAME_STATE,jsonString);
//        editor.apply();
//    }
//
//    public static ArrayList<User> readGameState (Context context){
//        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
//        String jsonString = sharedPreferences1.getString(GAME_STATE,"");
//
//        Gson gson = new Gson();
//
//
//    }



}
