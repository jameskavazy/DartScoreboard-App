package com.example.dartscoreboard;

import android.util.Log;

class Player {
    String name;
    int currentScore;
    boolean playerTurn;

    int lastScoreEntered;

    Player(String name, int currentScore, boolean playerTurn) {
        this.name = name;
        this.currentScore = currentScore;
        this.playerTurn = playerTurn;
    }

//        private void turn(){
//
//
//            //determines score for processing
//                inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> {
//                    if (testPlayer.playerTurn) {
//                        if (actionId == EditorInfo.IME_ACTION_DONE) {
//                            Log.d("dom test", "IME_ACTION_DONE");
//                            onScoreEntered(inputScoreEditText.getText().toString());
//                            ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
//                            return true;
//                        }
//
//                    }
//                    else {
//                        if (actionId == EditorInfo.IME_ACTION_DONE) {
//                            Log.d("dom test", "IME_ACTION_DONE");
//                            onScoreEntered(inputScoreEditText.getText().toString());
//                            ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
//                            return true;
//                        }
//
//                    }
//                    return false;
//                });
//
//        }


    private void printOutDetails() {
        Log.d("dom test", "Name: " + name);
    }

}