package com.example.dartscoreboard;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveGameState {

    HashMap<User, Integer> currentScores;
    HashMap<User, Boolean> turn;

    HashMap<User, ArrayList<Integer>> previousScores;


    private ArrayList<User> playersList;

    public SaveGameState(HashMap<User, Integer> currentScores,  HashMap<User, Boolean> turn, HashMap<User, ArrayList<Integer>> previousScores){
        this.currentScores = currentScores;
        this.turn = turn;
        this.previousScores = previousScores;
    }


    //todo creates an object of hashmaps. Hashmaps will all store different things, all player scores, all player turns, all player average scores etc...
    // todo this then can be added to arraydeque of gamestates
    // todo change the way averages are calculated
    // on game activity inside playervisit function I think we need to create a for loop that creates objects of gamestates

    public void loadGameState(User user){
        //todo code here



    }


}