package com.example.toytrader.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.toytrader.FirebaseHelper;
import com.example.toytrader.FirebaseListener;
import com.example.toytrader.R;
import com.example.toytrader.Toy;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity implements FirebaseListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.users_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new UserListAdapter(users, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        FirebaseHelper.getInstance().getAllUsers(this);

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

    @Override
    public <T> void getFBData(T event) {
        if(event instanceof ArrayList){
            users.addAll((ArrayList<User>) event);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}