package com.example.dartscoreboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class UsersActivity extends AppCompatActivity implements OnClickListener{

    public ArrayList<User> usersList;
    private RecyclerView recyclerView;

    private Button addNewUserButton;

    private EditText editText;

    private recyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setupUI();
    }

    private void setAdapter() {
        adapter = new recyclerAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_new_user_button){
            Log.d("dom test","onAddPlayerButtonclick");
            onAddNewUserButtonClick(editText.getText().toString());
            editText.getText().clear();
            PrefConfig.writeListInPref(getApplicationContext(), usersList);


        }
    }

    private void onAddNewUserButtonClick(String nameToAdd) {
        if (!nameToAdd.isEmpty()){
            usersList.add(new User(nameToAdd));
            adapter.notifyDataSetChanged();
        }
    }



    private void setupUI(){
        editText = findViewById(R.id.inputUserNameEditText);
        addNewUserButton = findViewById(R.id.add_new_user_button);
        addNewUserButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view_username_list);
        usersList = PrefConfig.readListFromPref(this);
        if (usersList == null) {
            usersList = new ArrayList<>();
        }

        setAdapter();
    }




//    private String[] getUsers(){
//        UsersActivity usersActivity = new UsersActivity();
//        usersActivity.usersList = new ArrayList<>();
//        String [] playersList = new String[usersActivity.usersList.size()];
//
//        for(int i = 0; i < usersActivity.usersList.size(); i++){
//            playersList[i] = String.valueOf(usersActivity.usersList.get(i));
//
//        }
//
//        return playersList;
//
//        // todo pass this using intents to the home activity and then to the select game activity - startActivityForResult() will do with with back button
//
//    }

}