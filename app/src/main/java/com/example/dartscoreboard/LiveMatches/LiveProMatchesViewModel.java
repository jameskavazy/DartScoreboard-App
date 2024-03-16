package com.example.dartscoreboard.LiveMatches;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveProMatchesViewModel extends AndroidViewModel {

    private LiveProMatchRepository repository;
    private MutableLiveData<List<MatchesResponse>> matchesResponseList;
    public LiveProMatchesViewModel(@NonNull Application application) {
        super(application);
        matchesResponseList = new MutableLiveData<>();
        repository = new LiveProMatchRepository(application);
    }

    public MutableLiveData<List<MatchesResponse>> getMatchesResponseList() {
        return matchesResponseList;
    }

    public void getDataFromApi(String dateString, View view){


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
//                ProgressBar progressBar = view.findViewById(R.id.progressBar);
                view.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(@NonNull Call<List<MatchesResponse>> call, @NonNull Throwable t) {
                Log.d("dom test", "onFailure" + Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void upsertAll(List<Match> matchesList){
        repository.upsertAll(matchesList);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public Single<List<Match>> getAllProMatches(){
       return repository.getAllLiveProMatches();
    }

}
