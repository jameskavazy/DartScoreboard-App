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
        return matchesDao.findGameByID(id);
    }


    public void deleteGameStateByID(long id){
        Completable completable = Completable.fromAction(()-> matchesDao.deleteGameStateByID(id));
        completable.subscribeOn(Schedulers.io()).subscribe();
    }
    public LiveData<List<GameState>> getAllMatchHistory(){
        return allMatchHistory;
    }




}
