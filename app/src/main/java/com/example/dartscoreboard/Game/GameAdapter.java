package com.example.dartscoreboard.Game;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.User.User;

import java.util.List;
import java.util.stream.Collectors;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<User> usersList;
    private Game game;

    private List<Visit> visits;
    public GameAdapter(List<User> usersList){
        Log.d("james test", "GameAdapter userList constructor: " + usersList);
        this.usersList = usersList;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameText;

        private final TextView setsTextView;
        private final TextView legsTextView;

        private final TextView playerScoreTextView;

        private final FrameLayout playerIndicator;

        public GameViewHolder (final View view){
            super(view);
            nameText = view.findViewById(R.id.player_name_text_view);
            legsTextView = view.findViewById(R.id.legs_text_view);
            setsTextView = view.findViewById(R.id.sets_text_view);
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
        User user = usersList.get(position);

        int startingScore = game.getGameType().startingScore;
        List<Visit> userVisits = visits.stream()
                .filter(visit -> visit.userID == user.userID)
                .collect(Collectors.toList());

        int visitScores = userVisits.stream().mapToInt(visit -> visit.score).sum();
        int currentScore = startingScore - visitScores;


//        int currentLegs = usersList.get(position).getCurrentLegs();
//        int currentSets = usersList.get(position).getCurrentSets();
        holder.nameText.setText(name);
        holder.playerScoreTextView.setText(String.valueOf(currentScore));
//        holder.legsTextView.setText(String.valueOf(currentLegs));
//        holder.setsTextView.setText(String.valueOf(currentSets));
        holder.playerIndicator.setVisibility(GameViewModel.getTurnIndex() == position ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

//    public void setUsersList(List<User> usersList){
//        this.usersList = usersList;
//        notifyDataSetChanged();
//    }


    public void setGame(Game game) {
        this.game = game;
        notifyDataSetChanged();
    }

    public void setVisits(List<Visit> visits){
        this.visits = visits;
        notifyDataSetChanged();
    }
}

