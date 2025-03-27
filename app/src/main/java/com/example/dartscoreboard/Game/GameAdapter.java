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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gamesInMatch = new ArrayList<>();
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
        int currentLegs = getCurrentLegs(user);
        int currentSets = currentLegs % matchWithUsers.match.matchSettings.getTotalSets(); //TODO if games finished and all sets won this will cycle over to 0

        holder.nameText.setText(name);
        holder.playerScoreTextView.setText(String.valueOf(currentScore));
        holder.legsTextView.setText(String.valueOf(currentLegs));
        holder.setsTextView.setText(String.valueOf(currentSets));
        if (gamesWithVisits.game != null){
            holder.playerIndicator.setVisibility(gamesWithVisits.game.turnIndex == position ? View.VISIBLE : View.GONE);
        }
    }
    private int getCurrentLegs(User user) {
        return (int) gamesInMatch.stream()
                .filter(game -> game.winnerId == user.userID)
                .count();
    }

    private int getCurrentSets(User user) {
//        if (matchData.legsSets != null) {
//           return (int) (matchData.legsSets.stream()
//                              .filter(value -> value.userID == user.userID)
//                              .count() % matchData.game.getGameSettings().getTotalSets());
//        }
        return 0;
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

