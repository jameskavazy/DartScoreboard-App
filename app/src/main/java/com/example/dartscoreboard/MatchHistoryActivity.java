package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MatchHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button gameSlot1Button;
    private Button gameSlot2Button;
    private Button gameSlot3Button;
    private Button clearGamesButton;
    private GameState gameState1 = PreferencesController.getInstance().readGameState(GameActivity.GAME_STATE_SLOT1_KEY);
    private GameState gameState2 = PreferencesController.getInstance().readGameState(GameActivity.GAME_STATE_SLOT2_KEY);
    private GameState gameState3 = PreferencesController.getInstance().readGameState(GameActivity.GAME_STATE_SLOT3_KEY);

    private String gameInfo;
    private String gameInfo2;
    private String gameInfo3;

    private String emptySlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        setupUI();
    }

    public void setupUI(){
        gameSlot1Button = findViewById(R.id.game_slot_1_button);
        gameSlot2Button = findViewById(R.id.game_slot_2_button);
        gameSlot3Button = findViewById(R.id.game_slot_3_button);
        clearGamesButton = findViewById(R.id.clear_games_button);
        gameSlot1Button.setOnClickListener(this);
        gameSlot2Button.setOnClickListener(this);
        gameSlot3Button.setOnClickListener(this);
        clearGamesButton.setOnClickListener(this);
        setSlotVisibility();
        if (gameState1 != null) {
            gameInfo = gameState1.getGameType().name + " " + gameState1.getPlayerList();
        }
        if (gameState2 != null) {
            gameInfo2 = gameState2.getGameType().name + " " + gameState2.getPlayerList();
        }
        if (gameState3 != null) {
            gameInfo3 = gameState3.getGameType().name + " " + gameState3.getPlayerList();
        }
        emptySlot = getString(R.string.empty_slot_string);
        setSlotText();
    }



    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.game_slot_1_button){
            Bundle arguments = new Bundle();
            arguments.putString(SelectGameActivity.SLOT_KEY,GameActivity.GAME_STATE_SLOT1_KEY);
            Intent intent = new Intent(this,GameActivity.class);
            intent.putExtras(arguments);
            startActivity(intent);
        }
        if (v.getId() == R.id.game_slot_2_button){
            Bundle arguments = new Bundle();
            arguments.putString(SelectGameActivity.SLOT_KEY,GameActivity.GAME_STATE_SLOT2_KEY);
            Intent intent = new Intent(this,GameActivity.class);
            intent.putExtras(arguments);
            startActivity(intent);
        }
        if (v.getId() == R.id.game_slot_3_button){
            Bundle arguments = new Bundle();
            arguments.putString(SelectGameActivity.SLOT_KEY,GameActivity.GAME_STATE_SLOT2_KEY);
            Intent intent = new Intent(this,GameActivity.class);
            intent.putExtras(arguments);
            startActivity(intent);
        }
        if (v.getId() == R.id.clear_games_button){
            PreferencesController.getInstance().clearGameState(GameActivity.GAME_STATE_SLOT1_KEY);
            PreferencesController.getInstance().clearGameState(GameActivity.GAME_STATE_SLOT2_KEY);
            PreferencesController.getInstance().clearGameState(GameActivity.GAME_STATE_SLOT3_KEY);
            setSlotVisibility();
            setSlotText();
        }
    }

    private void setSlotText(){
        GameState gameState1 = PreferencesController.getInstance().readGameState(GameActivity.GAME_STATE_SLOT1_KEY);
        if (gameState1 != null){
        gameSlot1Button.setText(gameInfo);
        } else gameSlot1Button.setText(emptySlot);

        if (gameState2 != null){
            gameSlot2Button.setText(gameInfo2);
        } else gameSlot2Button.setText(emptySlot);

        if (gameState3 != null){
            gameSlot3Button.setText(gameInfo3);
        } else gameSlot3Button.setText(emptySlot);

    }

    private void setSlotVisibility() {
        gameSlot1Button.setEnabled(PreferencesController.getInstance().readGameState(GameActivity.GAME_STATE_SLOT1_KEY) != null);
        gameSlot2Button.setEnabled(PreferencesController.getInstance().readGameState(GameActivity.GAME_STATE_SLOT2_KEY) != null);
        gameSlot3Button.setEnabled(PreferencesController.getInstance().readGameState(GameActivity.GAME_STATE_SLOT3_KEY) != null);
    }

    private String stringBuilder(GameState gameState){
        return gameState.getPlayerList() + ", ";

    }

}