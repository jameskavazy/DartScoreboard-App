package com.example.dartscoreboard.MatchHistory;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Game.GameRepository;
import com.example.dartscoreboard.Game.Game;
import com.example.dartscoreboard.User.UserRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class MatchHistoryViewModel extends AndroidViewModel {

    private final GameRepository repository;

    private UserRepository userRepository;
    private final LiveData<List<Game>> unfinishedGames;

    public MatchHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new GameRepository(application);
        userRepository = new UserRepository(application);
        unfinishedGames = repository.getUnfinishedGamesHistory();
    }

    public Completable insert(Game game) {
        Log.d("dom test", "MHVM insert");
        return repository.insert(game);
    }

    public void update(Game game) {
        repository.update(game);
    }

    public void delete(Game game) {
        repository.delete(game);
    }

    public void deleteAllMatches() {
        repository.deleteAll();
    }

    public LiveData<List<Game>> getUnfinishedGames() {
        return unfinishedGames;
    }

//    public LiveData<GameWithUsers> getPlayersInGame(String gameId){
//        return userRepository.getUserFromGame(gameId);
//    }

}
