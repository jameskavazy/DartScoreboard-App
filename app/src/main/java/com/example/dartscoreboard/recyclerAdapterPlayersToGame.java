package com.example.dartscoreboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

public class recyclerAdapterPlayersToGame extends RecyclerView.Adapter<recyclerAdapterPlayersToGame.ViewHolderPTG> {

    private ArrayList<User> usersList;
    private ClickListen listen;

    private User user;

    public interface ClickListen {
        void onClick(View view, final int position);
    }

    public recyclerAdapterPlayersToGame(ArrayList<User> usersList, ClickListen listen){
        this.usersList = usersList;
        this.listen = listen;
    }
    public class ViewHolderPTG extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameText;
        private CheckBox checkBox;

        public ViewHolderPTG(final View view){
            super(view);
            nameText = view.findViewById(R.id.name_text1);
            checkBox = view.findViewById(R.id.checkbox);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listen.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public recyclerAdapterPlayersToGame.ViewHolderPTG onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list,parent,false);
        return new ViewHolderPTG(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapterPlayersToGame.ViewHolderPTG holder, int position) {
        String name = usersList.get(position).getUsername();
        holder.nameText.setText(name);
        User user = usersList.get(position);
        boolean active = user.getActive();
        holder.checkBox.setChecked(active);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


}