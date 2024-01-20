package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.models.GuyUser;
import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private Button cancelButton;
    private Button createPlayerButton;
    private EditText enterUsernameEditText;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }


    private void setupUI() {
        setContentView(R.layout.activity_create_user);
        cancelButton = findViewById(R.id.cancel_create_player);
        createPlayerButton = findViewById(R.id.create_player_button);
        cancelButton.setOnClickListener(this);
        createPlayerButton.setOnClickListener(this);
        enterUsernameEditText = findViewById(R.id.enter_name_edit_text);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void onAddNewUserButtonClick(String nameToAdd) { // todo protect against duplicate players
        if (nameToAdd.isEmpty()) return;

        if (nameToAdd.equalsIgnoreCase("guy")) {
            Log.d("dom test", "onAddNewUserButtonClick GuyUser");
            userViewModel.insertUser(new GuyUser(nameToAdd,false));
        } else {
            Log.d("dom test", "onAddNewUserButtonClick user");
            userViewModel.insertUser(new User(nameToAdd,false));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_create_player) {
            Log.d("dom test", "cancel button click");
            finish();
        } else if (v.getId() == R.id.create_player_button) {
            Log.d("dom test", "OK button click");
            String usernameToAdd = enterUsernameEditText.getText().toString();
            onAddNewUserButtonClick(usernameToAdd);
            enterUsernameEditText.getText().clear();
            finish();
        }
    }
}