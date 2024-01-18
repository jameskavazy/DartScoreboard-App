package com.example.dartscoreboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

public class UserViewModel extends AndroidViewModel {

    private LiveData<ArrayList<User>> usersList;

    private UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        usersList = repository.getAllUsers();
    }

    public void insertUser(User user){
        repository.insertUser(user);
    }

    public void updateUser(User user){
        repository.updateUser(user);
    }

    public void deleteUser(User user){
        repository.deleteUser(user);
    }

    public void deleteAllUsers(){
        repository.deleteAllUsers();
    }

    public LiveData<ArrayList<User>> getAllUsers(){
       return repository.getAllUsers();
    }

}

