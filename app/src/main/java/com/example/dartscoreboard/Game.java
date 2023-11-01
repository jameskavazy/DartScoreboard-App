//package com.example.dartscoreboard;
//
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//public class Game {
//
//    ArrayList<User> playersList;
//    SelectGameActivity.GameType gameType;
//    int totalLegs;
//    int totalSets;
//
//    boolean gameEnd = false;
//
//
//    public Game(ArrayList<User> playersList, SelectGameActivity.GameType gameType, int totalLegs, int totalSets){
//        this.playersList = playersList;
//        this.gameType = gameType;
//        this.totalLegs = totalLegs;
//        this.totalSets = totalSets;
//    }
//
//    public void setLegs(){
//
//    }
//
//    public void setSets(){
//
//    }
//
//    public void saveGame(){
//        // save player scores here
//
//    }
//
//
//
//
//   public void gameLoop(){
//        if (!gameEnd) {
//            for (int i = 0; i < playersList.size(); i++) {
//                if (playersList.get(i).getPlayerScore() == 0) { //todo makes more sense to put the if checker on the game activity/subtract method
//                    if (playersList.get(i).currentLegs < totalLegs) {
//                        playersList.get(i).currentSets++;
//                        //todo reset all player scores to startingScore value
//                    }
//                    if (playersList.get(i).currentLegs == totalLegs) {
//                        playersList.get(i).currentSets++;
//                        playersList.get(i).currentLegs = 0;
//                    }
//                    if (playersList.get(i).currentSets == totalSets) {
//                        //Match Won
//                        gameEnd = true;
//                    }
//                }
//
//            }
//        }
//    }
//
//
//    //todo should gametype carry legs & sets?
//
//}
