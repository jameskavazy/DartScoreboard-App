package com.example.dartscoreboard;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveGameState {

    HashMap<User, Integer> currentScoresMap;
    HashMap<User, Boolean> turnsMap;

    HashMap<User, ArrayList<Integer>> previousScoresMap;


    private ArrayList<User> playersList;

    public SaveGameState(HashMap<User, Integer> currentScoresMap,  HashMap<User, Boolean> turnsMap){ //HashMap<User, ArrayList<Integer>> previousScoresMap
        this.currentScoresMap = currentScoresMap;
        this.turnsMap = turnsMap;
       //this.previousScoresMap = previousScoresMap;
    }


    //todo creates an object of hashmaps. Hashmaps will all store different things, all player scores, all player turns, all player average scores etc...
    // todo this then can be added to arraydeque of gamestates
    // todo change the way averages are calculated
    // on game activity inside playervisit function I think we need to create a for loop that creates objects of gamestates

    public void loadPreviousGameState(ArrayList<User> playersList){
        //todo code here

        for (User player:playersList
             ) {
            player.setPlayerScore(getCurrentScoresMap(player));
            player.setTurn(getTurnsMap(player));
            //player.revertScoresList(saveGameState.getScoresMap(player));
        }
    }

    public Integer getCurrentScoresMap(User user){
        return currentScoresMap.get(user);
    }
    public Boolean getTurnsMap(User user){
        return turnsMap.get(user);
    }

    public ArrayList<Integer> getScoresMap(User user){
        return previousScoresMap.get(user);
    }



}