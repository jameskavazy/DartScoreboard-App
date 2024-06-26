package com.example.dartscoreboard.User;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dartscoreboard.Utils.Converters;

@Database(entities = User.class, version = 1)
@TypeConverters({Converters.class})
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    private static volatile UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class,"userDb")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }







}
