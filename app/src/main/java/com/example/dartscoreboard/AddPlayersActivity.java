package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddPlayersActivity extends AppCompatActivity {

    private EditText enterNameBox;
    private Button sendNameToGameActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_players_activity);

        setupUI();

    }

    public void setupUI(){
        enterNameBox = findViewById(R.id.EditTextEnterName);
        sendNameToGameActivity = findViewById(R.id.sendPlayersToGameActivity);

        sendNameToGameActivity.setOnClickListener(v -> {
            Log.d("dom test","AddPlayerButtonClicked");
            String playerOneName = enterNameBox.getText().toString();
            Log.d("dom test",playerOneName);
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("send_name_one",playerOneName);

        });

//        enterNameBox.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                Log.d("dom test", "enter name box IME_ACTION_DONE");
//                Intent intent = new Intent(this, GameActivity.class);
//                addPlayerName(enterNameBox.getText().toString()); // doesn't work.
//
//
//
//                return true;
//            }
//            //todo cannot pass information between activities. Maybe you need an intent?
//
//            return false;
//        });

}
    public void addPlayerName(String name){
        //String username = enterNameBox.getText().toString();
        ((TextView)findViewById(R.id.gameActivityPlayerOneName)).setText(name);

    }





}