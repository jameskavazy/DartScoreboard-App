package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.dartscoreboard.models.User;

public class UserStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI(){
        setContentView(R.layout.activity_user_statistics);
        Bundle arguments = getIntent().getExtras();
        User user = (User) arguments.getSerializable(StatisticsActivity.userStatKey);
        TextView textView = findViewById(R.id.testView);
        textView.setText(user.getUsername());
    }


}