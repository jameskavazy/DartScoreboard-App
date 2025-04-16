package com.example.dartscoreboard.match.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.application.DartsScoreboardApplication;
import com.example.dartscoreboard.match.data.repository.MatchRepository;
import com.example.dartscoreboard.match.models.Statistics;
import com.example.dartscoreboard.user.UserRepository;

import java.util.function.Consumer;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserStatsViewModel extends AndroidViewModel {

    private final int userId;
    private final Statistics _statistics = new Statistics();
    private final MutableLiveData<Statistics> statistics = new MutableLiveData<>();
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<String> username = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public UserStatsViewModel(@NonNull Application application, int userId) {
        super(application);
        userRepository = new UserRepository(application);
        matchRepository = new MatchRepository(application);
        this.userId = userId;
        usernameFromDb();
        getAllData();
    }

    static class StatsViewModelFactory implements ViewModelProvider.Factory {
        private final int arg;

        public StatsViewModelFactory(int arg){
            this.arg = arg;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UserStatsViewModel.class)) {
                return (T) new UserStatsViewModel(DartsScoreboardApplication.getInstance(), arg);
            }
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }

    public void usernameFromDb(){
        Disposable d = userRepository.getUsernameById(userId)
                .subscribeOn(Schedulers.io())
                .subscribe(username::postValue);
        compositeDisposable.add(d);
    }

    public void getAllData(){
        queryAndUpdateStats(matchRepository.getUserMatchWins(userId), _statistics::setWins);
        queryAndUpdateStats(matchRepository.getUserMatchLosses(userId), _statistics::setLosses);
        queryAndUpdateStats(matchRepository.getUserMatchesPlayed(userId), _statistics::setMatchesPlayed);
        queryAndUpdateStats(matchRepository.getMatchWinRate(userId), _statistics::setMatchWinRate);
        queryAndUpdateStats(matchRepository.getAvgAllMatches(userId), _statistics::setAverageScore);
        queryAndUpdateStats(matchRepository.getLegsWon(userId), _statistics::setLegsWon);
        queryAndUpdateStats(matchRepository.getLegWinRate(userId), _statistics::setLegWinRate);
    }



    public void queryAndUpdateStats(Single<Integer> query, Consumer<Integer> updater){
        Disposable d = query
                .doOnSuccess(value -> {
                    updater.accept(value);
                    statistics.postValue(_statistics);
                })
                .doOnError(throwable -> {
                    updater.accept(0);
                    throwable.printStackTrace();
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(d);
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<Statistics> getStatistics() {
        return statistics;
    }


}
