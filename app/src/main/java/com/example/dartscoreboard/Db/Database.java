package com.example.dartscoreboard.Db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dartscoreboard.Game.GameState;
import com.example.dartscoreboard.LiveMatches.LiveProMatchesDao;
import com.example.dartscoreboard.LiveMatches.Match;
import com.example.dartscoreboard.MatchHistory.MatchesDao;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserDao;
import com.example.dartscoreboard.Utils.Converters;

@androidx.room.Database(
        entities = {
                User.class,
                GameState.class,
                Match.class
        },
        version = 3)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract MatchesDao matchesDao();
    public abstract LiveProMatchesDao liveProMatchesDao();
    private static volatile Database instance;

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class,"scoreboard-db")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }







}
