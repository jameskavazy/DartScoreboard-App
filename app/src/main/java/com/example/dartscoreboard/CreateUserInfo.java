package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateUserInfo extends AppCompatActivity implements View.OnClickListener {

    private Button cancelButton;
    private Button createPlayerButton;
    private EditText enterUsernameEditText;

    private ArrayList<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_info);
        setupUI();
    }


    private void setupUI(){
        cancelButton = findViewById(R.id.cancel_create_player);
        createPlayerButton = findViewById(R.id.create_player_button);
        cancelButton.setOnClickListener(this);
        createPlayerButton.setOnClickListener(this);
        enterUsernameEditText = findViewById(R.id.enter_name_edit_text);
        usersList = PrefConfig.readSPUserList(this);
        if (usersList == null){
            usersList = new ArrayList<>();
        }
    }

    private void onAddNewUserButtonClick(String nameToAdd) {
        if (!nameToAdd.isEmpty()){
            usersList.add(new User(nameToAdd,false));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_create_player){
            Log.d("dom test","cancel button click");
            finish();
            openUsersActivity();
        }
        if (v.getId() == R.id.create_player_button){
            Log.d("dom test","OK button click");
            String usernameToAdd = enterUsernameEditText.getText().toString();
            onAddNewUserButtonClick(usernameToAdd);
            enterUsernameEditText.getText().clear();
            PrefConfig.updateSPUserList(getApplicationContext(), usersList);
            openUsersActivity();
        }
    }

    private void openUsersActivity(){
        Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent);
        finish();
        //todo let's control the backstack - circular activities.
    }
}