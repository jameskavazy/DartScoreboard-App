package com.example.dartscoreboard.LiveMatches;

import android.app.Application;
import android.util.Log;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LiveProMatchRepository {

        private LiveProMatchesDao liveProMatchesDao;
//        private LiveData<List<Match>> allLiveProMatches;
        private LiveProMatchDatabase liveProMatchDatabase;

        public LiveProMatchRepository(Application application){
            liveProMatchDatabase = LiveProMatchDatabase.getInstance(application);
            liveProMatchesDao = liveProMatchDatabase.liveMatchesDao();
//            allLiveProMatches = liveProMatchesDao.getAll();
        }

        public Completable upsertAll(List<Match> matchesList){
            Log.d("dom test", "liveProMatchRepo upsertAll");
            Completable completable = Completable.fromAction(() -> liveProMatchesDao.upsertAll(matchesList));
            completable.subscribeOn(Schedulers.io()).subscribe();
            return completable;
        }

        public Completable deleteAll(){
            Log.d("dom test", "liveProMatchRepo deleteAll");
            Completable completable = Completable.fromAction(()-> liveProMatchesDao.deleteAll());
            completable.subscribeOn(Schedulers.io()).subscribe();
            return completable;
        }

        public Single<List<Match>> getAllLiveProMatches() {
            return Single.fromCallable(()-> liveProMatchesDao.getAll()).subscribeOn(Schedulers.io());
        }



}
