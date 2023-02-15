package com.example.practical4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        RatingBar RateBarFeedback = findViewById(R.id.RateBarFeedback);
        TextView TVRating = findViewById(R.id.TVRating);
        EditText ETFeedback = findViewById(R.id.ETFeedback);
        Button BtnFeedback = findViewById(R.id.BtnFeedback);

        BtnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Thank you for your feedback!";
                if(!ETFeedback.getText().toString().isEmpty())
                    message = message + "Please enjoy your RM5 Cashback: ABC123";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        RateBarFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                TVRating.setText("You have rated " + v);
            }
        });
    }
}