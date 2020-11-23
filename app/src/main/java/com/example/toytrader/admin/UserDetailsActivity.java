package com.example.toytrader.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toytrader.FirebaseHelper;
import com.example.toytrader.FirebaseListener;
import com.example.toytrader.R;
import java.util.HashMap;

public class UserDetailsActivity extends AppCompatActivity implements FirebaseListener {
    private TextView firstName, lastName, phoneNo, address, pincode;
    private Button block, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        String userId = i.getStringExtra("UserId");
        System.out.println(userId);
        setView();
        FirebaseHelper.getInstance().getUser(userId, this);
        onClickBlock();
        onClickDelete();
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

    public void setView(){
        firstName = findViewById(R.id.user_first_name);
        lastName = findViewById(R.id.user_last_name);
        phoneNo = findViewById(R.id.user_phone_number);
        address = findViewById(R.id.user_address);
        pincode = findViewById(R.id.user_pincode);
        block = (Button)findViewById(R.id.user_block_btn);
        delete = (Button)findViewById(R.id.user_delete_btn);
    }

    @Override
    public <T> void getFBData(T event) {
        if(event instanceof HashMap){
            firstName.setText(((HashMap) event).get("first_name").toString());
            lastName.setText(((HashMap) event).get("last_name").toString());
            address.setText(((HashMap) event).get("address").toString());
            pincode.setText(((HashMap) event).get("pincode").toString());
            phoneNo.setText(((HashMap) event).get("phoneno").toString());
            if(((HashMap) event).containsKey("block")){
                block.setBackgroundColor(Color.parseColor("#E8E8E8"));
            }
        }
    }

    public void onClickBlock(){
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
                builder.setMessage("Are you Sure to block this user?")
                        .setTitle("Block")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(".ManageUsersActivity");
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    public void onClickDelete(){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
                builder.setMessage("Are you Sure to delete this user?")
                        .setTitle("Delete")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(".ManageUsersActivity");
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