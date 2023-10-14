package com.example.dartscoreboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterGamePlayers extends RecyclerView.Adapter<RecyclerAdapterGamePlayers.GameViewHolder> {

    private ArrayList<User> usersList = new ArrayList<>();

    public RecyclerAdapterGamePlayers(ArrayList<User> usersList){
        this.usersList = usersList;
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView legsTextView;
        private TextView playerScoreTextView;

        public GameViewHolder (final View view){
            super(view);
            nameText = view.findViewById(R.id.player_name_text_view);
            legsTextView = view.findViewById(R.id.legs_text_view);
            playerScoreTextView = view.findViewById(R.id.player_score_text_view);
        }

    }



    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_players_list,parent,false);
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        String name = usersList.get(position).getUsername();
        holder.nameText.setText(name);

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


}
