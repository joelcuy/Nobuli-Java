package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nobulijava.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class AdminNewsListActivity extends AppCompatActivity {
    FloatingActionButton FABAddNews;
    DatabaseReference mDatabase;
    RecyclerView recyclerViewNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_list);

        getSupportActionBar().setTitle("List of News");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerViewNewsList = findViewById(R.id.recyclerView_newsList);

        FABAddNews = findViewById(R.id.floatingActionButton_adminNewsList_addNews);
        FABAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminNewsListActivity.this, AdminNewsAddActivity.class));
            }
        });
    }
}