package com.example.dartscoreboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MatchHistoryViewModel extends AndroidViewModel {

    private MatchHistoryRepository repository;
    private LiveData<List<GameState>> allGames;

    private long id;

    public MatchHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchHistoryRepository(application);
        allGames = repository.getAllMatchHistory();
    }

    public void insert(GameState gameState) {
       id = repository.insert(gameState);
    }

    public void update(GameState gameState) {
        repository.update(gameState);
    }

    public void delete(GameState gameState) {
        repository.delete(gameState);
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


}
