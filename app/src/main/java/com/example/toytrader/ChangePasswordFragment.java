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

public class ChangePasswordFragment extends Fragment implements FirebaseListener{
    private Button savePasswordBtn;
    private Button cancelPasswordBtn;
    private TextView oldPassword;
    private TextView newPassword;
    private TextView confirmNewPassword;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        oldPassword = (TextView) view.findViewById(R.id.current_password_txt);
        newPassword = (TextView) view.findViewById(R.id.new_password_txt);
        confirmNewPassword = (TextView) view.findViewById(R.id.confirm_new_pass_txt);

        savePasswordBtn = (Button)view.findViewById(R.id.save_pass_btn);
        final ChangePasswordFragment fragment = this;
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateNewPassword()){
                    FirebaseHelper.getInstance().changeUserPassword(oldPassword.getText().toString(), newPassword.getText().toString(),fragment );
                }
            }
        });

        cancelPasswordBtn = (Button)view.findViewById(R.id.cancel_pass_btn);
        cancelPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Unasaved Changes will be lost. Are you sure to cancel?")
                        .setTitle("Discard Changes")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().getSupportFragmentManager().beginTransaction().
                                        replace(R.id.profile_fragment,new ViewProfileFragment()).commit();
                            }
                        })
                        .setNegativeButton("NO", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    public boolean validateNewPassword(){
        if(oldPassword.getText().equals(newPassword.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Current Password and new Password can not be same")
                    .setTitle("Error")
                    .setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        } else if(!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("New Password and Confirm password should be same")
                    .setTitle("Error")
                    .setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        } else if(oldPassword.getText().length() == 0 || newPassword.getText().length() == 0
        || confirmNewPassword.getText().length() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Password should not be empty")
                    .setTitle("Error")
                    .setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        }

        return true;
    }

    @Override
    public <T> void getFBData(T event) {
        String res = (String) event;
        if(res.equals("Wrong Password")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Invalid Current password")
                    .setTitle("Error")
                    .setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if(res.equals("Failed")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Internal error in updating password. Please try some time later")
                    .setTitle("Error")
                    .setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.profile_fragment,new ViewProfileFragment()).commit();
        }

    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}