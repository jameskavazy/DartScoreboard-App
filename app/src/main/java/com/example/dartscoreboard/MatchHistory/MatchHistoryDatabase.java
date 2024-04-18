package com.example.dartscoreboard.MatchHistory;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dartscoreboard.Game.GameState;
import com.example.dartscoreboard.Utils.Converters;

@Database(entities = {GameState.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MatchHistoryDatabase extends RoomDatabase {

    public abstract MatchesDao matchesDao();

    private static volatile MatchHistoryDatabase instance;

    public static synchronized MatchHistoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                            , MatchHistoryDatabase.class, "matchesdb")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
