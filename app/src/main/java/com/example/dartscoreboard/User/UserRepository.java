package com.example.dartscoreboard.User;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Db.Database;
import com.example.dartscoreboard.match.MatchUsers;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {

    private final UserDao userDao;

    private final LiveData<List<User>> allUsers;

    public UserRepository(Application application){
        Database db = Database.getInstance(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    public void insertUser(User user){
        Completable.fromAction(()-> userDao.insertUser(user)).subscribeOn(Schedulers.io()).subscribe();
    }

    public void updateUser(User user){
        Completable.fromAction(()-> userDao.updateUser(user)).subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteUser(User user){
        Completable.fromAction(()-> userDao.deleteUser(user)).subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteAllUsers(){
        Completable.fromAction(userDao::deleteAllUsers).subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }

    public void addUsersToMatch(MatchUsers matchUsers) {
        Completable.fromAction(() -> userDao.insertToGame(matchUsers)).subscribeOn(Schedulers.io()).subscribe();
    }

}
