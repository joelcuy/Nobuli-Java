package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nobulijava.R;
import com.example.nobulijava.adapters.NewsAdapter;
import com.example.nobulijava.model.NewsObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserNewsListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNewsList;

    private DatabaseReference mDatabase;

    private ArrayList<NewsObj> newsObjArrayList;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_news_list);

        getSupportActionBar().setTitle("News");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerViewNewsList = findViewById(R.id.recyclerView_newsList);
                newsObjArrayList = new ArrayList<>();
                newsAdapter = new NewsAdapter(newsObjArrayList, UserNewsListActivity.this);
                recyclerViewNewsList.setAdapter(newsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsObjArrayList.clear();
                for (DataSnapshot quizSnapshot: dataSnapshot.getChildren()) {
                    NewsObj newsObj = quizSnapshot.getValue(NewsObj.class);
                    newsObj.setNewsID(quizSnapshot.getKey());
                    newsObjArrayList.add(newsObj);

                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}