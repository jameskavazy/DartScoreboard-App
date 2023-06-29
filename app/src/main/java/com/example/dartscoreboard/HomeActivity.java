package com.example.dartscoreboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button fiveoBtn;
    Button threeoBtn;
    Button sevenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        fiveoBtn = findViewById(R.id.fiveoBtn);
        threeoBtn = findViewById(R.id.threeoBtn);
        sevenBtn = findViewById(R.id.sevenoBtn);
        fiveoBtn.setOnClickListener(this);
        threeoBtn.setOnClickListener(this);
        sevenBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fiveoBtn) {
            Log.d("dom test", "fiveoBtn click");
        } else if (v.getId() == R.id.threeoBtn) {
            Log.d("dom test", "threeoBtn click");
        } else if (v.getId() == R.id.sevenoBtn) {
            Log.d("dom test", "sevenBtn click");
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