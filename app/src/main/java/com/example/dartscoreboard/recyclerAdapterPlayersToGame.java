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
import java.util.List;

public class recyclerAdapterPlayersToGame extends RecyclerView.Adapter<recyclerAdapterPlayersToGame.ViewHolderPTG> {

    private List<User> usersList = new ArrayList<>();

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(User user, int position);
    }

    public recyclerAdapterPlayersToGame(){
    }
    public class ViewHolderPTG extends RecyclerView.ViewHolder {
        private TextView nameText;
        private CheckBox checkBox;

        public ViewHolderPTG(final View view){
            super(view);
            nameText = view.findViewById(R.id.name_text1);
            checkBox = view.findViewById(R.id.checkbox);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onClick(usersList.get(position),position);
                    }
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