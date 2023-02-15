package com.example.practical11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button BtnMeow, BtnFindLocMap, BtnNearbyPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnMeow = findViewById(R.id.BtnMeow);
        BtnFindLocMap = findViewById(R.id.BtnFindLocMap);
        BtnNearbyPlaces = findViewById(R.id.BtnNearbyPlaces);

        BtnMeow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetCurrentLocation.class);
                startActivity(intent);
            }
        });

        BtnFindLocMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        BtnNearbyPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindNearbyPlaces.class);
                startActivity(intent);
            }
        });
    }
}