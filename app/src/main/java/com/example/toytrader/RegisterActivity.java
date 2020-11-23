package com.example.toytrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements FirebaseListener {
    private TextView loginText;
    private EditText email, password, firstName, lastName, phoneNumber, address, pinCode;
    private Button registerButton;
    private ProgressBar spinner;

    private ArrayList<EditText> aed;
    private Map<String, Object> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupAllViews();
        setupTextWatchers();
        onClickLoginText();
        onClickRegisterButton();
    }

    private void setupAllViews() {
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        firstName = findViewById(R.id.register_firstName);
        lastName = findViewById(R.id.register_lastName);
        phoneNumber = findViewById(R.id.register_phone);
        address = findViewById(R.id.register_address);
        pinCode = findViewById(R.id.register_pincode);
        loginText = (TextView)findViewById(R.id.login_text);
        registerButton =(Button)findViewById(R.id.register_btn);
        spinner = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        spinner.setVisibility(View.GONE);

        aed = new ArrayList<EditText>();
        aed.add(email);
        aed.add(password);
        aed.add(firstName);
        aed.add(lastName);
        aed.add(phoneNumber);
        aed.add(address);
        aed.add(pinCode);
    }

    private void setupTextWatchers() {
        email.addTextChangedListener(new CustomTextWatcher(email));
        password.addTextChangedListener(new CustomTextWatcher(password));
        firstName.addTextChangedListener(new CustomTextWatcher(firstName));
        lastName.addTextChangedListener(new CustomTextWatcher(lastName));
        phoneNumber.addTextChangedListener(new CustomTextWatcher(phoneNumber));
        address.addTextChangedListener(new CustomTextWatcher(address));
        pinCode.addTextChangedListener(new CustomTextWatcher(pinCode));
    }


    public void onClickLoginText(){
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(".LoginActivity");
                startActivity(intent);
            }
        });
    }

    public void onClickRegisterButton(){
        final RegisterActivity la = this;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidations()) {
                    spinner.setVisibility(View.VISIBLE);
                    FirebaseHelper.getInstance().signupWith(email.getText().toString(),
                                                            password.getText().toString(), data, la);
                }
            }
        });
    }

    private Boolean checkValidations() {
        for(EditText e : aed) {
            if(e.getText().toString().isEmpty()) {
                SnackbarHelper.showMessage("Please enter all the details", this.findViewById(android.R.id.content));
                return false;
            }
        }

        if (!Utilities.isValidEmail(email.getText())){
            SnackbarHelper.showMessage("Please Enter a valid Email", this.findViewById(android.R.id.content));
            return false;
        }else if (!Utilities.isValidPassword(password.getText())){
            SnackbarHelper.showMessage("Please Enter a valid Password", this.findViewById(android.R.id.content));
            return false;
        }

        data = new HashMap<String, Object>();
        data.put("email", email.getText().toString());
        data.put("address", address.getText().toString());
        data.put("first_name", firstName.getText().toString());
        data.put("last_name", lastName.getText().toString());
        data.put("phoneno", phoneNumber.getText().toString());
        data.put("pincode", pinCode.getText().toString());

        return true;
    }

    @Override
    public <T> void getFBData(T event) {
        spinner.setVisibility(View.GONE);
        if(event != null) {
            Intent intent = new Intent(".UserHomeActivity");
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}


