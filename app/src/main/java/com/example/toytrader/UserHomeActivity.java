package com.example.toytrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Button softtoyButton;
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, new ToyCategoriesFragment()).commit();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return Utilities.handleNavigationDrawerClick(menuItem, this, drawer);
    }


    public void openVehicleCategory(View v) {

        softtoyButton = (Button) v.findViewById(R.id.vehicles17);
        softtoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = Constants.VEHICLE;
                openList();
            }
        });
    }

    public void openSoftToyCategory(View v) {

        softtoyButton = (Button) v.findViewById(R.id.softtoy13);
        softtoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = Constants.SOFT_TOYS;
                openList();
            }
        });
    }

    public void openPartyCategory(View v) {


        softtoyButton = (Button) v.findViewById(R.id.partytoys19);
        softtoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = Constants.PARTY_TOYS;
                openList();
            }
        });
    }

    public void openDollsCategory(View v) {


        softtoyButton = (Button) v.findViewById(R.id.dolls14);
        softtoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = Constants.DOLLS;
                openList();
            }
        });
    }

    public void openElectronicsCategory(View v) {
        softtoyButton = (Button) v.findViewById(R.id.electronics15);
        softtoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = Constants.ELECTRONICS;
                openList();
            }
        });
    }

    private void openList(){
        Intent intent = new Intent(".CategoryView");
        intent.putExtra("toy", category);
        startActivity(intent);
    }

}