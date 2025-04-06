package com.example.dartscoreboard.match.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.match.data.models.Game;
import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchSettings;
import com.example.dartscoreboard.match.data.models.MatchType;
import com.example.dartscoreboard.match.data.models.MatchUsers;
import com.example.dartscoreboard.match.data.repository.GameRepository;
import com.example.dartscoreboard.user.User;
import com.example.dartscoreboard.user.UserRepository;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.util.PreferencesController;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SetupGameViewModel extends AndroidViewModel {

    private String matchId;
    private UserRepository userRepository;

    private GameRepository gameRepository;
    private List<User> selectedPlayers;


    public SetupGameViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        gameRepository = new GameRepository(application);
        selectedPlayers = PreferencesController.getInstance().getPlayers();
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
        if (selectedPlayers == null) {
            selectedPlayers = new ArrayList<>();
        }
        return selectedPlayers;
    }

    public void setSelectedPlayers(List<User> selectedPlayers){
        this.selectedPlayers = selectedPlayers;
    }

    public void addUsersToMatch(){
        for (User player :
                selectedPlayers) {
            MatchUsers matchUsers = new MatchUsers(player.userID, matchId, selectedPlayers.indexOf(player));
            userRepository.addUsersToMatch(matchUsers);
        }
    }

    public void randomisePlayerOrder(){
        Collections.shuffle(selectedPlayers);
    }
}
