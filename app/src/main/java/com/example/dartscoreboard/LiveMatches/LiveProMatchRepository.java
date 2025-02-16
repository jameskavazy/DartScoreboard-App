package com.example.dartscoreboard.LiveMatches;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Db.Database;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LiveProMatchRepository {

    private final LiveProMatchesDao liveProMatchesDao;
    private final LiveData<List<Match>> allLiveProMatches;

    public LiveProMatchRepository(Application application) {
        Database db = Database.getInstance(application);
        liveProMatchesDao = db.liveProMatchesDao();
        allLiveProMatches = liveProMatchesDao.getAll();
    }

    public void upsertAll(List<Match> matchesList) {
        Log.d("dom test", "liveProMatchRepo upsertAll");
        Completable completable = Completable.fromAction(() -> liveProMatchesDao.upsertAll(matchesList));
        completable.subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteAll() {
        Log.d("dom test", "liveProMatchRepo deleteAll");
        Completable completable = Completable.fromAction(liveProMatchesDao::deleteAll);
        completable.subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<List<Match>> getAllLiveProMatches() {
        return allLiveProMatches;
    }


}
