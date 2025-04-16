package com.example.dartscoreboard.match.presentation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;


import com.example.dartscoreboard.R;
import com.example.dartscoreboard.match.models.Statistics;

public class UserStatisticsActivity extends AppCompatActivity {



    private UserStatsViewModel userStatsViewModel;

    private TextView winsCountTextView;

    private TextView lossCountTextView;

    private TextView winRateTextView;

    private TextView matchesPlayedTextView;

    private TextView averageScoreTextView;

    private TextView legsWonTextView;
    private TextView legWinRateTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI(){
        setContentView(R.layout.activity_user_statistics);
        winsCountTextView = findViewById(R.id.wins_count);
        lossCountTextView = findViewById(R.id.losses_count);
        matchesPlayedTextView = findViewById(R.id.matches_played);
        winRateTextView = findViewById(R.id.win_rate);
        averageScoreTextView = findViewById(R.id.avg_score_text_view);
        legsWonTextView = findViewById(R.id.legs_won_tv);
        legWinRateTextView = findViewById(R.id.leg_win_rate);
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
        winRateTextView.setText(statistics.getMatchWinRate()+"%");
        averageScoreTextView.setText(String.valueOf(statistics.getAverageScore()));
        legsWonTextView.setText(String.valueOf(statistics.getLegsWon()));
        legWinRateTextView.setText(statistics.getLegWinRate()+"%");
    }


}