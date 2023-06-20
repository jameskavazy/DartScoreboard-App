package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
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