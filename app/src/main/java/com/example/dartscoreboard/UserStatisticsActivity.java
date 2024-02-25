package com.example.dartscoreboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.example.dartscoreboard.models.User;

public class UserStatisticsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView winsTextView;
    private TextView lossesTextView;

    private TextView winRateTextView;
    private TextView checkoutRateTextView;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getUser().getUsername() + " Stats");
    }
    private void setupUI(){
        setContentView(R.layout.activity_user_statistics);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            setUser((User) arguments.getSerializable(StatisticsActivity.userStatKey));
            winsTextView  = findViewById(R.id.winsCount);
            lossesTextView = findViewById(R.id.lossesCount);
            winRateTextView = findViewById(R.id.winRateCount);
            checkoutRateTextView = findViewById(R.id.checkoutRate);
            assert user != null;
            winsTextView.setText(String.valueOf(getUser().getWins()));
            lossesTextView.setText(String.valueOf(getUser().getLosses()));
            winRateTextView.setText(getUser().getWinRate() + "%");
            checkoutRateTextView.setText(getCheckoutRate() + "%");
        }
    }
    private void setUser(User user) {
        this.user = user;
    }

    private User getUser(){
        return user;
    }

    private int getCheckoutRate(){
        return Math.round((float) getUser().getCheckoutMade() / (getUser().getCheckoutMade() + getUser().getCheckoutMissed()) * 100);
    }
}