package com.example.practical9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String USER_FILE_NAME = "user_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(MainActivity.this, MainActivity.class);  //your class
                startActivity(i);
                finish();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        File UserFile = new File(context.getFilesDir(), USER_FILE_NAME);

        if (UserFile.exists()) {
            FragmentContainerView FCVCreateUser = findViewById(R.id.FCVCreateUser);
            FCVCreateUser.setVisibility(View.GONE);

            String username = readUserName();
            TextView TVWelcome = findViewById(R.id.textView3);
            TVWelcome.setText("Welcome Mr " + username);
        }
    }

    protected String readUserName() {
        String FileContent="";
        FileInputStream FIS = null;
        try {
            FIS = getApplicationContext().openFileInput(USER_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(FIS, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while(line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            FileContent = stringBuilder.toString();
        }
        return FileContent;
    }

    public void BtnGalleryOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        startActivity(intent);
    }

    public void BtnSettingsOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}