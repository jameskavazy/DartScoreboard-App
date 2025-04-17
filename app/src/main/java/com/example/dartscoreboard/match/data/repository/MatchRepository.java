package com.example.dartscoreboard.match.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.application.data.local.Database;
import com.example.dartscoreboard.match.data.models.LegWithVisits;
import com.example.dartscoreboard.match.data.local.MatchDao;
import com.example.dartscoreboard.match.data.models.Leg;
import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchWithUsers;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.match.data.models.Visit;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MatchRepository {
    private final MatchDao matchDao;
    private final LiveData<List<Match>> unfinishedMatches;
    Database database;
    public MatchRepository(Application application){
        database = Database.getInstance(application);
        matchDao = database.matchesDao();
        unfinishedMatches = matchDao.getUnfinishedGameHistory();
    }
    // Match and Leg Data
    public Flowable<MatchWithUsers> getMatchWithUsers(String matchId){
        return matchDao.getMatchData(matchId);
    }

    public Flowable<LegWithVisits> getLegWithVisits(String matchId){
        return matchDao.getLatestGameWithVisits(matchId);
    }

    // Matches

    public Completable insertMatch(Match match){
        return Completable.fromAction(() -> matchDao.insertMatch(match));
    }

    public Completable updateMatch(Match match){
        Completable completable = Completable.fromAction(() -> matchDao.updateMatch(match));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }

    public Completable deleteMatch(Match match) {
        Completable completable = Completable.fromAction(() -> matchDao.deleteMatch(match));
        completable.subscribeOn(Schedulers.io()).subscribe();
        return completable;
    }
    public Completable setMatchWinner(int userId, String matchId){
        return Completable.fromAction(() -> matchDao.setMatchWinner(userId, matchId));
    }
    public void deleteAllUnfinishedMatches(){
        Completable completable = Completable.fromAction(matchDao::deleteAllUnfinishedMatches);
        completable.subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<List<Match>> getUnfinishedMatches(){
        return unfinishedMatches;
    }

    // Sets

    public Completable insertSet(Set set){
        return Completable.fromAction(() -> matchDao.insertSet(set));
    }

    public Completable deleteSet(String setId){
        return Completable.fromAction(()-> matchDao.deleteSet(setId));
    }

    public Completable addSetWinner(String setId, int userId){
        return Completable.fromAction(() -> matchDao.addSetWinner(setId, userId));
    }

    public Single<Integer> getSetsWon(int userId, String matchId) {
        return matchDao.getSetsWon(userId, matchId);
    }

    public Single<String> getLatestSetId(String matchId){
        return matchDao.getLatestSetId(matchId);
    }

    // Legs

    public Completable insertLeg(Leg leg){
        return matchDao.insertLeg(leg);
    }

    public Completable deleteLegById(String gameId){
        return Completable.fromAction(()-> matchDao.deleteLegById(gameId));
    }

    public Single<Integer> getLegTotalScore(String gameId, int userId){
       return matchDao.getLegTotalScore(gameId, userId);
    }

    public Completable setLegWinner(int userId, String gameId){
        return Completable.fromAction(() -> matchDao.setLegWinner(userId, gameId));
    }


    public Single<Integer> legsWon(String setId, String matchId, int userId){
        return matchDao.legsWon(setId, matchId, userId);
    }

    public Single<String> getLatestLegId(String matchId){
        return matchDao.getLatestLegId(matchId);
    }

    public void updateTurnIndex(int index, String gameId){
        Completable.fromAction(() -> matchDao.updateTurnIndex(index, gameId)).subscribeOn(Schedulers.io()).subscribe();
    }

    // Visits

    public Completable insertVisit(Visit visit){
        return Completable.fromAction(() -> matchDao.insertVisit(visit)).subscribeOn(Schedulers.io());
    }

    public void deleteLatestVisit(){
       Completable.fromAction(matchDao::deleteLastVisit).subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<Integer> countUserVisits(String legId, int userId){
        return matchDao.countUserVisits(legId, userId);
    }

    public Single<Integer> getUserMatchWins(int userId){
        return matchDao.getUserMatchWins(userId);
    }

    public Single<Integer> getUserMatchLosses(int userId){
        return matchDao.getUserMatchLosses(userId);
    }

    public Single<Integer> getUserMatchesPlayed(int userId){
        return matchDao.getUserMatchesPlayed(userId);
    }

    public Single<Integer> getMatchWinRate(int userId){
        return matchDao.getMatchWinRate(userId);
    }

    public Single<Integer> getAvgAllMatches(int userId){
        return matchDao.getAvgAllMatches(userId);
    }

    public Single<Integer> getLegsWon(int userId){
        return matchDao.getLegsWon(userId);
    }

    public Single<Integer> getLegWinRate(int userId){
        return matchDao.getLegWinRate(userId);
    }

    public Single<Integer> getCheckoutRate(int userId) {
        return matchDao.getCheckoutRate(userId);
    }

}
