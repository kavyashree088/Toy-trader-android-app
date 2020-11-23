package com.example.toytrader.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.toytrader.R;
import com.example.toytrader.SnackbarHelper;

public class AdminLoginActivity extends AppCompatActivity {

    private TextView userLoginText;
    private EditText email, password;
    private Button login;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        setupAllViews();
        onClickUserLogin();
        onClockLogin();
    }

    private void setupAllViews() {
        email = findViewById(R.id.admin_login_email);
        password = findViewById(R.id.admin_login_password);
        login = findViewById(R.id.admin_login_button);
        userLoginText = (TextView) findViewById(R.id.user_login_text);
        spinner = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        spinner.setVisibility(View.GONE);
    }

    public void onClickUserLogin(){
        userLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(".LoginActivity");
                startActivity(intent);
            }
        });
    }

    public void onClockLogin(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    Intent intent = new Intent(".AdminHomeActivity");
                    startActivity(intent);
                } else{
                    SnackbarHelper.showMessage("Invalid Credentials", AdminLoginActivity.this.findViewById(android.R.id.content));
                }
            }
        });
    }
}