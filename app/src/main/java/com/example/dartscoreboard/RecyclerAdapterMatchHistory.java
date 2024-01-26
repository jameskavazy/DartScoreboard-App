package com.example.dartscoreboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterMatchHistory extends RecyclerView.Adapter<RecyclerAdapterMatchHistory.MyViewHolder> {

    private List<GameState> gameStatesList = new ArrayList<>();
    private OnItemClickListener listener;

    public RecyclerAdapterMatchHistory() {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView gameTitleTextView;
        private TextView gamePlayersTextView;

        private TextView gameDateCreatedTextView;

        public MyViewHolder(final View view) {
            super(view);
            gameTitleTextView = view.findViewById(R.id.match_history_title_textview);
            gamePlayersTextView = view.findViewById(R.id.match_history_players_textview);
            gameDateCreatedTextView = view.findViewById(R.id.match_history_date_textview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(gameStatesList.get(position));
                    }

                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterMatchHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_games_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterMatchHistory.MyViewHolder holder, int position) {
        GameState currentGameState = gameStatesList.get(position);
        holder.gameTitleTextView.setText(currentGameState.getGameType().name);
        holder.gamePlayersTextView.setText(usersListAsString(currentGameState));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        String dateStamp = currentGameState.getCreatedDate().format(dateFormatter);
        String timeStamp = currentGameState.getCreatedDate().truncatedTo(ChronoUnit.MINUTES).format(timeFormatter);
        String timeStampTruncated = timeStamp.substring(0,timeStamp.length() - 3);
        holder.gameDateCreatedTextView.setText(dateStamp+ "\n" + timeStampTruncated);
    }

    @Override
    public int getItemCount() {
        return gameStatesList.size();
    }

    public GameState getGameStateAtPosition(int position) {
        return gameStatesList.get(position);
    }


    public void setGameStatesList(List<GameState> gameStatesList) {
        this.gameStatesList = gameStatesList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(GameState gameState);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private String usersListAsString(GameState gameState) {
        String playerNamesString = null;
        if (gameState.getPlayerList() != null) {
            String[] namesOfGame = new String[gameState.getPlayerList().size()];
            for (int i = 0; i < gameState.getPlayerList().size(); i++) {
                namesOfGame[i] = gameState.getPlayerList().get(i).getUsername();
            }
            playerNamesString = String.join(", ", namesOfGame);
        }
        return playerNamesString;
    }
}


