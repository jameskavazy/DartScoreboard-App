package com.example.dartscoreboard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements OnClickListener {

    public ArrayList<User> usersList;
    private RecyclerView recyclerView;
    private FloatingActionButton addNewUserButton;
    private recyclerAdapterUsers adapter;
    private recyclerAdapterUsers.ClickHandler clickHandler;
    private int positionInAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setupUI();
    }

        private void setAdapter() {
            setOnClickListener();
            adapter = new recyclerAdapterUsers(usersList, clickHandler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
       clickHandler = (view, position) -> {
           setPositionInAdapter(position);
           onCreateDialogue().show();
       };
    }

    public void setPositionInAdapter(int position){
        this.positionInAdapter = position;
    }
    public int getPositionInAdapter(){
        return positionInAdapter;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_new_user_button){
            Log.d("dom test","onAddPlayerButtonclick");
            finish();
            openCreateUserActivity();
        }
    }

    private void openCreateUserActivity() {
        Intent intent = new Intent(this, CreateUserInfo.class);
        startActivity(intent);
    }

    private void onAddNewUserButtonClick(String nameToAdd) {
        if (!nameToAdd.isEmpty()){
            usersList.add(new User(nameToAdd,false));
            adapter.notifyDataSetChanged();
       }
    }

    private void setupUI(){
        addNewUserButton = findViewById(R.id.add_new_user_button);
        addNewUserButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view_username_list);
        usersList = PreferencesController.readSPUserList(this);
        if (usersList == null) {
            usersList = new ArrayList<>();
        }
        setAdapter();
        adapter.notifyDataSetChanged();
    }

    public Dialog onCreateDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete" + " " + usersList.get(getPositionInAdapter()).getUsername() + "? Any statistics will also be deleted.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Deletes player
                        usersList.remove(getPositionInAdapter());
                        adapter.notifyItemRemoved(getPositionInAdapter());
                        PreferencesController.updateSPUserList(getApplicationContext(), usersList);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                    }
                });
        // Create the AlertDialog object and return it.
        return builder.create();
    }
}

    



