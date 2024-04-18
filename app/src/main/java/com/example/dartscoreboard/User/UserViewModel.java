package com.example.dartscoreboard.User;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final LiveData<List<User>> usersList;

    private final UserRepository repository;

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

//    public void deleteAllUsers(){
//        repository.deleteAllUsers();
//    }

    public LiveData<List<User>> getAllUsers(){
       return usersList;
    }

    public LiveData<List<User>> getActiveUsers(){
        return repository.getActiveUsers(true);
    }


}

