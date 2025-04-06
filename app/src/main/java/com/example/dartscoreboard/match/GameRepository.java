package com.example.dartscoreboard.match;

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

    public Flowable<GameWithVisits> getGameWithVisits(String matchId){
        return gameDao.getLatestGameWithVisits(matchId);
    }

    public Flowable<MatchWithUsers> getMatchWithUsers(String matchId){
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

    public void deleteGameStateByID(String matchId, int winnerId){
        Completable completable = Completable.fromAction(()-> gameDao.deleteGameStateByID(matchId, winnerId));
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

    public Completable setGameWinner(int userId, String gameId, int currentSetNumber){
        return Completable.fromAction(() -> gameDao.setGameWinner(userId, gameId, currentSetNumber));
    }

    public Completable setMatchWinner(int userId, String matchId){
        return Completable.fromAction(() -> gameDao.setMatchWinner(userId, matchId));
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

    public Completable insertGame(Game game){
        return gameDao.insertGame(game);
    }

    public Single<Integer> legsWon(String setId, String matchId, int userId){
        return gameDao.legsWon(setId, matchId, userId);
    }

    public Flowable<List<Game>> getGamesInMatch(String matchId){
        return gameDao.getGamesInMatch(matchId);
    }

    public Completable addSetWinner(String setId, int userId){
        return Completable.fromAction(() -> gameDao.addSetWinner(setId, userId));
    }

    public Single<Integer> getSetsWon(int userId, String matchId) {
        return gameDao.getSetsWon(userId, matchId);
    }

    public Completable insertSet(Set set){
        return Completable.fromAction(() -> gameDao.insertSet(set));
    }

    public Flowable<List<Set>> getSetsInMatch(String matchId){
        return gameDao.getSetsInMatch(matchId);
    }

}
