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

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
    private ArrayList<User> users;
    private Context context;

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_row, parent,false);
        UserListAdapter.UserListViewHolder userListViewHolder = new UserListAdapter.UserListViewHolder(view, context);
        return userListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = users.get(position);
        holder.userName.setText(user.getUserName());
        holder.user = user;
    }

    public UserListAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView userName;
        private Button viewUser;
        private Context context;
        private User user;

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(".UserDetailsActivity");
            intent.putExtra("UserId", user.getUserId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        public UserListViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            userName = itemView.findViewById(R.id.user_name_txt);
            viewUser = itemView.findViewById(R.id.view_user_btn);
            viewUser.setOnClickListener(this);
        }
    }

}
