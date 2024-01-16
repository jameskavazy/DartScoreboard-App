package com.example.dartscoreboard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MatchHistoryViewModel extends AndroidViewModel {

    private MatchHistoryRepository repository;
    private LiveData<List<GameState>> allGames;

    private long id;

    public MatchHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchHistoryRepository(application);
        allGames = repository.getAllMatchHistory();
    }

    public Single<Long> insert(GameState gameState) {
        return repository.insert(gameState);
    }

    public void update(GameState gameState) {
        repository.update(gameState);
    }

    public void delete(GameState gameState) {
        repository.delete(gameState);
    }

    public LiveData<GameState> findGameById(int id){
        return repository.getGameStateById(id);
    }

    public void deleteAllMatches() {
        repository.deleteAll();
    }

    public LiveData<List<GameState>> getAllGames() {
        return repository.getAllMatchHistory();
    }

    public long getInsertedId(){
        return id;
    }

    public void deleteGameStateByID(long id){
       //todo make this work. Does it need to return an id?
        repository.deleteGameStateByID(id);
    }


}
