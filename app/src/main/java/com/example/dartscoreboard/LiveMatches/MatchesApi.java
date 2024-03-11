package com.example.dartscoreboard.LiveMatches;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MatchesApi {

    @Headers({
            "X-RapidAPI-Key: 59079c81eemsh194b5158bf4823ep184c8ajsn57be2276eee4",
            "X-RapidAPI-Host: darts-devs.p.rapidapi.com"
    })
    @GET("matches-by-date")
    Call<List<MatchesResponse>> getMatchResponse(@Query("offset") int offset,
                                                 @Query("limit") int limit,
                                                 @Query("date") String date,
                                                 @Query("lang") String lang);
}

