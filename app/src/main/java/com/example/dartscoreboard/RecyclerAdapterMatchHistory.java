package com.example.dartscoreboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterMatchHistory extends ListAdapter<GameState, RecyclerAdapterMatchHistory.MyViewHolder> {

    private OnItemClickListener listener;
    protected RecyclerAdapterMatchHistory() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<GameState> DIFF_CALLBACK = new DiffUtil.ItemCallback<GameState>() {
        @Override
        public boolean areItemsTheSame(@NonNull GameState oldItem, @NonNull GameState newItem) {
            Log.d("dom test","areItemsTheSame? Ids" + oldItem.getGameID() + newItem.getGameID());
            return oldItem.getGameID() == newItem.getGameID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GameState oldItem, @NonNull GameState newItem) {
            Log.d("dom test","areContentsTheSame times -" + oldItem.getGameID() + " " + oldItem.getOffsetDateTime() + newItem.getGameID() + " " + newItem.getOffsetDateTime());
            return oldItem.getOffsetDateTime().isEqual(newItem.getOffsetDateTime());
        }
    };



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView gameTitleTextView;
        private final TextView gamePlayersTextView;

        private final TextView gameDateCreatedTextView;

        public MyViewHolder(final View view) {
            super(view);
            gameTitleTextView = view.findViewById(R.id.match_history_title_textview);
            gamePlayersTextView = view.findViewById(R.id.match_history_players_textview);
            gameDateCreatedTextView = view.findViewById(R.id.match_history_date_textview);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
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
        GameState currentGameState = getItem(position);
        holder.gameTitleTextView.setText(currentGameState.getGameType().name);
        holder.gamePlayersTextView.setText(usersListAsString(currentGameState));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        String dateStamp = currentGameState.getCreatedDate().format(dateFormatter);
        String timeStamp = currentGameState.getCreatedDate().truncatedTo(ChronoUnit.MINUTES).format(timeFormatter);
        String timeStampTruncated = timeStamp.substring(0,timeStamp.length() - 3);
        holder.gameDateCreatedTextView.setText(dateStamp+ "\n" + timeStampTruncated);
    }

    public GameState getGameStateAtPosition(int position) {
        return getItem(position);
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


