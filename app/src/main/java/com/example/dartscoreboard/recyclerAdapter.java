package com.example.dartscoreboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<User> usersList;


//    SharedPreferences sharedPreferences;
//
//    SharedPreferences.Editor editor;
//
//    public static final String userPrefs = "my prefs";
//
//    public static final String userNameKey = "nameKey";


    public recyclerAdapter(ArrayList<User> usersList){
        this.usersList = usersList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTxt;
//        private Context context;

        public MyViewHolder(final View view){
            super(view);
            nameTxt = view.findViewById(R.id.name_text1);
//            sharedPreferences = context.getSharedPreferences(userPrefs, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor1 = sharedPreferences.edit();
//            editor1.putString(userNameKey, "test");

        }


    }



    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = usersList.get(position).getUsername();
        holder.nameTxt.setText(name);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
