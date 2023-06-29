package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static String GAME_TYPE_KEY = "GAME_TYPE";

    private Button fiveoBtn;
    private Button threeoBtn;
    private Button sevenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "HomeActivityonCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fiveoBtn) {
            Log.d("dom test", "fiveoBtn click");
            onFiveoBtnClicked();
        } else if (v.getId() == R.id.threeoBtn) {
            Log.d("dom test", "threeoBtn click");
        } else if (v.getId() == R.id.sevenoBtn) {
            Log.d("dom test", "sevenBtn click");
        }
    }

    private void setupUI() {
        Log.d("dom test", "setupUI");
        setContentView(R.layout.activity_main);
        fiveoBtn = findViewById(R.id.fiveoBtn);
        threeoBtn = findViewById(R.id.threeoBtn);
        sevenBtn = findViewById(R.id.sevenoBtn);
        fiveoBtn.setOnClickListener(this);
        threeoBtn.setOnClickListener(this);
        sevenBtn.setOnClickListener(this);
    }

   private void onFiveoBtnClicked() {
       Log.d("dom test", "onFiveoBtnClicked");
       openGameActivity();
   }

    private void openGameActivity() {
        Log.d("dom test", "openGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.FiveO);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    enum GameType {
        FiveO("FiveO", 501),
        ThreeO("ThreeO", 301),
        SevenO("SevenO", 170);

        String name;
        int startingScore;

        GameType(String name, int startingScore) {
            this.name = name;
            this.startingScore = startingScore;
        }
    }
}










  /*      public static void main (String[]args){

// players go here...


            Player user1 = new Player("user1", 0, 0);
            System.out.println(user1.name);
            user1.getPlayerScore();
            Player user2 = new Player("user2", 0, 0);
            System.out.println(user2.name);
            user2.playerScore = user1.playerScore;


//Match Starts
            System.out.println("Game Start");
            System.out.println();
            while (user1.playerScore != 0 && user2.playerScore != 0) {


                System.out.println(user1.turn());
                if (user1.playerScore == 0) {
                    System.out.println(user1.name + " Wins!");
                    break;
                }
                System.out.println(user2.turn());
                if (user2.playerScore == 0) {
                    System.out.println(user2.name + " Wins!");
                }


            }


        }


    }*/