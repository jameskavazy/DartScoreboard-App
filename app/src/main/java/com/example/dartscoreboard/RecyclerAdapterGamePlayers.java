package com.example.dartscoreboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterGamePlayers extends RecyclerView.Adapter<RecyclerAdapterGamePlayers.GameViewHolder> {

    private ArrayList<User> usersList = new ArrayList<>();
  //  private int legs;
    private int gameScore;

    public RecyclerAdapterGamePlayers(ArrayList<User> usersList, int gameScore){
        this.usersList = usersList;
        //this.legs = legs;
        this.gameScore = gameScore;

    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView legsTextView;
        private TextView playerScoreTextView;

        private FrameLayout playerIndicator;

        public GameViewHolder (final View view){
            super(view);
            nameText = view.findViewById(R.id.player_name_text_view);
            legsTextView = view.findViewById(R.id.legs_text_view);
            playerScoreTextView = view.findViewById(R.id.player_score_text_view);
            playerIndicator = view.findViewById(R.id.player_turn_indicator);
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
        int gameScore = usersList.get(position).getPlayerScore();
        boolean playerTurn = usersList.get(position).isTurn();
        holder.nameText.setText(name);
        holder.playerScoreTextView.setText(String.valueOf(gameScore));
        if (!playerTurn){
            holder.playerIndicator.setVisibility(View.INVISIBLE);
        }
        if (playerTurn) {
            holder.playerIndicator.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


}
