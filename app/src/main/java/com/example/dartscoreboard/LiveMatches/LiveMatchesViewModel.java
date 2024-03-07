package com.example.dartscoreboard.LiveMatches;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveMatchesViewModel extends AndroidViewModel {

    private MutableLiveData<List<MatchesResponse>> matchesResponseList;
    public LiveMatchesViewModel(@NonNull Application application) {
        super(application);
        matchesResponseList = new MutableLiveData<>();
    }

    public MutableLiveData<List<MatchesResponse>> getMatchesResponseList() {
        return matchesResponseList;
    }

//    public void setMatchList(MutableLiveData<List<MatchesResponse>> matchList) {
//        this.matchList = matchList;
//    }



    public void getDataFromApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://darts-devs.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<MatchesResponse>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<MatchesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MatchesResponse>> call, @NonNull Response<List<MatchesResponse>> response) {
                if (!response.isSuccessful()){
                    Log.d("dom test", String.valueOf(response.code()));
                    return;
                }
//                assert response.body() != null;
                matchesResponseList.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<MatchesResponse>> call, @NonNull Throwable t) {
                Log.d("dom test", "onFailure" + Objects.requireNonNull(t.getMessage()));
            }
        });
        ///matches-by-date?offset=0&limit=50&date=eq.2024-03-03&lang=en
    }
}
