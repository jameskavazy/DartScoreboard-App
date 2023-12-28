package com.example.dartscoreboard;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class MatchHistoryRepository {
    private MatchesDao matchesDao;
    private LiveData<List<GameState>> allMatchHistory;

    public MatchHistoryRepository(Application application){
        MatchHistoryDatabase matchHistoryDatabase = MatchHistoryDatabase.getInstance(application);
        matchesDao = matchHistoryDatabase.matchesDao();
        allMatchHistory = matchesDao.getAllMatchHistory();
    }

    public void upsert(GameState gameState){
        new UpsertGameStateAsyncTask(matchesDao).execute(gameState);
    }

    public void delete(GameState gameState){
        new DeleteGameStateAsyncTask(matchesDao).execute(gameState);
    }
    public void deleteAll(){
        new DeleteAllGameStatesAsyncTask(matchesDao).execute();

    }
    public LiveData<List<GameState>> getAllMatchHistory(){
        return allMatchHistory;
    }

    private static class UpsertGameStateAsyncTask extends AsyncTask<GameState, Void, Void>{
        private MatchesDao matchesDao;

        private UpsertGameStateAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }
        @Override
        protected Void doInBackground(GameState... gameStates) {
            matchesDao.upsertGameState(gameStates[0]);
            return null;
        }
    }
    private static class DeleteGameStateAsyncTask extends AsyncTask<GameState, Void, Void>{
        private MatchesDao matchesDao;

        private DeleteGameStateAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }
        @Override
        protected Void doInBackground(GameState... gameStates) {
            matchesDao.delete(gameStates[0]);
            return null;
        }
    }
    private static class DeleteAllGameStatesAsyncTask extends AsyncTask<Void, Void, Void>{
        private MatchesDao matchesDao;

        private DeleteAllGameStatesAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            matchesDao.deleteAllMatchHistory();
            return null;
        }
    }


}
