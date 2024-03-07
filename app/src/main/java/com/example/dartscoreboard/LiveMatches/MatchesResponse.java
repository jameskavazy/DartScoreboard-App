package com.example.dartscoreboard.LiveMatches;

import java.util.List;

public class MatchesResponse {

    private String date;
    private List<Match> matches;

    public String getDate() {
        return date;
    }

    public List<Match> getMatches() {
        return matches;
    }
}
