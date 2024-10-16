package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nobulijava.R;
import com.example.nobulijava.adapters.NewsAdapter;
import com.example.nobulijava.adapters.QuizAdapter;
import com.example.nobulijava.model.NewsObj;
import com.example.nobulijava.model.QuizObj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class AdminNewsListActivity extends AppCompatActivity {
    private FloatingActionButton FABAddNews;
    private RecyclerView recyclerViewNewsList;

    private DatabaseReference mDatabase;

    private ArrayList<NewsObj> newsObjArrayList;
    private ArrayList<NewsObj> reverseNewsObjArrayList;
    NewsAdapter newsAdapter;

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
        newsObjArrayList = new ArrayList<>();
        reverseNewsObjArrayList = new ArrayList<>();
        newsAdapter = new NewsAdapter(reverseNewsObjArrayList, AdminNewsListActivity.this);
        recyclerViewNewsList.setAdapter(newsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.child("News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsObjArrayList.clear();
                reverseNewsObjArrayList.clear();
                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    NewsObj newsObj = quizSnapshot.getValue(NewsObj.class);
                    newsObj.setNewsID(quizSnapshot.getKey());
                    newsObjArrayList.add(newsObj);
                }
                for (int i = newsObjArrayList.size() - 1; i >= 0; i--) {
                    // Append the elements in reverse order
                    reverseNewsObjArrayList.add(newsObjArrayList.get(i));
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}