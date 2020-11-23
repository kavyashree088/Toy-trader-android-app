package com.example.toytrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CategoryView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FirebaseListener {


    private DrawerLayout drawer;

    private ArrayList<Toy> toys;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private TextView myaboutbutton;
    private TextView emptyView;
    String thiscategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent i = getIntent();
        thiscategory = i.getStringExtra("toy");

        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);

        /*
        GET DATABASE VALUES HERE OF THAT PARTICULAR CATEGORY
         */
        toys = new ArrayList<Toy>();

        layoutManager = new LinearLayoutManager(this);
        adapter = new ToysAdapter(toys, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        emptyView = findViewById(R.id.empty_view);
        FirebaseHelper.getInstance().getToysForCategory(thiscategory, this);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return Utilities.handleNavigationDrawerClick(menuItem, this, drawer);
    }


    public void aboutToy(View v)
    {
        myaboutbutton =(TextView) v.findViewById(R.id.aboutText);
        myaboutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public <T> void getFBData(T event) {
        if(event instanceof ArrayList){
            toys.addAll((ArrayList<Toy>) event);
            adapter.notifyDataSetChanged();
            if (toys.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }

    
}