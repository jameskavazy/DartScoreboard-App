package com.example.dartscoreboard.Game;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Db.Database;
import com.example.dartscoreboard.User.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameRepository {
    private final GameDao gameDao;
    private final LiveData<List<Game>> unfinishedGamesHistory;
    Database database;

    public GameRepository(Application application){
        database = Database.getInstance(application);
        gameDao = database.matchesDao();
        unfinishedGamesHistory = gameDao.getUnfinishedGameHistory();
    }

    public Flowable<GameData> getGameData(String gameId){
        return gameDao.getGameData(gameId);
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
    public LiveData<List<Game>> getUnfinishedGamesHistory(){
        return unfinishedGamesHistory;
    }

    public Completable insertVisit(Visit visit){
        return Completable.fromAction(() -> gameDao.insertVisit(visit)).subscribeOn(Schedulers.io());
//        Completable completable = Completable.fromAction(() -> gameDao.insertVisit(visit));
//        completable.subscribeOn(Schedulers.io()).subscribe();
//        return completable;
    }

    public LiveData<List<Visit>> getVisitsInGame(String gameId){
        return gameDao.getVisitsInMatch(gameId);
    }

    public Single<Integer> getGameTotalScoreByUser(String gameId, int userId){
       return gameDao.getGameTotalScoreByUser(gameId, userId);
//        return Single.fromCallable(()-> gameDao.getGameTotalScoreByUser(gameId, userId)).subscribeOn(Schedulers.io());
    }

    public void deleteLatestVisit(){
       Completable.fromAction(gameDao::deleteLastVisit).subscribeOn(Schedulers.io()).subscribe();
    }

    public void setWinner(int userId, String gameId){
        Completable.fromAction(() -> gameDao.setWinner(userId, gameId)).subscribeOn(Schedulers.io()).subscribe();
    }

    public Single<Integer> getWinner(String gameId){
        return gameDao.getWinner(gameId);
    }

    public void updateTurnIndex(int index, String gameId){
        Completable.fromAction(() -> gameDao.updateTurnIndex(index, gameId)).subscribeOn(Schedulers.io()).subscribe();
    }

    public void updateLegIndex(int index, String gameId){
        Completable.fromAction(() -> gameDao.updateLegIndex(index, gameId)).subscribeOn(Schedulers.io()).subscribe();
    }

    public void updateSetIndex(int index, String gameId){
        Completable.fromAction(() -> gameDao.updateSetIndex(index, gameId)).subscribeOn(Schedulers.io()).subscribe();
    }

    public Completable insertLegSetWinner(MatchLegsSets matchLegsSets){
        return Completable.fromAction(() -> gameDao.insertLegSetWinner(matchLegsSets));
    }
    public void deleteLegSetWinner(MatchLegsSets matchLegsSets){
        Completable.fromAction(() -> gameDao.deleteLegSetWinner(matchLegsSets)).subscribeOn(Schedulers.io()).subscribe();
    }

    public Maybe<Integer> getCurrentLegsSets(int userId, MatchLegsSets.Type type, String gameId){
        return gameDao.getCountLegSetWon(userId, type, gameId);
    }
}
