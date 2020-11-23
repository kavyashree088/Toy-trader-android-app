package com.example.toytrader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

public class ViewProfileFragment extends Fragment implements FirebaseListener {
    private Button editProfileButton;
    private TextView changePasswordText;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView address;
    private TextView pincode;
    private TextView phone;

    public ViewProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_profile, container, false);
        final ViewProfileFragment fragment = this;
        FirebaseHelper.getInstance().getDetailsForCurrentUser(fragment);
        editProfileButton = (Button)view.findViewById(R.id.edit_profile_btn);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.profile_fragment,new EditProfileFragment()).commit();
            }
        });

        changePasswordText  = (TextView)view.findViewById(R.id.change_password_link);
        changePasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.profile_fragment,new ChangePasswordFragment()).commit();
            }
        });
        return view;
    }

    @Override
    public <T> void getFBData(T event) {
        if (event != null){
            Map m = (Map)event;
            System.out.println(m);
            firstName = (TextView)getView().findViewById(R.id.first_name_view_txt);
            firstName.setText(m.get("first_name").toString());

            lastName = (TextView)getView().findViewById(R.id.last_name_view_txt);
            lastName.setText(m.get("last_name").toString());

            phone = (TextView)getView().findViewById(R.id.phone_view_txt);
            phone.setText(m.get("phoneno").toString());

            address = (TextView)getView().findViewById(R.id.address_view_txt);
            address.setText(m.get("address").toString());

            email = (TextView)getView().findViewById(R.id.email_view_text);
            email.setText(m.get("email").toString());

            pincode = (TextView)getView().findViewById(R.id.pincode_view_txt);
            pincode.setText(m.get("pincode").toString());
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}