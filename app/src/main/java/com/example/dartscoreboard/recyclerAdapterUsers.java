package com.example.dartscoreboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;
import java.util.List;

public class recyclerAdapterUsers extends RecyclerView.Adapter<recyclerAdapterUsers.MyViewHolder> {
    private List<User> usersList = new ArrayList<>();

    public recyclerAdapterUsers(){
    }

    private OnItemClickListener listener;

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
            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(usersList.get(position));
                }
            });
        }


        @Override
        public void onClick(View v) {
            clickHandler.onMyButtonClicked(v, getAdapterPosition());
        }
    }

    public void setUsersList(List<User> usersList){
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public User getUserAtPosition(int position){
        return usersList.get(position);
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
