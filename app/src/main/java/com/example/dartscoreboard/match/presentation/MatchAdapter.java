package com.example.dartscoreboard.match.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.match.data.models.LegWithVisits;
import com.example.dartscoreboard.match.data.models.MatchWithUsers;

import java.util.Objects;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.GameViewHolder> {
    
    private MatchWithUsers matchWithUsers = new MatchWithUsers();

    private LegWithVisits gamesWithVisits = new LegWithVisits();

    public MatchAdapter() {

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

        String name = matchWithUsers.users.get(position).getUsername();
        int userId = matchWithUsers.users.get(position).userId;
        
        int startingScore = matchWithUsers.match.getMatchType().startingScore;
        int visitScores = getVisitScores(userId);
        int currentScore = startingScore - visitScores;
        int currentLegs = getCurrentLegs(userId);
        int currentSets = getCurrentSets(userId);

        holder.nameText.setText(name);
        holder.playerScoreTextView.setText(String.valueOf(currentScore));
        holder.legsTextView.setText(String.valueOf(currentLegs));
        holder.setsTextView.setText(String.valueOf(currentSets));
        if (gamesWithVisits.leg != null) {
            holder.playerIndicator.setVisibility(gamesWithVisits.leg.turnIndex == position ? View.VISIBLE : View.GONE);
        }

    }

    private int getCurrentLegs(int userId) {
        return (int) matchWithUsers.legs.stream().
                filter(game -> Objects.equals(game.setId, gamesWithVisits.leg.setId) && game.winnerId == userId)
                .count();
    }

    private int getCurrentSets(int userId) {
        return (int) matchWithUsers.sets.stream()
                .filter(set -> set.winnerId == userId)
                .count();
    }

    public int getVisitScores(int userId) {
        if (gamesWithVisits.visits != null) {
            return gamesWithVisits.visits.stream()
                    .filter(visit -> visit.userId == userId)
                    .mapToInt(value -> value.score)
                    .sum();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return (matchWithUsers != null && matchWithUsers.users != null) ? matchWithUsers.users.size() : 0;
    }

    public void setMatchData(MatchWithUsers matchWithUsers) {
        this.matchWithUsers = matchWithUsers;
        notifyDataSetChanged();
    }

    public void setLegWithVisits(LegWithVisits gamesWithVisits) {
        this.gamesWithVisits = gamesWithVisits;
        notifyDataSetChanged();
    }

}

