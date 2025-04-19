package com.example.dartscoreboard.match.presentation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.match.data.repository.MatchRepository;
import com.example.dartscoreboard.match.data.models.Match;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class MatchHistoryViewModel extends AndroidViewModel {

    private final MatchRepository repository;

    private final LiveData<List<Match>> unfinishedMatches;

    public MatchHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchRepository(application);
        unfinishedMatches = repository.getUnfinishedMatches();
    }

    public Completable insertMatch(Match match) {
        Log.d("dom test", "MHVM insert");
        return repository.insertMatch(match);
    }

    public void update(Match match) {
        repository.updateMatch(match);
    }

    public void delete(Match match) {
        repository.deleteMatch(match);
    }

    public void deleteAllUnfinishedMatches() {
        repository.deleteAllUnfinishedMatches();
    }

    public LiveData<List<Match>> getUnfinishedMatches() {
        return unfinishedMatches;
    }

}
