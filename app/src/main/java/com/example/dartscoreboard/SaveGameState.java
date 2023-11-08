package com.example.dartscoreboard;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveGameState {

    HashMap<User, Integer> currentScoresMap;
    HashMap<User, Boolean> turnsMap;
    HashMap<User, ArrayList<Integer>> previousScoresListMap;

    public SaveGameState(HashMap<User, Integer> currentScoresMap,  HashMap<User, Boolean> turnsMap,HashMap<User, ArrayList<Integer>> previousScoresListMap){ //
        this.currentScoresMap = currentScoresMap;
        this.turnsMap = turnsMap;
        this.previousScoresListMap = previousScoresListMap;
    }

    // todo change the way averages are calculated


    public void loadPreviousGameState(ArrayList<User> playersList){
        for (User player:playersList
             ) {
            player.setPlayerScore(getCurrentScoresMap(player));
            player.setTurn(getTurnsMap(player));
            Log.d("dom test",player.username + "before updating in sgs class" + player.getPreviousScoresList());
            player.setPreviousScoresList(getPreviousScoresListMap(player));
            Log.d("dom test", player.username + " retrieved scorelist inside of savegamestate class  " + player.getPreviousScoresList());
        }
    }

    public Integer getCurrentScoresMap(User user){
        return currentScoresMap.get(user);
    }
    public Boolean getTurnsMap(User user){
        return turnsMap.get(user);
    }

    public ArrayList<Integer> getPreviousScoresListMap(User user){
        Log.d("dom test", user.username + "Score Map Method For Retrieving For Undo ::" + previousScoresListMap.get(user));
        return previousScoresListMap.get(user);
    }



}