package com.example.dartscoreboard.statistics.presentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.R;

public class UserStatisticsActivity extends AppCompatActivity {

    private UserStatsViewModel userStatsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(userStatsViewModel.getUser().getUsername() + " Stats");
    }
    private void setupUI(){
        setContentView(R.layout.activity_user_statistics);
        userStatsViewModel = new ViewModelProvider(this).get(UserStatsViewModel.class);
    }




}