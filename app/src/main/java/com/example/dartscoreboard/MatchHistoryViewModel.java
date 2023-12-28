package com.example.dartscoreboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MatchHistoryViewModel extends AndroidViewModel {

    private MatchHistoryRepository repository;
    private LiveData<List<GameState>> allGames;

    public MatchHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchHistoryRepository(application);
        allGames = repository.getAllMatchHistory();
    }

    public void upsert(GameState gameState){
        repository.upsert(gameState);
    }

    public void delete(GameState gameState){
        repository.delete(gameState);
    }

    public void deleteAllMatches(){
        repository.deleteAll();
    }

    public LiveData<List<GameState>> getAllGames(){
       return repository.getAllMatchHistory();
    }


}
