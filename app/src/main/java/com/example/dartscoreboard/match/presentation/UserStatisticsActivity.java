package com.example.dartscoreboard.match.presentation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;


import com.example.dartscoreboard.R;
import com.example.dartscoreboard.match.models.Statistics;

import java.util.Locale;

public class UserStatisticsActivity extends AppCompatActivity {



    private UserStatsViewModel userStatsViewModel;

    private TextView winsCountTextView;

    private TextView lossCountTextView;

    private TextView winRateTextView;

    private TextView matchesPlayedTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI(){
        setContentView(R.layout.activity_user_statistics);
        winsCountTextView = findViewById(R.id.winsCount);
        lossCountTextView = findViewById(R.id.lossesCount);
        matchesPlayedTextView = findViewById(R.id.matches_played);
        winRateTextView = findViewById(R.id.win_rate);
        int userId = getIntent().getIntExtra(StatisticsActivity.userStatKey, 0);
        userStatsViewModel = new ViewModelProvider(this,
                new UserStatsViewModel.StatsViewModelFactory(userId)).get(UserStatsViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        userStatsViewModel.getUsername().observe(this,
                username -> toolbar.setTitle(username + " Stats"));

        userStatsViewModel.getStatistics().observe(this, this::setTextFields);

    }

    private void setTextFields(Statistics statistics) {
        winsCountTextView.setText(String.valueOf(statistics.getWins()));
        lossCountTextView.setText(String.valueOf(statistics.getLosses()));
        matchesPlayedTextView.setText(String.valueOf(statistics.getMatchesPlayed()));
        winRateTextView.setText(statistics.getWinRate()+"%");


    }


}