package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nobulijava.R;

public class UserReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        getSupportActionBar().setTitle("Report");

    }
}