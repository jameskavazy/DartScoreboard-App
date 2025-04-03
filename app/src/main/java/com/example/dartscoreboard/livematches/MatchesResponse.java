package com.example.dartscoreboard.livematches;

import java.util.List;

public class MatchesResponse {

    private String date;
    private List<ProMatch> proMatches;

    public MatchesResponse() {
    }

    public String getDate() {
        return date;
    }

    public List<ProMatch> getMatches() {
        return proMatches;
    }
}
