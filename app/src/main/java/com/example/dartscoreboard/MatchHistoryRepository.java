package com.example.dartscoreboard;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MatchHistoryRepository {
    private MatchesDao matchesDao;
    private LiveData<List<GameState>> allMatchHistory;

    public MatchHistoryRepository(Application application){
        MatchHistoryDatabase matchHistoryDatabase = MatchHistoryDatabase.getInstance(application);
        matchesDao = matchHistoryDatabase.matchesDao();
        allMatchHistory = matchesDao.getAllMatchHistory();
    }

    public long insert(GameState gameState){
        try {
            return new InsertGameStateAsyncTask(matchesDao).execute(gameState).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(GameState gameState){
        new UpdateGameStateAsyncTask(matchesDao).execute(gameState);
    }

    public void delete(GameState gameState){
        new DeleteGameStateAsyncTask(matchesDao).execute(gameState);
    }
    public void deleteAll(){
        new DeleteAllGameStatesAsyncTask(matchesDao).execute();

    }

    public void getGameStateById(){
        new GetGameStateByIdAsyncTask(matchesDao).execute();
    }

    public LiveData<List<GameState>> getAllMatchHistory(){
        return allMatchHistory;
    }

    private static class UpdateGameStateAsyncTask extends AsyncTask<GameState, Void, Void>{
        private MatchesDao matchesDao;

        private UpdateGameStateAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }
        @Override
        protected Void doInBackground(GameState... gameStates) {
            matchesDao.updateGameState(gameStates[0]);
            return null;
        }
    }

    private static class InsertGameStateAsyncTask extends AsyncTask<GameState, Void, Long>{
        private MatchesDao matchesDao;

        private InsertGameStateAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }
        @Override
        protected Long doInBackground(GameState... gameStates) {
            return matchesDao.insertGameState(gameStates[0]);
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

    private static class GetGameStateByIdAsyncTask extends AsyncTask<Integer, Void, Void>{
        private MatchesDao matchesDao;

        private GetGameStateByIdAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }
        @Override
        protected Void doInBackground(Integer... id) {
            matchesDao.findGameByID(id[0]);
            return null;
        }
    }


}
