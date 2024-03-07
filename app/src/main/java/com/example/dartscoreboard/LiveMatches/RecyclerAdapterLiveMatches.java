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


public class RecyclerAdapterLiveMatches extends RecyclerView.Adapter<RecyclerAdapterLiveMatches.MatchViewHolder> {


//    private List<MatchesResponse> matchesResponseList = new ArrayList<>();

    private List<Match> matchList = new ArrayList<>();

    public RecyclerAdapterLiveMatches(){
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        private TextView homePlayerNameTextView;
        private TextView awayPlayerNameTextView;
        private TextView matchStatusTextView;

        private TextView startTimeTextView;

        private TextView tournamentNameTextView;

        private TextView homeScoreTextView;
        private TextView awayScoreTextView;


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
    public void onBindViewHolder(@NonNull RecyclerAdapterLiveMatches.MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        int home_score = match.getHome_team_score();
        int away_score = match.getAway_team_score();
        String time = match.getStart_time().substring(11,16);
        holder.homePlayerNameTextView.setText(match.getHome_team_name());
        holder.awayPlayerNameTextView.setText(match.getAway_team_name());
        holder.tournamentNameTextView.setText(match.getTournament_name());
        holder.startTimeTextView.setText(time);
        holder.matchStatusTextView.setText(match.getStatus());
        holder.homeScoreTextView.setText(String.valueOf(home_score));
        holder.awayScoreTextView.setText(String.valueOf(away_score));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public void setMatchesList(List<Match> matchList) {
        this.matchList = matchList;
        notifyDataSetChanged();
    }
}
