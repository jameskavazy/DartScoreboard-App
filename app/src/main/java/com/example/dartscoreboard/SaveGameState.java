package com.example.dartscoreboard;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveGameState {

    HashMap<User, Integer> currentScoresMap;
    HashMap<User, Boolean> turnsMap;
    HashMap<User, ArrayList<Integer>> previousScoresListMap;

    public SaveGameState(HashMap<User, Integer> currentScoresMap,  HashMap<User, Boolean> turnsMap, HashMap<User,ArrayList<Integer>> previousScoresListMap){ //HashMap<User, ArrayList<Integer>> previousScoresMap
        this.currentScoresMap = currentScoresMap;
        this.turnsMap = turnsMap;
        this.previousScoresListMap = previousScoresListMap;
    }


    //todo creates an object of hashmaps. Hashmaps will all store different things, all player scores, all player turns, all player average scores etc...
    // todo this then can be added to arraydeque of gamestates

    public void loadPreviousGameState(ArrayList<User> playersList){
        for (User player:playersList
             ) {
            player.setPlayerScore(getCurrentScoresMap(player));
            player.setTurn(getTurnsMap(player));
            player.setPreviousScoresList(getScoresMap(player));
        }
    }

    public Integer getCurrentScoresMap(User user){
        return currentScoresMap.get(user);
    }
    public Boolean getTurnsMap(User user){
        return turnsMap.get(user);
    }

    public ArrayList<Integer> getScoresMap(User user){
        return previousScoresListMap.get(user);
    }



}