package com.example.practical11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class FindNearbyPlaces extends AppCompatActivity {
    Spinner SPCategory;
    Button BtnFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;
    FloatingActionButton FABRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_nearby_places);
        //Connect with the widget in the frontend UI file
        SPCategory = findViewById(R.id.SPCategory);
        BtnFind = findViewById(R.id.BtnFind);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.FragMap);
        FABRestaurant = findViewById(R.id.FABRestaurant);
//Prepare the place category list
        String[] placeCategory = {"Restaurant", "School", "Mosque", "Hospital"};
        SPCategory.setAdapter(new ArrayAdapter<>(FindNearbyPlaces.this,
                android.R.layout.simple_spinner_dropdown_item, placeCategory));
        //Initialize fusedLocationProviderClient to communicate with the location Services API
                fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        //Check if permission is granted or not
        //Automatically when get into this activity because we are in MapView
        if (ActivityCompat.checkSelfPermission(FindNearbyPlaces.this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else { //if not granted, request for permission to user
            ActivityCompat.requestPermissions(FindNearbyPlaces.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }
        //Setup OnClickListener for the BtnFind
        BtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a variable to hold the selected item position of Spinner
                int SPLoc = SPCategory.getSelectedItemPosition();
                //Create the variable to hold the string that will be send to the
                //API later to retrieve the nearby places
                //location is obtained when "getCurrentLocation()" is called just now
                //radius is the size of the area counting from your current location
                //type is the type listed in spinner based on user selection ("restaurant", ...)
                //key is the API key in the Google Cloud Developer Platform (Get Public Key)
                //More information: https://developers.google.com/maps/documentation/places/web-service/search-nearby
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                        + "?location=" + currentLat + "," + currentLong
                        + "&radius=5000"
                        + "&type=" + placeCategory[SPLoc].toLowerCase()
                        + "&key=" + getResources().getString(R.string.google_maps_key)
                        + "&sensor=true";
                //Log.i("log", url);
                // Call to execute PlaceTask
                new PlaceTask().execute(url);
            }
        });
        //Same with previous onClickListener but this is on Floating Action Button
        FABRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                        + "?location=" + currentLat + "," + currentLong
                        + "&radius=5000"
                        + "&type=restaurant"
                        + "&key=" + getResources().getString(R.string.google_maps_key)
                        + "&sensor=true";
                //Log.i("log", url);
                new PlaceTask().execute(url);
            }
        });
    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FindNearbyPlaces.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            return;
        }
        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map = googleMap;
                            LatLng curLatLng = new LatLng(currentLat, currentLong);
                            googleMap.addMarker(new MarkerOptions()
                                    .position(curLatLng)
                                    .title("Current Location")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLong), 10));
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==99){
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    //Open Internet Connection
    //Execute the Url sent to this method
    //Received the information and append the data
    //Return back to ParserTask
    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while((line = reader.readLine())!= null){
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;
    }
    //Working with the API in background
    //Calling the downloadUrl and send the returns to ParserTask
    private class PlaceTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            new ParserTask().execute(s);
        }
    }
    //Do Parsing Task in background
    //Call JsonParser to execute the parsing steps
    //Hashmap is returned containing the clean key/value to continue working onPostExecute
    private class ParserTask extends AsyncTask<String,Integer,
            List<HashMap<String,String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String,String>> mapList = null;
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }
        //Get back the cleaned value
        //And placed the marker on nearby places
        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //super.onPostExecute(hashMaps);
            map.clear();
            for (int i = 0; i < hashMaps.size(); i++){
                HashMap<String,String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                LatLng latlng = new LatLng(lat, lng);
                MarkerOptions options = new MarkerOptions();
                options.position(latlng);
                options.title(name);
                map.addMarker(options);
            }
        }
    }
}