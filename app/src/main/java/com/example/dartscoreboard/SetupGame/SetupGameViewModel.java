package com.example.dartscoreboard.SetupGame;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.match.Game;
import com.example.dartscoreboard.match.GameViewModel;
import com.example.dartscoreboard.match.Match;
import com.example.dartscoreboard.match.MatchSettings;
import com.example.dartscoreboard.match.MatchType;
import com.example.dartscoreboard.match.MatchUsers;
import com.example.dartscoreboard.match.GameRepository;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;
import com.example.dartscoreboard.match.Set;
import com.example.dartscoreboard.util.PreferencesController;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SetupGameViewModel extends AndroidViewModel {

    private String matchId;
    private UserRepository userRepository;

    private GameRepository gameRepository;
    private List<User> playersToGame;


    public SetupGameViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        gameRepository = new GameRepository(application);
        playersToGame = PreferencesController.getInstance().getPlayers();
    }


    public MatchSettings getMatchSettings(int legs, int sets) {
        return new MatchSettings(legs, sets);
    }

    public Match createMatch(MatchType matchType, int legs, int sets) {
        matchId = UUID.randomUUID().toString();
        Match match = new Match(matchId, matchType, getMatchSettings(legs, sets), OffsetDateTime.now());
        match.setPlayersCSV(getSelectedPlayers());
        addUsersToMatch();
        gameRepository.insertMatch(match).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                String setId = UUID.randomUUID().toString();
                GameViewModel.currentSetNumber = 0;
                gameRepository.insertSet(new Set(setId, match.matchId, 0)).subscribeOn(Schedulers.io()).subscribe();
                gameRepository.insertGame(new Game(UUID.randomUUID().toString(), setId, match.matchId, 0,0,0)).subscribeOn(Schedulers.io()).subscribe();
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });

        return match;
    }

    public List<User> getSelectedPlayers() {
        if (playersToGame == null) {
            playersToGame = new ArrayList<>();
        }
        return playersToGame;
    }

    public void addUsersToMatch(){
        for (User player :
                playersToGame) {
            MatchUsers matchUsers = new MatchUsers(player.userID, matchId, playersToGame.indexOf(player));
            userRepository.addUsersToMatch(matchUsers);
        }
    }
}
