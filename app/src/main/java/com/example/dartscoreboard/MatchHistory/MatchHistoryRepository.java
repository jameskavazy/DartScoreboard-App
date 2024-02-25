package com.example.dartscoreboard.MatchHistory;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Game.GameState;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
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
        Log.d("dom test", "repo insert");
        return Single.fromCallable(()-> matchesDao.insertGameState(gameState)).subscribeOn(Schedulers.io());
    }

    public Completable update(GameState gameState){
        Completable completable = Completable.fromAction(() -> matchesDao.updateGameState(gameState));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public Completable delete(GameState gameState) {
        Completable completable = Completable.fromAction(() -> matchesDao.deleteGameState(gameState));
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
