package com.example.toytrader.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.toytrader.R;
public class AdminHomeActivity extends AppCompatActivity {
    private Button manageUser, manageToys, manageIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        onClickManageUser();
        onClickManageToys();
        onClickManageIssues();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClickManageUser(){
        manageUser = (Button)findViewById(R.id.mange_users_btn);
        manageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(".ManageUsersActivity");
                startActivity(intent);
            }
        });
    }

    public void onClickManageToys(){
        manageToys = (Button)findViewById(R.id.manage_toys_btn);
        manageToys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(".ManageToysActivity");
                startActivity(intent);
            }
        });
    }

    public void onClickManageIssues(){
        manageIssues = (Button)findViewById(R.id.manga_issues_btn);
        manageIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(".ManageIssuesActivity");
                startActivity(intent);
            }
        });
    }
}