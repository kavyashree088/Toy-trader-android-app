package com.example.toytrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadToy extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener, FirebaseListener {

    private EditText name, tags, location, cost, description;
    private Button uploadImageButton;
    private ImageView imageView;
    private Uri selectedImage;
    private Spinner spinner;
    private ProgressBar progressSpinner;
    private Map toyData;
    private static final String[] paths = {"Vehicle", "Soft Toys", "Party Toys", "Dolls", "Electronics"};
    private DrawerLayout drawer;
    String category = "";
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_toy);

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        spinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UploadToy.this,
                android.R.layout.simple_spinner_item, paths);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        setupViews();
        setupTextWatchers();
        setupButtonClick();
    }

    private void setupViews() {
        name = findViewById(R.id.nametext);
        location = findViewById(R.id.locationtext);
        cost = findViewById(R.id.costtext);
        description = findViewById(R.id.descriptiontext);
        uploadImageButton = findViewById(R.id.imagesrc);
        imageView = findViewById(R.id.upload_toy_image);
        progressSpinner = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        progressSpinner.setVisibility(View.GONE);
    }

    private void setupTextWatchers() {
        name.addTextChangedListener(new CustomTextWatcher(name));
        location.addTextChangedListener(new CustomTextWatcher(location));
        cost.addTextChangedListener(new CustomTextWatcher(cost));
        description.addTextChangedListener(new CustomTextWatcher(description));
    }

    private void setupButtonClick() {
        final UploadToy ut = this;
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ut, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
                showDialog("Select an Option");
            }
        });
    }

    private void showDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text);
        builder.setMessage("Pick One").setCancelable(true).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        }).setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0: //Camera
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    selectedImage = getImageUri(photo);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(photo);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageURI(selectedImage);
                }
                break;
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            //retrive this string on category page
            case 0:
                category = Constants.VEHICLE;
                Toast.makeText(parent.getContext(), "Vehicles selected!", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                category = Constants.SOFT_TOYS;
                Toast.makeText(parent.getContext(), "Soft Toys selected!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                category = Constants.PARTY_TOYS;
                Toast.makeText(parent.getContext(), "Party Toys Selected!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                category = Constants.DOLLS;
                Toast.makeText(parent.getContext(), "Dolls Selected!", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                category = Constants.ELECTRONICS;
                Toast.makeText(parent.getContext(), "Electronics Selected!", Toast.LENGTH_SHORT).show();
                break;

        }

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
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return Utilities.handleNavigationDrawerClick(menuItem, this, drawer);
    }

    private Boolean checkValidations() {
        if (selectedImage == null) {
            SnackbarHelper.showMessage("Please select a toy image", this.findViewById(android.R.id.content));
            return false;
        } else if (name.getText().toString().isEmpty()) {
            SnackbarHelper.showMessage("Please enter toy name", this.findViewById(android.R.id.content));
            return false;
        } else if (category.isEmpty()) {
            SnackbarHelper.showMessage("Please select a category", this.findViewById(android.R.id.content));
            return false;
        } else if (description.getText().toString().isEmpty()) {
            SnackbarHelper.showMessage("Please enter some toy description", this.findViewById(android.R.id.content));
            return false;
        } else if (cost.getText().toString().isEmpty()) {
            SnackbarHelper.showMessage("Please enter some cost for the toy", this.findViewById(android.R.id.content));
            return false;
        } else if (location.getText().toString().isEmpty()) {
            SnackbarHelper.showMessage("Please enter the address for the toy", this.findViewById(android.R.id.content));
            return false;
        }
        toyData = new HashMap();
        toyData.put("category", category);
        toyData.put("cost", Float.parseFloat(cost.getText().toString()));
        toyData.put("description", description.getText().toString());
        toyData.put("location", location.getText().toString());
        toyData.put("name", name.getText().toString());
        toyData.put("userid", FirebaseHelper.getInstance().getFirebaseUser().getUid());
        toyData.put("image", "");
        return true;
    }

    public void addtoy(View V) {
        if (checkValidations()) {
            progressSpinner.setVisibility(View.VISIBLE);
            FirebaseHelper.getInstance().uploadToyWithData(toyData, selectedImage, this);
        }
    }

    @Override
    public <T> void getFBData(T event) {
        progressSpinner.setVisibility(View.GONE);
        if (event instanceof String) {
            SnackbarHelper.showMessage((String) event, this.findViewById(android.R.id.content));
        } else if (event instanceof Boolean) {
            SnackbarHelper.showMessage("Uploaded Successfully! ", this.findViewById(android.R.id.content));
            Intent i = new Intent(this, UserHomeActivity.class);
            startActivity(i);
            this.finish();
        }
    }

    @Override
    public <T> void updateFBResult(T event) {

    }
}




