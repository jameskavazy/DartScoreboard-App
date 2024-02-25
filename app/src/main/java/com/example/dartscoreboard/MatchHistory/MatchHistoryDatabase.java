package com.example.dartscoreboard.MatchHistory;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private MatchesDao matchesDao;

        private PopulateDbAsyncTask(MatchHistoryDatabase db) {
            matchesDao = db.matchesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            matchesDao.updateGameState(new GameState(SelectGameActivity.GameType.FiveO,
//                    new GameSettings(5, 5),
//                    PreferencesController.readSPUserList(DartsScoreboardApplication.getContext()),
//                    0, 0, 0));
//            Log.d("dom test", String.valueOf(PreferencesController.readSPUserList(DartsScoreboardApplication.getContext()).get(0)));
            return null;
        }
    }
}
