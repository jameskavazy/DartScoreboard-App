package com.example.dartscoreboard.Statistics;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.User.User;

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
        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            userStatsViewModel.setUser((User) arguments.getSerializable(StatisticsActivity.userStatKey));
            TextView winsTextView = findViewById(R.id.winsCount);
            TextView lossesTextView = findViewById(R.id.lossesCount);
            TextView winRateTextView = findViewById(R.id.winRateCount);
            TextView checkoutRateTextView = findViewById(R.id.checkoutRate);
            TextView avgScoreTextView = findViewById(R.id.avg_score_text_view);
            winsTextView.setText(String.valueOf(userStatsViewModel.getUser().getWins()));
            lossesTextView.setText(String.valueOf(userStatsViewModel.getUser().getLosses()));
            avgScoreTextView.setText(String.valueOf(userStatsViewModel.getUser().getLifeTimeAvgScore()));
            String winRate = userStatsViewModel.getUser().getWinRate() + "%";
            String checkoutRate = userStatsViewModel.getCheckoutRate() + "%";
            winRateTextView.setText(winRate);
            checkoutRateTextView.setText(checkoutRate);
        }
    }




}