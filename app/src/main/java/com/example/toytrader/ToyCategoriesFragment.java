package com.example.toytrader;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ToyCategoriesFragment extends Fragment {
    private Button softtoyButton;
    LayoutInflater inflater;
    String category="";
    View v;

    public ToyCategoriesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_toy_categories, container, false);
        return inflater.inflate(R.layout.fragment_toy_categories, container, false);
    }

    public void testToyView(View v)
    {

        softtoyButton =(Button)v.findViewById(R.id.softtoy13);
        softtoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(".ToyView");
                startActivity(intent);
            }
        });
    }


}