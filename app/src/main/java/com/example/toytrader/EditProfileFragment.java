package com.example.toytrader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment implements FirebaseListener {

    private Button saveButton;
    private Button cancelButton;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView address;
    private TextView pincode;
    private TextView phone;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        final EditProfileFragment fragment = this;
        FirebaseHelper.getInstance().getDetailsForCurrentUser(fragment);

        saveButton = (Button) view.findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap data = new HashMap<String, Object>();
                data.put("email", email.getText().toString());
                data.put("address", address.getText().toString());
                data.put("first_name", firstName.getText().toString());
                data.put("last_name", lastName.getText().toString());
                data.put("phoneno", phone.getText().toString());
                data.put("pincode", pincode.getText().toString());
                FirebaseHelper.getInstance().updateDetailsForCurrentUser(data, fragment);
            }
        });

        cancelButton = (Button) view.findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Unsaved Changes will be lost. Are you sure to cancel?")
                        .setTitle("Discard Changes")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().getSupportFragmentManager().beginTransaction().
                                        replace(R.id.profile_fragment, new ViewProfileFragment()).commit();
                            }
                        })
                        .setNegativeButton("NO", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    @Override
    public <T> void getFBData(T event) {
        if (event != null) {
            Map m = (Map) event;
            initialiseFields();
            firstName.setText(m.get("first_name").toString());
            lastName.setText(m.get("last_name").toString());
            phone.setText(m.get("phoneno").toString());
            address.setText(m.get("address").toString());
            email.setText(m.get("email").toString());
            pincode.setText(m.get("pincode").toString());
        }
    }

    @Override
    public <T> void updateFBResult(T event) {
        Boolean res = (Boolean) event;
        if (res) {
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.profile_fragment, new ViewProfileFragment()).commit();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Unable to save your changes. Please try sometime later.")
                    .setTitle("Error")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().getSupportFragmentManager().beginTransaction().
                                    replace(R.id.profile_fragment, new ViewProfileFragment()).commit();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }

    public void initialiseFields() {
        firstName = (TextView) getView().findViewById(R.id.first_name_edit_txt);
        lastName = (TextView) getView().findViewById(R.id.last_name_edit_txt);
        phone = (TextView) getView().findViewById(R.id.phone_edit_txt);
        address = (TextView) getView().findViewById(R.id.address_edit_text);
        email = (TextView) getView().findViewById(R.id.edit_email_txt);
        pincode = (TextView) getView().findViewById(R.id.pincode_edit_txt);
    }
}