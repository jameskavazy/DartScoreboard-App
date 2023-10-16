package com.example.dartscoreboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements OnClickListener {

    public ArrayList<User> usersList;
    private RecyclerView recyclerView;
    private Button addNewUserButton;
    private EditText editText;
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
       clickHandler = new recyclerAdapterUsers.ClickHandler() {
            @Override
            public void onMyButtonClicked(View view, int position) {
                setPosition(position);
                onCreateDialogue().show();
            }
        };
    }

    public void setPosition(int position){
        this.positionInAdapter = position;
    }

    public int getPositionInAdapter(){
        return positionInAdapter;
    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_new_user_button){
            Log.d("dom test","onAddPlayerButtonclick");
            onAddNewUserButtonClick(editText.getText().toString());
            editText.getText().clear();
            PrefConfig.updateSPUserList(getApplicationContext(), usersList);
        }
    }

    private void onAddNewUserButtonClick(String nameToAdd) {
        if (!nameToAdd.isEmpty()){
            usersList.add(new User(nameToAdd,false));
            adapter.notifyDataSetChanged();
       }
    }

    private void setupUI(){
        editText = findViewById(R.id.inputUserNameEditText);
        addNewUserButton = findViewById(R.id.add_new_user_button);
        addNewUserButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view_username_list);
        usersList = PrefConfig.readSPUserList(this);
        if (usersList == null) {
            usersList = new ArrayList<>();
        }
        setAdapter();
    }

    public Dialog onCreateDialogue(){
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Deletes player
                        usersList.remove(getPositionInAdapter());
                        adapter.notifyItemRemoved(getPositionInAdapter());
                        PrefConfig.updateSPUserList(getApplicationContext(), usersList);
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




        public static class StartGameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction.
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Deletes player


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
// ...

    



}