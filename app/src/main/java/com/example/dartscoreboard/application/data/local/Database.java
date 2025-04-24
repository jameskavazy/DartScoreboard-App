package com.example.dartscoreboard.application.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.dartscoreboard.match.data.local.MatchDao;
import com.example.dartscoreboard.match.data.local.StatsDao;
import com.example.dartscoreboard.match.data.models.Leg;
import com.example.dartscoreboard.match.data.models.MatchUsers;
import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.match.data.models.ValidMatchPerformanceView;
import com.example.dartscoreboard.match.data.models.Visit;
import com.example.dartscoreboard.match.data.models.User;
import com.example.dartscoreboard.match.data.local.UserDao;
import com.example.dartscoreboard.util.Converters;

@androidx.room.Database(
        entities = {
                User.class,
                Leg.class,
                Set.class,
                Match.class,
                MatchUsers.class,
                Visit.class
        },
        views = ValidMatchPerformanceView.class,
        version = 1)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract MatchDao matchesDao();
    public abstract StatsDao statsDao();
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
