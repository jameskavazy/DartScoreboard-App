package com.example.dartscoreboard.Game;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Db.Database;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameRepository {
    private final GameDao gameDao;
    private final LiveData<List<Game>> allMatchHistory;
    Database database;

    public GameRepository(Application application){
        database = Database.getInstance(application);
        gameDao = database.matchesDao();
        allMatchHistory = gameDao.getAllMatchHistory();
    }

    public Completable insert(Game game){
        Log.d("dom test", "repo insert");
        Completable completable = Completable.fromAction(() -> gameDao.insertGameState(game));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public Completable update(Game game){
        Completable completable = Completable.fromAction(() -> gameDao.updateGameState(game));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public Completable delete(Game game) {
        Completable completable = Completable.fromAction(() -> gameDao.deleteGameState(game));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public void deleteAll(){
        Completable completable = Completable.fromAction(gameDao::deleteAllMatchHistory);
        completable.subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<Game> getGameStateById(String id){
        return gameDao.findGameByID(id);
    }

    public void deleteGameStateByID(String id){
        Completable completable = Completable.fromAction(()-> gameDao.deleteGameStateByID(id));
        completable.subscribeOn(Schedulers.io()).subscribe();
    }
    public LiveData<List<Game>> getAllMatchHistory(){
        return allMatchHistory;
    }

    public Completable insertVisit(Visit visit){
        Completable completable = Completable.fromAction(() -> gameDao.insertVisit(visit));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public LiveData<List<Visit>> getVisitsInGame(String gameId){
        return gameDao.getVisitsInMatch(gameId);
    }

    public Single<List<Visit>> getMatchVisitsByUser(String gameId, int userId){
        return Single.fromCallable(()-> gameDao.getMatchVisitsByUser(gameId, userId)).subscribeOn(Schedulers.io());
    }


}
