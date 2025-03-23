package com.example.dartscoreboard.LiveMatches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapterLiveProMatches extends RecyclerView.Adapter<RecyclerAdapterLiveProMatches.MatchViewHolder> {

    private List<ProMatch> proMatchList = new ArrayList<>();

    public RecyclerAdapterLiveProMatches(){
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {

        private final TextView homePlayerNameTextView;
        private final TextView awayPlayerNameTextView;
        private final TextView matchStatusTextView;

        private final TextView startTimeTextView;

        private final TextView tournamentNameTextView;

        private final TextView homeScoreTextView;
        private final TextView awayScoreTextView;


        public MatchViewHolder(final View view) {
            super(view);
            homePlayerNameTextView = view.findViewById(R.id.home_player_name);
            awayPlayerNameTextView = view.findViewById(R.id.away_player_name);
            matchStatusTextView = view.findViewById(R.id.match_status);
            startTimeTextView = view.findViewById(R.id.start_time);
            tournamentNameTextView = view.findViewById(R.id.tournament_name);
            homeScoreTextView = view.findViewById(R.id.home_score);
            awayScoreTextView = view.findViewById(R.id.away_score);
        }

    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_matches_list_item,parent,false);
        return new MatchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLiveProMatches.MatchViewHolder holder, int position) {
        ProMatch proMatch = proMatchList.get(position);
        int home_score = proMatch.getHome_team_score();
        int away_score = proMatch.getAway_team_score();
        String time = proMatch.getStart_time().substring(11,16);
        holder.homePlayerNameTextView.setText(proMatch.getHome_team_name());
        holder.awayPlayerNameTextView.setText(proMatch.getAway_team_name());
        holder.tournamentNameTextView.setText(proMatch.getTournament_name());
        holder.startTimeTextView.setText(time);
        holder.matchStatusTextView.setText(proMatch.getStatus());
        holder.homeScoreTextView.setText(String.valueOf(home_score));
        holder.awayScoreTextView.setText(String.valueOf(away_score));
    }

    @Override
    public int getItemCount() {
        return proMatchList.size();
    }

    public void setMatchesList(List<ProMatch> proMatchList) {
        this.proMatchList = proMatchList;
        notifyDataSetChanged();
    }
}
