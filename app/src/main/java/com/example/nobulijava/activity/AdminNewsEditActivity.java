package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nobulijava.R;
import com.example.nobulijava.model.NewsObj;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminNewsEditActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonSave;
    private TextView textViewDatePosted;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_edit);

        getSupportActionBar().setTitle("Edit News");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        NewsObj selectedNews = (NewsObj) getIntent().getSerializableExtra("SelectedNews");

        editTextTitle = findViewById(R.id.editText_adminNewsAdd_title);
        editTextContent = findViewById(R.id.editText_adminNewsAdd_content);
        buttonSave = findViewById(R.id.button_adminNewsAdd_saveNews);
        textViewDatePosted = findViewById(R.id.textView_adminNewsAdd_datePosted);

        editTextTitle.setText(selectedNews.getTitle());
        editTextContent.setText(selectedNews.getContent());
        textViewDatePosted.setText(selectedNews.getDatePosted());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = editTextTitle.getText().toString();
                String contentString = editTextContent.getText().toString();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("title", titleString);
                childUpdates.put("content", contentString);
                mDatabase.child("News").child(selectedNews.getNewsID()).updateChildren(childUpdates);
                finish();
            }
        });
    }
}