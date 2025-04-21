package com.example.dartscoreboard.match.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.application.data.local.Database;
import com.example.dartscoreboard.match.data.models.MatchUsers;
import com.example.dartscoreboard.match.data.models.User;
import com.example.dartscoreboard.match.data.local.UserDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
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

    public Completable addUsersToMatch(MatchUsers matchUsers) {
        return userDao.insertToGame(matchUsers);
    }

    public Single<String> getUsernameById(int userId){
        return userDao.getUsernameById(userId);
    }

}
