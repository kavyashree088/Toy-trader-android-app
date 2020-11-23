package com.example.toytrader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ToyView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FirebaseListener {
    private DrawerLayout drawer;
    TextView tisname;
    TextView tiscategory;
    TextView tisdescription;
    TextView tiscost;
    TextView tislocation;
    ImageView toyImage;
    private Button addcart;
    private Button reportButton;
    private Button gettoylocation;
    private Toy selectedToy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_view);

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
        String toyID = i.getStringExtra("ToyID");

        FirebaseHelper.getInstance().getToyForID(toyID, this);

        tisname = (TextView) findViewById(R.id.toyviewnametextview);
        tiscategory = (TextView) findViewById(R.id.categorytextview);
        tisdescription = (TextView) findViewById(R.id.descriptiontextview);
        tiscost = (TextView) findViewById(R.id.costtextview);
        tislocation = (TextView) findViewById(R.id.locationtextview);
        reportButton = findViewById(R.id.toyView_report_button);
        toyImage = findViewById(R.id.toy_view_image);
        toyImage.setImageResource(R.drawable.softoys);
        addToCart();
        locateToy();
        reportToy();
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
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return Utilities.handleNavigationDrawerClick(menuItem, this, drawer);
    }

    public void addToCart() {
        final Context c = this;

        addcart = findViewById(R.id.addtocartbutton);
        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double i = Double.parseDouble(tiscost.getText().toString());

                Toy t = selectedToy;

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
                SharedPreferences.Editor editor = preferences.edit();

                Gson gson = new Gson();
                String json = gson.toJson(t);
                Set stringSet = preferences.getStringSet("toys", new HashSet<String>());
                stringSet.add(json);
                editor.putStringSet("toys", stringSet);
                editor.apply();
                Intent intent = new Intent(".UserHomeActivity");
                startActivity(intent);
            }
        });
    }

    public void locateToy() {
        gettoylocation = findViewById(R.id.locationbutton);

        gettoylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(".ToyLocation");
                i.putExtra("toyLocation", selectedToy.getLocation());
                startActivity(i);
            }
        });
    }

    private void reportToy() {
        final ToyView t = this;
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(t);
                final EditText input = new EditText(t);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                builder.setTitle("Report Fraud")
                        .setMessage("What is the problem?")
                        .setView(input)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map m = new HashMap();
                                m.put("reason", input.getText().toString());
                                m.put("toyid", selectedToy.getToyID());
                                m.put("reporterid", FirebaseHelper.getInstance().getFirebaseUser().getUid());
                                m.put("toyname", selectedToy.getName());
                                m.put("reported_at", Calendar.getInstance().getTime().toString());
                                FirebaseHelper.getInstance().reportToy(m, t);
                            }
                        })
                        .setNegativeButton("NO", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void setToyData() {
        String nam = selectedToy.getName();
        String cat = Utilities.getCategory(selectedToy.getCategory());
        String cos = selectedToy.getCost().toString();
        String desc = selectedToy.getDescription();
        String loca = selectedToy.getLocation();

        tisname.setText(nam);
        tiscategory.setText(cat);
        tisdescription.setText(desc);
        tiscost.setText(cos);
        tislocation.setText(loca);

        if (selectedToy.getImage() != null && !selectedToy.getImage().isEmpty()) {
            new DownloadImageTask(toyImage).execute(selectedToy.getImage());
        } else {
            toyImage.setImageResource(R.drawable.softoys);
        }
    }

    @Override
    public <T> void getFBData(T event) {
        if (event instanceof Toy) {
            selectedToy = (Toy) event;
            setToyData();
        } else if (event instanceof Boolean) {
            if (((Boolean) event) == true) {
                SnackbarHelper.showMessage("Reported Successfully, Will be reviewed by an Admin", this.findViewById(android.R.id.content));
            } else {
                SnackbarHelper.showMessage("Something went wrong, try again!", this.findViewById(android.R.id.content));
            }
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}