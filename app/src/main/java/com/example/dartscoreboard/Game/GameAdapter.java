package com.example.dartscoreboard.Game;

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
    private GameData gameData = new GameData();

    public GameAdapter() {

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
        String name = gameData.users.get(position).getUsername();
        User user = gameData.users.get(position);
        int startingScore = gameData.game.getGameType().startingScore;
        int visitScores = getVisitScores(user);
        int currentScore = startingScore - visitScores;
        int currentLegs = getCurrentLegsSets(user, MatchLegsSets.Type.Leg);
        int currentSets = getCurrentLegsSets(user, MatchLegsSets.Type.Set);

        holder.nameText.setText(name);
        holder.playerScoreTextView.setText(String.valueOf(currentScore));
        holder.legsTextView.setText(String.valueOf(currentLegs));
        holder.setsTextView.setText(String.valueOf(currentSets));
        holder.playerIndicator.setVisibility(gameData.game.turnIndex == position ? View.VISIBLE : View.GONE);
    }

    private int getCurrentLegsSets(User user, MatchLegsSets.Type type) {
        if (gameData.legsSets != null) {
            return (int) gameData.legsSets.stream()
                    .filter(value -> value.type == type && value.userID == user.userID)
                    .count();
        }
        return 0;
    }

    public int getVisitScores(User user) {
        if (gameData.visits != null) {
            List<Visit> visits = gameData.visits;

            List<Visit> userVisits = visits.stream()
                    .filter(visit -> visit.userID == user.userID)
                    .collect(Collectors.toList());

            return userVisits.stream().mapToInt(visit -> visit.score).sum();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return  gameData != null && gameData.users != null ? gameData.users.size() : 0;
    }

    public void setGameWithUsers(GameData gameData) {
        this.gameData = gameData;
        notifyDataSetChanged();
    }
}

