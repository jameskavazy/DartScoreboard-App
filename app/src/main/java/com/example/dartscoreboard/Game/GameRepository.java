package com.example.dartscoreboard.Game;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Db.Database;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameRepository {
    private final GameDao gameDao;
    private final LiveData<List<Match>> unfinishedGamesHistory;
    Database database;

    public GameRepository(Application application){
        database = Database.getInstance(application);
        gameDao = database.matchesDao();
        unfinishedGamesHistory = gameDao.getUnfinishedGameHistory();
    }

    public Flowable<MatchData> getMatchData(String matchId){
        return gameDao.getMatchData(matchId);
    }

    public Completable insertMatch(Match match){
        return Completable.fromAction(() -> gameDao.insertMatch(match));

    }
//    public Completable insertGame(Game game){
//        Log.d("dom test", "repo insert");
//        Completable completable = Completable.fromAction(() -> gameDao.insertGame(game));
//        completable.subscribeOn(Schedulers.io()).subscribe();
//        return completable;
//    }

    public Completable updateMatch(Match match){
        Completable completable = Completable.fromAction(() -> gameDao.updateMatch(match));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public Completable deleteMatch(Match match) {
        Completable completable = Completable.fromAction(() -> gameDao.deleteMatch(match));
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
    public LiveData<List<Match>> getUnfinishedMatches(){
        return unfinishedGamesHistory;
    }

    public Completable insertVisit(Visit visit){
        return Completable.fromAction(() -> gameDao.insertVisit(visit)).subscribeOn(Schedulers.io());
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

}
