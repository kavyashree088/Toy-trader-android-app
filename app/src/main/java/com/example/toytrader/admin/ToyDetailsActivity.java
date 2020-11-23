package com.example.toytrader.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toytrader.DownloadImageTask;
import com.example.toytrader.FirebaseHelper;
import com.example.toytrader.FirebaseListener;
import com.example.toytrader.R;
import com.example.toytrader.Toy;
import com.example.toytrader.Utilities;

public class ToyDetailsActivity extends AppCompatActivity implements FirebaseListener {
    private TextView name, cost, description, category, location;
    private Button delete;
    private Toy selectedToy;
    private ImageView toyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_details);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        setView();

        Intent i = getIntent();
        String toyID = i.getStringExtra("ToyId");
        FirebaseHelper.getInstance().getToyForID(toyID, this);
        onClickDelete();
    }

    public void setView(){
        name = findViewById(R.id.toy_name);
        description = findViewById(R.id.toy_description);
        cost = findViewById(R.id.toy_cost);
        location = findViewById(R.id.toy_location);
        category = findViewById(R.id.toy_category);
        delete = (Button)findViewById(R.id.toy_delete_btn);
        toyImage = (ImageView)findViewById(R.id.toy_image);
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

    @Override
    public <T> void getFBData(T event) {
        if(event instanceof Toy) {
            selectedToy = (Toy) event;
            setToyData();
        }
    }
    private void setToyData() {
        String nam= selectedToy.getName();
        String cat= Utilities.getCategory(selectedToy.getCategory());
        String cos= selectedToy.getCost().toString();
        String desc= selectedToy.getDescription();
        String loca= selectedToy.getLocation();

        name.setText(nam);
        category.setText(cat);
        description.setText( desc);
        cost.setText(cos);
        location.setText(loca);

        if(selectedToy.getImage() != null && !selectedToy.getImage().isEmpty()) {
            new DownloadImageTask(toyImage).execute(selectedToy.getImage());
        }else {
            toyImage.setImageResource(R.drawable.softoys);
        }
    }

    public void onClickDelete(){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ToyDetailsActivity.this);
                builder.setMessage("Are you Sure to delete this Toy?")
                        .setTitle("Delete")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(".ManageToysActivity");
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    @Override
    public <T> void updateFBResult(T event) {

    }
}