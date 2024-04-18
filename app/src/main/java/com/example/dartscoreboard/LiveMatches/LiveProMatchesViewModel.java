package com.example.dartscoreboard.LiveMatches;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;

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

public class LiveProMatchesViewModel extends AndroidViewModel {

    private final LiveProMatchRepository repository;

    private final MutableLiveData<Integer> recyclerViewVisibility = new MutableLiveData<>();

    public LiveProMatchesViewModel(@NonNull Application application) {
        super(application);
        repository = new LiveProMatchRepository(application);
    }

    public void getDataFromApi(String dateString){

        if (Objects.equals(dateString, "TODAY")){
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

                if (response.body() != null && !response.body().isEmpty()){
                    recyclerViewVisibility.setValue(View.VISIBLE);
                    deleteAll();
                    upsertAll(response.body().get(0).getMatches());
                } else {
                    Log.d("dom test", "response.body is empty or null");
                    recyclerViewVisibility.setValue(View.GONE);
                    Toast.makeText(DartsScoreboardApplication.getContext(), "No Matches Found", Toast.LENGTH_SHORT).show();
                }
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

    public LiveData<List<Match>> getAllProMatches(){
       return repository.getAllLiveProMatches();
    }

    public MutableLiveData<Integer> getRecyclerViewVisibility(){
        return recyclerViewVisibility;
    }

}
