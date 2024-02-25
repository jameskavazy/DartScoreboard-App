package com.example.dartscoreboard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.models.User;

public class UserStatisticsActivity extends AppCompatActivity {

    private UserStatsViewModel userStatsViewModel;

    private Toolbar toolbar;

    private TextView winsTextView;
    private TextView lossesTextView;

    private TextView winRateTextView;
    private TextView checkoutRateTextView;

//    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(userStatsViewModel.getUser().getUsername() + " Stats");
    }
    private void setupUI(){
        setContentView(R.layout.activity_user_statistics);
        userStatsViewModel = new ViewModelProvider(this).get(UserStatsViewModel.class);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            userStatsViewModel.setUser((User) arguments.getSerializable(StatisticsActivity.userStatKey));
            winsTextView  = findViewById(R.id.winsCount);
            lossesTextView = findViewById(R.id.lossesCount);
            winRateTextView = findViewById(R.id.winRateCount);
            checkoutRateTextView = findViewById(R.id.checkoutRate);
            winsTextView.setText(String.valueOf(userStatsViewModel.getUser().getWins()));
            lossesTextView.setText(String.valueOf(userStatsViewModel.getUser().getLosses()));
            winRateTextView.setText(userStatsViewModel.getUser().getWinRate() + "%");
            checkoutRateTextView.setText(userStatsViewModel.getCheckoutRate() + "%");
        }
    }


}