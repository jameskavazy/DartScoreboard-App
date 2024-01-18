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

public class recyclerAdapterUsers extends RecyclerView.Adapter<recyclerAdapterUsers.MyViewHolder> {
    private ArrayList<User> usersList;

    private ClickHandler clickHandler;

    public recyclerAdapterUsers(ArrayList<User> usersList, ClickHandler clickHandler){
        this.usersList = usersList;
        this.clickHandler = clickHandler;
    }

    public interface ClickHandler {
        void onMyButtonClicked (View view, final int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ClickHandler clickHandler;
        private TextView nameTxt;

        private CheckBox checkBox;

        public MyViewHolder(final View view){
            super(view);
            nameTxt = view.findViewById(R.id.name_text1);
            checkBox = view.findViewById(R.id.checkbox);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            clickHandler.onMyButtonClicked(v, getAdapterPosition());
        }
    }

    public void setUsersList(ArrayList<User> usersList){
        this.usersList = usersList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public recyclerAdapterUsers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull recyclerAdapterUsers.MyViewHolder holder, int position) {
        String name = usersList.get(position).getUsername();
        holder.nameTxt.setText(name);
        holder.clickHandler = this.clickHandler;
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
