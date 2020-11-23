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

public class IssueListAdapter extends RecyclerView.Adapter<IssueListAdapter.IssueListViewHolder> {
    private ArrayList<IssueModel> issues;
    private Context context;

    @NonNull
    @Override
    public IssueListAdapter.IssueListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_row, parent,false);
        IssueListAdapter.IssueListViewHolder userListViewHolder = new IssueListAdapter.IssueListViewHolder(view, context);
        return userListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IssueListAdapter.IssueListViewHolder holder, int position) {
        IssueModel issue = issues.get(position);
        holder.toyName.setText(issue.getToyName());
        holder.reason.setText(issue.getReason());
        holder.issueModel = issue;
    }

    public IssueListAdapter(ArrayList<IssueModel> issues, Context context) {
        this.issues = issues;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public static class IssueListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView toyName, reason;
        private Button viewIssue;
        private Context context;
        private IssueModel issueModel;

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(".IssueDetailsActivity");
            intent.putExtra("IssueId", issueModel.getIssueId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        public IssueListViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            toyName = itemView.findViewById(R.id.issue_toy_name);
            reason = itemView.findViewById(R.id.issue_reason);
            viewIssue = itemView.findViewById(R.id.issue_view_button);
            viewIssue.setOnClickListener(this);
        }
    }

}
