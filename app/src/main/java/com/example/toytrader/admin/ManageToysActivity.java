package com.example.toytrader.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.toytrader.FirebaseHelper;
import com.example.toytrader.FirebaseListener;
import com.example.toytrader.R;

import java.util.ArrayList;

public class ManageToysActivity extends AppCompatActivity implements FirebaseListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    ArrayList<ToyModel> toys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_toys);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        toys = new ArrayList<>();
        recyclerView = findViewById(R.id.toys_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ToyListAdapter(toys, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        FirebaseHelper.getInstance().getAllToys(this);
    }

    @Override
    public <T> void getFBData(T event) {
        if(event instanceof ArrayList){
            toys.addAll((ArrayList<ToyModel>) event);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

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
}