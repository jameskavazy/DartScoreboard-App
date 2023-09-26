package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<User> usersList;
    private RecyclerView recyclerView;

    private Button addNewUserButton;

    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setupUI();
    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

//    private void setUserInfo() {
//        String username = editText.getText().toString();
//        usersList.add(new User(username));
//    }


    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.add_new_user_button){
//            setUserInfo();
//        }
    }

    private void setupUI(){
        addNewUserButton = findViewById(R.id.add_new_user_button);
        recyclerView = findViewById(R.id.recycler_view_username_list);
        usersList = new ArrayList<>();
        //setUserInfo();
        setAdapter();

    }

}