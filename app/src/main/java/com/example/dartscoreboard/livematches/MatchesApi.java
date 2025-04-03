package com.example.dartscoreboard.livematches;

import com.example.dartscoreboard.BuildConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MatchesApi {

    String API_KEY = BuildConfig.DartScoreboard_API_KEY;

    @Headers({
            "X-RapidAPI-Key: " + API_KEY,
            "X-RapidAPI-Host: darts-devs.p.rapidapi.com"
    })
    @GET("matches-by-date")
    Call<List<MatchesResponse>> getMatchResponse(@Query("offset") int offset,
                                                 @Query("limit") int limit,
                                                 @Query("date") String date,
                                                 @Query("lang") String lang);
}

