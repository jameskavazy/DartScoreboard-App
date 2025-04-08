package com.example.dartscoreboard.match.data.models;

public class MatchData {

    private final MatchWithUsers matchWithUsers;
    private final LegWithVisits legWithVisits;

    public MatchData(MatchWithUsers matchWithUsers, LegWithVisits legWithVisits){
        this.matchWithUsers = matchWithUsers;
        this.legWithVisits = legWithVisits;
    }

    public MatchWithUsers getMatchWithUsers() {
        return matchWithUsers;
    }

    public LegWithVisits getLegWithVisits() {
        return legWithVisits;
    }
}
