package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
            onThreeoBtnClicked();
        } else if (v.getId() == R.id.sevenoBtn) {
            Log.d("dom test", "sevenBtn click");
            onSevenOBtnClicked();
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
       openFiveoGameActivity();
   }

   private void onThreeoBtnClicked() {
        Log.d("dom test", "onThreeoBtnClicked");
        openThreeoGameActivity();
    }

   private void onSevenOBtnClicked() {
        Log.d("dom test", "onSevenOBtnClicked");
        openSevenoGameActivity();
   }

   private void onAddPlayersBtnClicked(){
        Log.d("dom test","onAddPlayersBtnClicked");

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

//    private void openAddPlayersActivity() {
//        Log.d("dom test", "openAddPlayersActivity");
//        Intent intent = new Intent(this, AddPlayersActivity.class);
//        startActivity(intent);
//    }





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