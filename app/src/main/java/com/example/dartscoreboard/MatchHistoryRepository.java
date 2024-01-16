package com.example.dartscoreboard;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MatchHistoryRepository {
    private MatchesDao matchesDao;
    private LiveData<List<GameState>> allMatchHistory;
    MatchHistoryDatabase matchHistoryDatabase;

    public MatchHistoryRepository(Application application){
        matchHistoryDatabase = MatchHistoryDatabase.getInstance(application);
        matchesDao = matchHistoryDatabase.matchesDao();
        allMatchHistory = matchesDao.getAllMatchHistory();
    }

    public Single<Long> insert(GameState gameState){
        return Single.fromCallable(()-> matchesDao.insertGameState(gameState)).subscribeOn(Schedulers.io());
    }

//    public long insert(GameState gameState){
//        try {
//            return new InsertGameStateAsyncTask(matchesDao).execute(gameState).get();
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Completable update(GameState gameState){
        Completable completable = Completable.fromAction(() -> matchesDao.updateGameState(gameState));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public Completable delete(GameState gameState) {
        Completable completable = Completable.fromAction(() -> matchesDao.delete(gameState));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public Completable deleteAll(){
        Completable completable = Completable.fromAction(()-> matchesDao.deleteAllMatchHistory());
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public LiveData<GameState> getGameStateById(int id){
//        new GetGameStateByIdAsyncTask(matchesDao).execute(id);
        return matchesDao.findGameByID(id);
    }

//    public void deleteGameStateByID(long id){
//        new DeleteGameStateByIDAsyncTask(matchesDao).execute(id);
//    }

    public void deleteGameStateByID(long id){
        new DeleteGameStateByIDAsyncTask(matchesDao).execute(id);
    }
    public LiveData<List<GameState>> getAllMatchHistory(){
        return allMatchHistory;
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


    private static class GetGameStateByIdAsyncTask extends AsyncTask<Integer, Void, LiveData<GameState>>{
        private MatchesDao matchesDao;

        private GetGameStateByIdAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }
        @Override
        protected LiveData<GameState> doInBackground(Integer... id) {
            return matchesDao.findGameByID(id[0]);
        }
    }

    private static class DeleteGameStateByIDAsyncTask extends AsyncTask<Long,Void,Void>{

        private MatchesDao matchesDao;

        private DeleteGameStateByIDAsyncTask(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            matchesDao.deleteGameStateByID(longs[0]);
            return null;
        }
    }


}
