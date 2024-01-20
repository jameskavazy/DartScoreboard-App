package com.example.dartscoreboard;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {

    private UserDao userDao;
    private UserDatabase userDatabase;

    private LiveData<List<User>> allUsers;

    private LiveData<List<User>> activeUsers;

    public UserRepository(Application application){
        userDatabase = UserDatabase.getInstance(application);
        userDao = userDatabase.userDao();
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
        Completable.fromAction(()-> userDao.deleteAllUsers()).subscribeOn(Schedulers.io()).subscribe();
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }

    public LiveData<List<User>> getActiveUsers(boolean active){
        return userDao.getActiveUsers(active);
    }



}
