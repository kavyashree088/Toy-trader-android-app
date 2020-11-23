package com.example.toytrader.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.toytrader.FirebaseHelper;
import com.example.toytrader.FirebaseListener;
import com.example.toytrader.R;

import java.util.HashMap;

public class IssueDetailsActivity extends AppCompatActivity implements FirebaseListener {
    private TextView toyName, reporter, reason, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        setViews();

        Intent i = getIntent();
        String toyID = i.getStringExtra("IssueId");
        FirebaseHelper.getInstance().getIssue(toyID, this);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void setViews(){
        toyName = findViewById(R.id.issue_toy_name_txt);
        reporter = findViewById(R.id.issue_reporter);
        reason = findViewById(R.id.issue_reason_text);
        time = findViewById(R.id.issue_report_time);
    }

    @Override
    public <T> void getFBData(T event) {
        if(event instanceof HashMap){
            toyName.setText("Toy Name: "+((HashMap) event).get("toyname").toString());
            reporter.setText("Reported By: "+((HashMap) event).get("user_name").toString());
            reason.setText("Reason: "+((HashMap) event).get("reason").toString());
            time.setText("Reported At: "+((HashMap) event).get("reported_at").toString());
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}