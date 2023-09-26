package com.example.dartscoreboard;

import static java.lang.String.valueOf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener {

    public static String GAME_TYPE_KEY = "GAME_TYPE";

    private Button startGameBtn;
    String[] gameSelectList = {"501","301","170"};
    boolean[] selectedPlayers;
    ArrayList<Integer> playerList = new ArrayList<>();
    String[] listOfPlayers = {"Dom","James","Guy","Nick"};

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView playerListCheckBox;
    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "HomeActivityonCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        Log.d("dom test", "setupUI");
        setContentView(R.layout.select_game_activity);
        startGameBtn = findViewById(R.id.gameStartButton);
        playerListCheckBox = findViewById(R.id.NameDropDownBox);
        startGameBtn.setOnClickListener(this);
        autoCompleteTextView = findViewById(R.id.gameTypeDropDownBox);
        setUpGameTypeDropDownMenu();
        selectedPlayers = new boolean[listOfPlayers.length];
        playerListCheckBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gameStartButton){
            openGameActivity();
        }
        if (v.getId() == R.id.NameDropDownBox){
            alertDialogueLaunch();
        }
    }

    public void openGameActivity(){
        if (autoCompleteTextView.getText().toString().equals("501")){
            Log.d("dom test","openFiveoGameActivity");
            openFiveoGameActivity();
        }
        if (autoCompleteTextView.getText().toString().equals("301")){
            Log.d("dom test","openThreeoGameActivity");
            openThreeoGameActivity();
        }
        if (autoCompleteTextView.getText().toString().equals("170")){
            Log.d("dom test","openSevenoGameActivity");
            openSevenoGameActivity();
        }
        //else Toast.makeText(this,"You must select a game type",Toast.LENGTH_SHORT).show();
    }

    private void openFiveoGameActivity() {
        String playersFromTextView = playerListCheckBox.getText().toString();
        String[] listOfPlayersFromTextView = playersFromTextView.split(",");
        Log.d("dom test", "openFiveoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.FiveO);
        arguments.putString("PLAYER_NAME",listOfPlayersFromTextView[0]);
        arguments.putString("PLAYER_NAME_2",listOfPlayersFromTextView[1]);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void openThreeoGameActivity() {
        String playersFromTextView = playerListCheckBox.getText().toString();
        String[] listOfPlayersFromTextView = playersFromTextView.split(",");
        Log.d("dom test", "openThreeoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.ThreeO);
        arguments.putString("PLAYER_NAME",listOfPlayersFromTextView[0]);
        arguments.putString("PLAYER_NAME_2",listOfPlayersFromTextView[1]);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void openSevenoGameActivity() {
        String playersFromTextView = playerListCheckBox.getText().toString();
        String[] listOfPlayersFromTextView = playersFromTextView.split(",");
        Log.d("dom test", "openSevenoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.SevenO);
        arguments.putString("PLAYER_NAME",listOfPlayersFromTextView[0]);
        arguments.putString("PLAYER_NAME_2",listOfPlayersFromTextView[1]);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void setUpGameTypeDropDownMenu(){
    adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,gameSelectList);
    autoCompleteTextView.setAdapter(adapterItems);
    }


    private void alertDialogueLaunch(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectGameActivity.this);
        builder.setTitle("Select Players");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(listOfPlayers, selectedPlayers, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    playerList.add(which);
                    Collections.sort(playerList);
                    //todo add some logic to create an arraylist, then update playername text views. MAYBE use the playerList integer arraylist for this?

                }
                else playerList.remove(Integer.valueOf(which));
            }


        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < playerList.size(); j++){
                    stringBuilder.append(listOfPlayers[playerList.get(j)]);
                    if (j != playerList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                playerListCheckBox.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int j = 0; j > selectedPlayers.length; j++){
                    selectedPlayers[j] = false;
                    playerList.clear();
                    playerListCheckBox.setText(""); //todo change the XML so that it's a normal textview, then clear all will work as intended.

                }
            }
        });
        builder.show();
    }

    private void getPlayersFromTextView(){


    }




    enum GameType {
        FiveO("501", 501),
        ThreeO("301", 301),
        SevenO("170", 170);

        String name;
        int startingScore;

        GameType(String name, int startingScore) {
            this.name = name;
            this.startingScore = startingScore;
        }
    }
}




