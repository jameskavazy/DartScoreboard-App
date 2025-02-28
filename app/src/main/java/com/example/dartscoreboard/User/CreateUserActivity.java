package com.example.dartscoreboard.User;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;
import com.example.dartscoreboard.R;

import java.util.ArrayList;
import java.util.List;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private List<User> usersList = new ArrayList<>();
    private EditText enterUsernameEditText;
    private UserViewModel userViewModel;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Create New User");

    }

    private void setupUI() {
        setContentView(R.layout.activity_create_user);
        toolbar = findViewById(R.id.toolbar);
        Button cancelButton = findViewById(R.id.cancel_create_player);
        Button createPlayerButton = findViewById(R.id.create_player_button);
        cancelButton.setOnClickListener(this);
        createPlayerButton.setOnClickListener(this);
        enterUsernameEditText = findViewById(R.id.enter_name_edit_text);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, this::setUsersList);
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
            User guy = new User(nameToAdd);
            guy.isGuy = true;
            userViewModel.insertUser(guy);
        } else {
            Log.d("dom test", "onAddNewUserButtonClick user");
            userViewModel.insertUser(new User(nameToAdd));
        }

    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.cancel_create_player) {
            Log.d("dom test", "cancel button click");
            finish();
        } else if (viewId == R.id.create_player_button) {
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