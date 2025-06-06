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

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SetupMatchViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    private final MatchRepository matchRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SetupMatchViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        matchRepository = new MatchRepository(application);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public MatchSettings getMatchSettings(int legs, int sets) {
        return new MatchSettings(legs, sets);
    }

    public String createMatch(MatchType matchType, int legs, int sets) {
        Match match = new Match(UUID.randomUUID().toString(), matchType, getMatchSettings(legs, sets), OffsetDateTime.now());
        match.setPlayersCSV(PreferencesController.getInstance().getPlayers());
        String setId = UUID.randomUUID().toString();

        Disposable d = matchRepository.insertMatch(match)
                .andThen(addUsersToMatch(match.matchId))
                .andThen(matchRepository.insertSet(new Set(setId, match.matchId)))
                .andThen(matchRepository.insertLeg(new Leg(UUID.randomUUID().toString(), setId, match.matchId, 0)))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d("InsertDebug", "Match data inserted successfully"),
                        Throwable::printStackTrace
                );
        compositeDisposable.add(d);
        return match.matchId;
    }

    public Completable addUsersToMatch(String matchId){
        List<User> selectedPlayers = PreferencesController.getInstance().getPlayers();
        if (selectedPlayers == null || selectedPlayers.isEmpty()) {
            return Completable.complete();
        }

        List<Completable> insertions = new ArrayList<>();

        for (User player : selectedPlayers) {
            MatchUsers matchUsers = new MatchUsers(player.userId, matchId, selectedPlayers.indexOf(player));
            insertions.add(Completable.fromAction(() -> userRepository.addUsersToMatch(matchUsers)));
        }
        return Completable.merge(insertions);
    }

    public void randomisePlayerOrder(List<User> selectedPlayers){
        if (selectedPlayers != null) {
            Collections.shuffle(selectedPlayers);
        }
    }
}
