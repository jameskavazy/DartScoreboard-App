package com.example.dartscoreboard.match.presentation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.application.DartsScoreboardApplication;
import com.example.dartscoreboard.match.data.repository.MatchRepository;
import com.example.dartscoreboard.match.models.Statistics;
import com.example.dartscoreboard.match.data.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserStatsViewModel extends AndroidViewModel {

    private final int userId;
//    private Statistics _statistics;
    private final MutableLiveData<Statistics> statistics = new MutableLiveData<>();
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<String> username = new MutableLiveData<>();

    private final Set<Pair<Integer, Integer>> scoreSegments
            = new HashSet<>(Arrays.asList(
            Pair.create(0, 59), Pair.create(60, 99), Pair.create(100, 139), Pair.create(140, 179), Pair.create(180, 180)));

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
        Disposable d = matchRepository.getUserStats(userId)
                .subscribeOn(Schedulers.io())
                .subscribe(statistics::postValue);
        compositeDisposable.add(d);
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<Statistics> getStatistics() {
        return statistics;
    }


}
