package com.example.practical4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class task1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);

        final Button BtnCalculate = findViewById(R.id.BtnCalculate);
        BtnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText Num1 = findViewById(R.id.ETNum1);
                EditText Num2 = findViewById(R.id.ETNum2);
                TextView TVResult = findViewById(R.id.TVResult);
                if(TextUtils.isEmpty(Num1.getText().toString()) || TextUtils.isEmpty(Num2.getText().toString())) {
                    Toast.makeText(task1.this, "Please enter the number into the blank!!", Toast.LENGTH_LONG).show();
                } else {
                    Double Result = Double.parseDouble(Num1.getText().toString()) + Double.parseDouble(Num2.getText().toString());
                    TVResult.setText(Double.toString(Result));
                }
            }
        });
    }
}