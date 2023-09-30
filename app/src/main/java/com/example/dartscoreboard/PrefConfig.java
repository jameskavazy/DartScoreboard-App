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
    public static final String userPrefs = "my prefs";
    public static final String userNameKey = "nameKey";

    public static void writeListInPref(Context context, ArrayList<User> usersList){

        Gson gson = new Gson();
        String jsonString = gson.toJson(usersList);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userPrefs,jsonString);
        editor.apply();
    }

    public static ArrayList<User> readListFromPref (Context context){
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);

        String jsonString = sharedPreferences1.getString(userPrefs,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        ArrayList<User> usersList = gson.fromJson(jsonString,type);
        return usersList;
      //todo keep following video from here you're so close.
    }
}
