package com.example.dartscoreboard.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;

import java.util.ArrayList;
import java.util.List;

public class recyclerAdapterUsers extends RecyclerView.Adapter<recyclerAdapterUsers.MyViewHolder> {
    private List<User> usersList = new ArrayList<>();

    public recyclerAdapterUsers(){
    }

    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTxt;

        private final CheckBox checkBox;

        public MyViewHolder(final View view){
            super(view);
            nameTxt = view.findViewById(R.id.name_text1);
            checkBox = view.findViewById(R.id.checkbox);
            view.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(usersList.get(position));
                }
            });
        }
    }

    public void setUsersList(List<User> usersList){
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClicked(User user);
    }



    @NonNull
    @Override
    public recyclerAdapterUsers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull recyclerAdapterUsers.MyViewHolder holder, int position) {
        String name = usersList.get(position).getUsername();
        holder.nameTxt.setText(name);
        holder.checkBox.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
