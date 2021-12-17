package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nobulijava.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SetQuizListActivity extends AppCompatActivity {

    private FloatingActionButton fabAddQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_quiz_list);

        fabAddQuiz = findViewById(R.id.floatingActionButton_addQuiz);

        fabAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetQuizListActivity.this, SetQuizDetailsActivity.class));
                finish();
            }
        });
    }


}