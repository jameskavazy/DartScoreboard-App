package com.example.dartscoreboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

public class UserViewModel extends AndroidViewModel {

    private LiveData<ArrayList<User>> usersList;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }


}

