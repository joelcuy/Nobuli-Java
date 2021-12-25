package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nobulijava.R;
import com.example.nobulijava.model.NewsObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserNewsDetailsActivity extends AppCompatActivity {
    private ImageView imageViewNews;
    private TextView textViewTitle;
    private TextView textViewDatePosted;
    private TextView textViewContent;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    String FOLDER_NAME = "news_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_news_details);

        findViewById();

        getSupportActionBar().hide();

        NewsObj selectedNews = (NewsObj) getIntent().getSerializableExtra("SelectedNews");

        textViewTitle.setText(selectedNews.getTitle());
        textViewContent.setText(selectedNews.getContent());
        textViewDatePosted.setText(selectedNews.getDatePosted().substring(0,11));


        StorageReference gsReference = storage.getReferenceFromUrl("gs://nobulibot-ysta.appspot.com/" + FOLDER_NAME + "/" + selectedNews.getNewsID());
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(UserNewsDetailsActivity.this).load(uri).into(imageViewNews);
            }
        });


    }

    private void findViewById(){
        imageViewNews = findViewById(R.id.imageView_newsDetails_image);
        textViewTitle = findViewById(R.id.textView_newsDetails_title);
        textViewDatePosted = findViewById(R.id.textView_newsDetails_datePosted);
        textViewContent = findViewById(R.id.textView_newsDetails_content);
    }
}