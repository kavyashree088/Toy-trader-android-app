package com.example.toytrader.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.toytrader.FirebaseHelper;
import com.example.toytrader.FirebaseListener;
import com.example.toytrader.R;

import java.util.ArrayList;

public class ManageIssuesActivity extends AppCompatActivity implements FirebaseListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    ArrayList<IssueModel> issues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_issues);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        issues = new ArrayList<>();
        recyclerView = findViewById(R.id.issues_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new IssueListAdapter(issues, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        FirebaseHelper.getInstance().getAllIssues(this);
    }

    @Override
    public <T> void getFBData(T event) {
        if(event instanceof ArrayList){
            issues.addAll((ArrayList<IssueModel>) event);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}