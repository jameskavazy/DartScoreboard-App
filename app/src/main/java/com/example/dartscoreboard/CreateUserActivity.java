package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.models.GuyUser;
import com.example.dartscoreboard.models.User;

import java.util.ArrayList;
import java.util.List;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private List<User> usersList = new ArrayList<>();

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
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                setUsersList(users);
            }
        });
    }

    private void onAddNewUserButtonClick(String nameToAdd) {
        if (nameToAdd.isEmpty()) return;

        for (User user : getUsersList()) {
            if (user.getUsername().equalsIgnoreCase(nameToAdd)) {
                Toast.makeText(DartsScoreboardApplication.getContext(), "User already with that name. Please enter a unique name", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (nameToAdd.equalsIgnoreCase("guy")) {
            Log.d("dom test", "onAddNewUserButtonClick GuyUser");
            User guy = new User(nameToAdd, false);
            guy.isGuy = true;
            userViewModel.insertUser(guy);
        } else {
            Log.d("dom test", "onAddNewUserButtonClick user");
            userViewModel.insertUser(new User(nameToAdd, false));
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

    private void setUsersList(List<User> usersList){
        this.usersList = usersList;
    }

    private List<User> getUsersList(){
        return usersList;
    }
}