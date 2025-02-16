package com.example.dartscoreboard.Statistics;

import com.example.dartscoreboard.User.User;

import java.util.ArrayList;

public final class StatsHelper {

    private static StatsHelper instance;


    private StatsHelper() {

    }

    public static StatsHelper getInstance() {
        if (instance == null){
            instance = new StatsHelper();
        }
        return instance;
    }

    public void updateLifeTimeScores(User user){
        int totalScores = 0;
        if (!user.getPreviousScoresList().isEmpty()){
            for (int i = 0; i < user.getPreviousScoresList().size(); i++){
                totalScores += user.getPreviousScoresList().get(i);
            }
        }
        user.setLifeTimeScore(user.getLifeTimeScore() + totalScores);
    }

    public void updateLifeTimeVisits(User user){
        user.setLifeTimeVisits(user.getLifeTimeVisits() + user.getPreviousScoresList().size());
    }
}
