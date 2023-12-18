package com.example.dartscoreboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterMatchHistory extends RecyclerView.Adapter<RecyclerAdapterMatchHistory.MyViewHolder> {

    private ArrayList<GameState> gameStatesList;

    public RecyclerAdapterMatchHistory(ArrayList<GameState> gameStatesList){
        this.gameStatesList = gameStatesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView gameInfoTextView;

        public MyViewHolder(final View view){
            super(view);
            gameInfoTextView = view.findViewById(R.id.match_history_textview);
        }


    }

    @NonNull
    @Override
    public RecyclerAdapterMatchHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_games_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterMatchHistory.MyViewHolder holder, int position) {
        String gameInfo = gameStatesList.get(position).getGameType().name;
        holder.gameInfoTextView.setText(gameInfo);
    }

    @Override
    public int getItemCount() {
        return gameStatesList.size();
    }
}
