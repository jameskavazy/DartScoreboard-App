package com.example.dartscoreboard.LiveMatches;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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

    public void getDataFromApi(String dateString){
        if (dateString == null){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateString = simpleDateFormat.format(calendar.getTime());
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://darts-devs.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MatchesApi matchesApi = retrofit.create(MatchesApi.class);
        Call<List<MatchesResponse>> call = matchesApi.getMatchResponse(0,50,"eq."+dateString,"en");
        call.enqueue(new Callback<List<MatchesResponse>>() {
            //todo pagination?
            @Override
            public void onResponse(@NonNull Call<List<MatchesResponse>> call, @NonNull Response<List<MatchesResponse>> response) {
                if (!response.isSuccessful()){
                    Log.d("dom test", String.valueOf(response.code()));
                    return;
                }
                matchesResponseList.postValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<List<MatchesResponse>> call, @NonNull Throwable t) {
                Log.d("dom test", "onFailure" + Objects.requireNonNull(t.getMessage()));
            }
        });
    }

}
