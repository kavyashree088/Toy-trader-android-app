package com.example.toytrader;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetToyLocation extends AsyncTask<Object,String ,String> {

    private String googleplaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        GetLocationURL getUrl = new GetLocationURL();
        try {
            googleplaceData = getUrl.ReadTheURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleplaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearByList = null;
        LocationParser dataParser = new LocationParser();
        nearByList = dataParser.parse(s);

        DisplayNearByPlaces(nearByList);
    }

    private void DisplayNearByPlaces(List<HashMap<String, String>> nearByList) {

        for (int i = 0; i < nearByList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleNearByPlace = nearByList.get(i);

            double latitude = Double.parseDouble(googleNearByPlace.get("lat"));
            double longitude = Double.parseDouble(googleNearByPlace.get("lng"));

            LatLng latLng = new LatLng(latitude, longitude);

            markerOptions.position(latLng);
            // markerOptions.title(nameofPlace+ ": "+vicinity+" :"+ratings);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
    }
}
