package com.example.dartscoreboard.LiveMatches;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dartscoreboard.Utils.Converters;

@Database(entities = Match.class, version = 1)
@TypeConverters({Converters.class})
public abstract class LiveProMatchDatabase extends RoomDatabase {
    public abstract LiveProMatchesDao liveMatchesDao();

    private static volatile LiveProMatchDatabase instance;

    public static synchronized LiveProMatchDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                            , LiveProMatchDatabase.class, "promatchesdb")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}
