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
    private MatchData matchData = new MatchData();

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
        String name = matchData.users.get(position).getUsername();
        User user = matchData.users.get(position);
        int startingScore = matchData.match.getMatchType().startingScore;
        int visitScores = getVisitScores(user);
        int currentScore = startingScore - visitScores;
//        int currentLegs = getCurrentLegs(user);
//        int currentSets = getCurrentSets(user);

        holder.nameText.setText(name);
        holder.playerScoreTextView.setText(String.valueOf(currentScore));
//        holder.legsTextView.setText(String.valueOf(currentLegs));
//        holder.setsTextView.setText(String.valueOf(currentSets));
//        holder.playerIndicator.setVisibility(matchData.game.turnIndex == position ? View.VISIBLE : View.GONE);
    }
    private int getCurrentLegs(User user) {
//        if (matchData.legsSets != null) {
//            return (int) matchData.legsSets.stream()
//                    .filter(value -> value.setNumber == matchData.game.currentSet && value.userID == user.userID)
//                    .count();
//        }
        return 0;
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
//        if (matchData.visits != null) {
//            List<Visit> visits = matchData.visits;
//
//            List<Visit> userVisits = visits.stream()
//                    .filter(visit -> visit.userID == user.userID)
//                    .collect(Collectors.toList());
//
//            return userVisits.stream().mapToInt(visit -> visit.score).sum();
//        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return  matchData != null && matchData.users != null ? matchData.users.size() : 0;
    }

    public void setGameWithUsers(MatchData matchData) {
        this.matchData = matchData;
        notifyDataSetChanged();
    }
}

