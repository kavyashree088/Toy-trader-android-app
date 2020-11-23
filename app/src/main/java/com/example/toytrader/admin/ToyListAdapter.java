package com.example.toytrader.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toytrader.R;

import java.util.ArrayList;

public class ToyListAdapter extends RecyclerView.Adapter<ToyListAdapter.ToyListViewHolder> {
    private ArrayList<ToyModel> users;
    private Context context;

    @NonNull
    @Override
    public ToyListAdapter.ToyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_row, parent,false);
        ToyListAdapter.ToyListViewHolder toyListViewHolder = new ToyListAdapter.ToyListViewHolder(view, context);
        return toyListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToyListAdapter.ToyListViewHolder holder, int position) {
        ToyModel user = users.get(position);
        holder.userName.setText(user.getName());
        holder.user = user;
    }

    public ToyListAdapter(ArrayList<ToyModel> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ToyListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView userName;
        private Button viewUser;
        private Context context;
        private ToyModel user;

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(".ToyDetailsActivity");
            intent.putExtra("ToyId", user.getToyId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        public ToyListViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            userName = itemView.findViewById(R.id.user_name_txt);
            viewUser = itemView.findViewById(R.id.view_user_btn);
            viewUser.setOnClickListener(this);
        }
    }

}