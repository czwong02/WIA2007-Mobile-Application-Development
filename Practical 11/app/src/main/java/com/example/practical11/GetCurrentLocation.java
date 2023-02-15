package com.example.practical11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetCurrentLocation extends AppCompatActivity {
    Button BtnHelp;
    TextView TVRescueLat, TVRescueLong, TVRescueM1, TVRescueM2;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_current_location);
        BtnHelp = findViewById(R.id.BtnHelp);
        TVRescueLat = findViewById(R.id.TVRescueLat);
        TVRescueLong = findViewById(R.id.TVRescueLong);
        TVRescueM1 = findViewById(R.id.TVRescueM1);
        TVRescueM2 = findViewById(R.id.TVRescueM2);
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        BtnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(GetCurrentLocation.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    getMeowLocation();
                } else {
                    ActivityCompat.requestPermissions(GetCurrentLocation.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getMeowLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetCurrentLocation.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(GetCurrentLocation.this,
                                        Locale.getDefault());
                                List<Address> address = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1);
                                String messageLat = "Latitude: " + address.get(0).getLatitude();
                                String messageLong = "Longitude: " + address.get(0).getLongitude();
                                TVRescueLat.setText(messageLat);
                                TVRescueLong.setText(messageLong);
                                TVRescueM1.setVisibility(View.VISIBLE);
                                TVRescueM2.setVisibility(View.VISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
