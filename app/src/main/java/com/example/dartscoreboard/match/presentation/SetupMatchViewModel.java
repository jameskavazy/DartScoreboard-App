package com.example.dartscoreboard.match.presentation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.match.data.models.Leg;
import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchSettings;
import com.example.dartscoreboard.match.data.models.MatchType;
import com.example.dartscoreboard.match.data.models.MatchUsers;
import com.example.dartscoreboard.match.data.repository.MatchRepository;
import com.example.dartscoreboard.match.data.models.User;
import com.example.dartscoreboard.match.data.repository.UserRepository;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.util.PreferencesController;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SetupMatchViewModel extends AndroidViewModel {

    private String matchId;
    private final UserRepository userRepository;

    private final MatchRepository matchRepository;
//    private List<User> selectedPlayers;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SetupMatchViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        matchRepository = new MatchRepository(application);
//        selectedPlayers = PreferencesController.getInstance().getPlayers();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public MatchSettings getMatchSettings(int legs, int sets) {
        return new MatchSettings(legs, sets);
    }

    public Match createMatch(MatchType matchType, int legs, int sets) {
        matchId = UUID.randomUUID().toString();
        Match match = new Match(matchId, matchType, getMatchSettings(legs, sets), OffsetDateTime.now());
        match.setPlayersCSV(PreferencesController.getInstance().getPlayers());
        Disposable d = matchRepository.insertMatch(match).subscribeOn(Schedulers.io()).subscribe(() -> {
                String setId = UUID.randomUUID().toString();
                addUsersToMatch();
                matchRepository.insertSet(new Set(setId, match.matchId)).subscribeOn(Schedulers.io()).subscribe();
                matchRepository.insertLeg(new Leg(UUID.randomUUID().toString(), setId, match.matchId, 0)).subscribeOn(Schedulers.io()).subscribe();
            });
        compositeDisposable.add(d);
        return match;
    }

    public void addUsersToMatch(){
        List<User> selectedPlayers = PreferencesController.getInstance().getPlayers();
        if (selectedPlayers == null) return;



        for (User player :
                selectedPlayers) {
            Log.d("userlist", "addUsersToMatch: " + player.getUsername() + " " + selectedPlayers.indexOf(player));
            MatchUsers matchUsers = new MatchUsers(player.userId, matchId, selectedPlayers.indexOf(player));
            userRepository.addUsersToMatch(matchUsers);
        }
    }

    public void randomisePlayerOrder(List<User> selectedPlayers){
        Collections.shuffle(selectedPlayers);
    }


}
