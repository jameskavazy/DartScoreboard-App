package com.example.dartscoreboard.match.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.user.User;
import com.example.dartscoreboard.util.PreferencesController;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelectAdapter extends RecyclerView.Adapter<PlayerSelectAdapter.ViewHolderPTG> {

    private List<User> usersList = new ArrayList<>();

    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onClick(User user, int position);
    }

    public PlayerSelectAdapter(){
    }
    public class ViewHolderPTG extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final CheckBox checkBox;

        public ViewHolderPTG(final View view){
            super(view);
            nameText = view.findViewById(R.id.name_text1);
            checkBox = view.findViewById(R.id.checkbox);
            view.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onClick(usersList.get(position),position);
                }
            });
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setUsersList(List<User> usersList){
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerSelectAdapter.ViewHolderPTG onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list,parent,false);
        return new ViewHolderPTG(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerSelectAdapter.ViewHolderPTG holder, int position) {
        String name = usersList.get(position).getUsername();
        holder.nameText.setText(name);
        User user = usersList.get(position);

        List<User> savedUsers = PreferencesController.getInstance().getPlayers();
        if (savedUsers == null) {
            savedUsers = new ArrayList<>();
        }
        boolean selected = savedUsers.stream().anyMatch(player -> player.userId == user.userId);
        holder.checkBox.setChecked(selected);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


}