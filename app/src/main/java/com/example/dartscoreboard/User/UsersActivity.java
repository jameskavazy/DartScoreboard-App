package com.example.dartscoreboard.User;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UsersActivity extends AppCompatActivity implements OnClickListener {

    private RecyclerView recyclerView;
    private recyclerAdapterUsers adapter;
    private UserViewModel userViewModel;

    private Toolbar toolbar;

    FloatingActionButton addNewUserButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Users");
    }

    private void setAdapter() {
            adapter = new recyclerAdapterUsers();
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.getAllUsers().observe(this,adapter::setUsersList);


            adapter.setOnItemClickListener(new recyclerAdapterUsers.OnItemClickListener() {
                @Override
                public void onItemClicked(User user) {
                    onCreateDialogue(user).show();
                }
            });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_new_user_button){
            Log.d("dom test","onAddPlayerButtonclick");
            openCreateUserActivity();
        }
    }

    private void openCreateUserActivity() {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    private void setupUI(){
        addNewUserButton = findViewById(R.id.add_new_user_button);
        addNewUserButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view_username_list);
        toolbar = findViewById(R.id.toolbar);
        setAdapter();
    }

    public Dialog onCreateDialogue(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete" + " " + user.getUsername() + "? Any statistics will also be deleted.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Deletes player
                        userViewModel.deleteUser(user);
                        adapter.notifyDataSetChanged();
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

    



