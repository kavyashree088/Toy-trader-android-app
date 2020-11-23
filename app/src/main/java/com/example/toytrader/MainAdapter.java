package com.example.toytrader;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{


    ArrayList<String> s;
    public MainAdapter(ArrayList<String> temp) {
        s=temp;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MainAdapter.ViewHolder holder, int position) {
        holder.name.setText(s.get(position));
    }

    @Override
    public int getItemCount() {
        return s.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.category_toyname);
        }
    }
}
