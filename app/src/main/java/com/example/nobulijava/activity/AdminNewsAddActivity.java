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

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminNewsAddActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private TextView textViewDatePosted;
    private Button buttonSaveNews;
    private DateTimeFormatter dateTimeFormatter;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_add);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextTitle = findViewById(R.id.editText_adminNewsAdd_title);
        editTextContent = findViewById(R.id.editText_adminNewsAdd_content);
        textViewDatePosted = findViewById(R.id.textView_adminNewsAdd_datePosted);
        buttonSaveNews = findViewById(R.id.button_adminNewsAdd_saveNews);

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();


        textViewDatePosted.setText(dateTimeFormatter.format(currentDateTime));

        buttonSaveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = editTextTitle.getText().toString();
                String contentString = editTextContent.getText().toString();
                String datePostedString = textViewDatePosted.getText().toString();

                NewsObj newNews = new NewsObj(titleString, contentString, datePostedString);
                mDatabase.child("News").push().setValue(newNews);
                finish();
            }
        });


    }
}