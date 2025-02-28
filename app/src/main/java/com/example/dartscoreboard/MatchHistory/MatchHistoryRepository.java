package com.example.dartscoreboard.MatchHistory;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Db.Database;
import com.example.dartscoreboard.Game.GameState;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MatchHistoryRepository {
    private final MatchesDao matchesDao;
    private final LiveData<List<GameState>> allMatchHistory;
    Database database;

    public MatchHistoryRepository(Application application){
        database = Database.getInstance(application);
        matchesDao = database.matchesDao();
        allMatchHistory = matchesDao.getAllMatchHistory();
    }

    public Completable insert(GameState gameState){
        Log.d("dom test", "repo insert");
        Completable completable = Completable.fromAction(() -> matchesDao.insertGameState(gameState));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
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

    public void deleteAll(){
        Completable completable = Completable.fromAction(matchesDao::deleteAllMatchHistory);
        completable.subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<GameState> getGameStateById(int id){
        return matchesDao.findGameByID(id);
    }

    public void deleteGameStateByID(String id){
        Completable completable = Completable.fromAction(()-> matchesDao.deleteGameStateByID(id));
        completable.subscribeOn(Schedulers.io()).subscribe();
    }
    public LiveData<List<GameState>> getAllMatchHistory(){
        return allMatchHistory;
    }
}
