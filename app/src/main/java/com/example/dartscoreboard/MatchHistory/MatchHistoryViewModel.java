package com.example.dartscoreboard.MatchHistory;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Game.GameState;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class MatchHistoryViewModel extends AndroidViewModel {

    private final MatchHistoryRepository repository;
    private final LiveData<List<GameState>> allGames;

    public MatchHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchHistoryRepository(application);
        allGames = repository.getAllMatchHistory();
    }

    public Single<Long> insert(GameState gameState) {
        Log.d("dom test", "MHVM insert");
        return repository.insert(gameState);
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
        return allGames;
    }

}
