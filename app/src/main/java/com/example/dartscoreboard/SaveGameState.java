package com.example.dartscoreboard;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveGameState {

    HashMap<User, Integer> currentScoresMap;
    HashMap<User, Boolean> turnsMap;
    HashMap<User, ArrayList<Integer>> previousScoresListMap;

    HashMap<User, Integer> previousLegsMap;

    HashMap<User, Integer> previousSetsMap;

    public SaveGameState(HashMap<User, Integer> currentScoresMap,  HashMap<User, Boolean> turnsMap, HashMap<User,ArrayList<Integer>> previousScoresListMap,HashMap<User, Integer> previousLegsMap, HashMap<User, Integer> previousSetsMap){
        this.currentScoresMap = currentScoresMap;
        this.turnsMap = turnsMap;
        this.previousScoresListMap = previousScoresListMap;
        this.previousLegsMap = previousLegsMap;
        this.previousSetsMap = previousSetsMap;
    }


    //todo creates an object of hashmaps. Hashmaps will all store different things, all player scores, all player turns, all player average scores etc...
    // todo this then can be added to arraydeque of gamestates

    public void loadPreviousGameState(ArrayList<User> playersList){
        for (User player:playersList
             ) {
            player.setPlayerScore(getCurrentScoresMap(player));
            player.setTurn(getTurnsMap(player));
            player.setPreviousScoresList(getPreviousScoresListMap(player));
            player.setPlayerLegs(getLegsMap(player));
            player.setPlayerSets(getSetsMap(player));

            //todo below code block shouldn't be needed but the setpreviousscore method above doesn't do its job
            if (player.turn && !player.previousScoresList.isEmpty()){
                player.previousScoresList.remove(player.previousScoresList.size() - 1);
                //player.visits--;
            }
        }
    }



    public Integer getCurrentScoresMap(User user){
        //Log.d("dom test", user.username + " Current Score to reset to? = " + getCurrentScoresMap(user));
        return currentScoresMap.get(user);
    }
    public Boolean getTurnsMap(User user){
        return turnsMap.get(user);
    }

    public ArrayList<Integer> getPreviousScoresListMap(User user){
        //Log.d("dom test", "onGetPreviousScoresListMap -- This is what we're setting previousScoresList to!  "+ user.username + previousScoresListMap.get(user));
        return previousScoresListMap.get(user);
    }

    public Integer getLegsMap(User user){
        return previousLegsMap.get(user);
    }

    private Integer getSetsMap(User user) {
        return previousSetsMap.get(user);
    }


}