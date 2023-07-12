package com.example.dartscoreboard;

import java.util.Scanner;


public class Player {
    String name;
    int playerScore;
    int tally;
    double visit;
    double threeDartAverage;
    int gameType;




    public Player(String inputName, int numberOfDarts, double average){
        name = inputName;
        playerScore = gameType;
        visit = numberOfDarts;
        threeDartAverage = average;
        Scanner userInputName = new Scanner(System.in);
        System.out.println("Enter your name");
        name = userInputName.nextLine();
    }
    public int getGameType() {


        Scanner chooseGame = new Scanner(System.in);
// User selects game type
        System.out.println("Enter starting score: 170, 301 or 501: ");
        gameType = chooseGame.nextInt();
//Ensures only 501, 301 and 170 can be selected - while the input has to be manual
        while ((gameType != 501) && (gameType != 301) && (gameType != 170)) {
            System.out.println("Invalid starting score.");
            System.out.println("Enter starting score: 170, 301 or 501: ");
            gameType = chooseGame.nextInt();
        }
        return gameType;
    }


    public int getPlayerScore(){
        playerScore = getGameType();
        return playerScore;
    }


    public void getThreeDartAverage(){
        threeDartAverage = (gameType - playerScore) / (visit);
        System.out.println("3 Dart Average: "+ threeDartAverage);
    }


    public int subtract() {
        int new_score = playerScore - tally;
        if (( ((playerScore <= 180) && (playerScore >= 171)) || (playerScore == 169) || (playerScore == 168) || (playerScore == 166) || (playerScore == 165) || (playerScore == 163) || (playerScore == 162) || (playerScore == 159)) && (tally == playerScore)){
            System.out.println("Invalid Score");
            return playerScore;
        }
        if (new_score == 0 || new_score > 1) {
            return new_score;
        }
        else {
            System.out.println("BUST");
        } return playerScore;
    }


    public int turn() {
        Scanner enterScore = new Scanner(System.in);
        System.out.println(name);
        System.out.println(playerScore);
        System.out.println();
        System.out.println("Enter your score: ");
        tally = enterScore.nextInt();
        while (!(tally <= 180 && tally >= 0) || (((playerScore <= 180) && (playerScore >= 171)) && (tally == playerScore))) {
            System.out.println("Invalid score");
            System.out.println();
            System.out.println(name);
            System.out.println(playerScore);
            System.out.println();
            tally = enterScore.nextInt();
        }
        playerScore = subtract();
        visit++;
        getThreeDartAverage();
        return playerScore;
    }




/* public double getAverage(){
threeDartAverage = ((double) (playerScore - tally) / visit);
return threeDartAverage;
}




*/




}
/* public static void addUser(){
ArrayList<String> listOfUsers = new ArrayList<String>();
Scanner userInputName = new Scanner(System.in);
System.out.println("Enter your name");
String name = userInputName.nextLine();
listOfUsers.add(name);
}




public String selectUser(){
System.out.println(Arrays.toString(listOfUsers.toArray()));







}*/

