package com.example.dartscoreboard.MatchHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.Game.Game;
import com.example.dartscoreboard.R;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class RecyclerAdapterMatchHistory extends ListAdapter<Game, RecyclerAdapterMatchHistory.MyViewHolder> {

    private OnItemClickListener listener;

    public RecyclerAdapterMatchHistory() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Game> DIFF_CALLBACK = new DiffUtil.ItemCallback<Game>() {
        @Override
        public boolean areItemsTheSame(@NonNull Game oldItem, @NonNull Game newItem) {
            return Objects.equals(oldItem.getGameId(), newItem.getGameId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Game oldItem, @NonNull Game newItem) {
            return false;
//            return oldItem.getOffsetDateTime().isEqual(newItem.getOffsetDateTime());
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
                int position = getBindingAdapterPosition();
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
        Game game = getItem(position);
//        holder.gameTitleTextView.setText(game.getGameType().name);
//        holder.gamePlayersTextView.setText(game.playersCSVString);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
//        String dateStamp = game.getCreatedDate().format(dateFormatter);
//        String timeStamp = game.getCreatedDate().truncatedTo(ChronoUnit.MINUTES).format(timeFormatter);
//        String timeStampTruncated = timeStamp.substring(0,timeStamp.length() - 3);
//        String dateTimeFormatted = dateStamp+"\n"+timeStampTruncated;
//        holder.gameDateCreatedTextView.setText(dateTimeFormatted);
    }

    public Game getGameStateAtPosition(int position) {
        return getItem(position);
    }


    public interface OnItemClickListener {
        void onItemClick(Game game);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }




}


