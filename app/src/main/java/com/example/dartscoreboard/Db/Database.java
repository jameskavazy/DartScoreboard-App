package com.example.dartscoreboard.Db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.dartscoreboard.match.Game;
import com.example.dartscoreboard.match.MatchUsers;
import com.example.dartscoreboard.match.Match;
import com.example.dartscoreboard.match.Set;
import com.example.dartscoreboard.match.Visit;
import com.example.dartscoreboard.live_matches.LiveProMatchesDao;
import com.example.dartscoreboard.live_matches.ProMatch;
import com.example.dartscoreboard.match.GameDao;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserDao;
import com.example.dartscoreboard.util.Converters;

@androidx.room.Database(
        entities = {
                User.class,
                Game.class,
                Set.class,
                Match.class,
                ProMatch.class,
                MatchUsers.class,
                Visit.class
        },
        version = 1)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract GameDao matchesDao();
    public abstract LiveProMatchesDao liveProMatchesDao();
    private static volatile Database instance;

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class,"scoreboard-db")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private final static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    };







}
