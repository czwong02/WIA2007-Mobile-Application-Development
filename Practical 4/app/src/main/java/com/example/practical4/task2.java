package com.example.practical4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class task2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);
        Button BtnSubmit = findViewById(R.id.BtnSubmit);

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnSubmitOnClick(view);
            }
        });
    }

    public void BtnSubmitOnClick(View v) {
        RadioButton CBRed = findViewById(R.id.CBRed);
        RadioButton CBGreen = findViewById(R.id.CBGreen);
        RadioButton CBBlue = findViewById(R.id.CBBlue);

        ConstraintLayout CSLayout = findViewById(R.id.CLayoutCS);

        if(CBRed.isChecked()) CSLayout.setBackgroundColor(Color.RED);
        else if(CBGreen.isChecked()) CSLayout.setBackgroundColor(Color.GREEN);
        else if(CBBlue.isChecked())CSLayout.setBackgroundColor(Color.BLUE);
        else Toast.makeText(task2.this, "Please select a colour", Toast.LENGTH_SHORT).show();
    }
}