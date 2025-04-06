package com.example.dartscoreboard.match;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {


    String currentSetId;
    private List<Game> gamesInMatch = new ArrayList<>();

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

    private List<Set> sets = new ArrayList<>();
    private MatchWithUsers matchWithUsers = new MatchWithUsers();

    private GameWithVisits gamesWithVisits = new GameWithVisits();

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

        String name = matchWithUsers.users.get(position).getUsername();
        User user = matchWithUsers.users.get(position);
        int startingScore = matchWithUsers.match.getMatchType().startingScore;
        int visitScores = getVisitScores(user);
        int currentScore = startingScore - visitScores;

        if (gamesWithVisits.game.winnerId == user.userID) {
            currentScore = 0;
        }

        int currentSets = (int) matchWithUsers.sets.stream()
                .filter(set -> set.winnerId == user.userID)
                .count();

        int currentLegs = (int) matchWithUsers.games.stream().
                filter(game -> Objects.equals(game.setId, gamesWithVisits.game.setId) && game.winnerId == user.userID)
                .count();

        holder.nameText.setText(name);
        holder.playerScoreTextView.setText(String.valueOf(currentScore));
        holder.legsTextView.setText(String.valueOf(currentLegs));
        holder.setsTextView.setText(String.valueOf(currentSets));
        if (gamesWithVisits.game != null) {
            holder.playerIndicator.setVisibility(gamesWithVisits.game.turnIndex == position ? View.VISIBLE : View.GONE);
        }

    }
    public int getVisitScores(User user) {
        if (gamesWithVisits.visits != null) {
            List<Visit> visits = gamesWithVisits.visits;

            List<Visit> userVisits = visits.stream()
                    .filter(visit -> visit.userID == user.userID)
                    .collect(Collectors.toList());

            return userVisits.stream().mapToInt(visit -> visit.score).sum();
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

    public void setGameWithVisits(GameWithVisits gamesWithVisits) {
        this.gamesWithVisits = gamesWithVisits;
        notifyDataSetChanged();
    }

    public void setGamesInMatch(List<Game> gamesInMatch) {
        this.gamesInMatch = gamesInMatch;
    }
}

