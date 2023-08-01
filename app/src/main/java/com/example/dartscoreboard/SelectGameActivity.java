package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener {

    public static String GAME_TYPE_KEY = "GAME_TYPE";

    private Button startGameBtn;

    String[] gameSelectList = {"501","301","170"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "HomeActivityonCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        Log.d("dom test", "setupUI");
        setContentView(R.layout.activity_main);
        startGameBtn = findViewById(R.id.gameStartButton);
        startGameBtn.setOnClickListener(this);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        setUpDropDownMenu();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gameStartButton){
            openGameActivity();
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
    }

    private void openFiveoGameActivity() {

        Log.d("dom test", "openFiveoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.FiveO);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void openThreeoGameActivity() {
        Log.d("dom test", "openThreeoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.ThreeO);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void openSevenoGameActivity() {
        Log.d("dom test", "openSevenoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.SevenO);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void setUpDropDownMenu(){
    adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,gameSelectList);
    autoCompleteTextView.setAdapter(adapterItems);
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




