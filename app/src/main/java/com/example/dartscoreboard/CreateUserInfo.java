package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dartscoreboard.models.GuyUser;
import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

public class CreateUserInfo extends AppCompatActivity implements View.OnClickListener {

    private Button cancelButton;
    private Button createPlayerButton;
    private EditText enterUsernameEditText;

    private ArrayList<User> usersList;

    private ArrayList<User> testLit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_info);
        setupUI();
    }


    private void setupUI() {
        cancelButton = findViewById(R.id.cancel_create_player);
        createPlayerButton = findViewById(R.id.create_player_button);
        cancelButton.setOnClickListener(this);
        createPlayerButton.setOnClickListener(this);
        enterUsernameEditText = findViewById(R.id.enter_name_edit_text);
        usersList = PreferencesController.readSPUserList(this);
        if (usersList == null){
            usersList = new ArrayList<>();
        }
    }

    private void onAddNewUserButtonClick(String nameToAdd) { // todo protect against duplicate players
        if (nameToAdd.isEmpty()) return;

        if (nameToAdd.equalsIgnoreCase("guy")) {
            Log.d("dom test", "onAddNewUserButtonClick GuyUser");
            usersList.add(new GuyUser(nameToAdd, false));
        } else {
            Log.d("dom test", "onAddNewUserButtonClick user");
            usersList.add(new User(nameToAdd, false));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_create_player) {
            Log.d("dom test", "cancel button click");
            finish();
            openUsersActivity();
        } else if (v.getId() == R.id.create_player_button) {
            Log.d("dom test", "OK button click");
            String usernameToAdd = enterUsernameEditText.getText().toString();
            onAddNewUserButtonClick(usernameToAdd);
            enterUsernameEditText.getText().clear();
            PreferencesController.updateSPUserList(getApplicationContext(), usersList);
            openUsersActivity();
        }
    }

    private void openUsersActivity() {
        Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent);
        finish();
    }
}