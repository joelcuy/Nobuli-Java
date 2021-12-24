package com.example.nobulijava.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nobulijava.R;
import com.example.nobulijava.model.NewsObj;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdminNewsEditActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonSave;
    private TextView textViewDatePosted;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    ImageView imageViewNews;
    ProgressBar progressBar;
    String FOLDER_NAME = "news_image";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri uriSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_edit);

        getSupportActionBar().setTitle("Edit News");



        NewsObj selectedNews = (NewsObj) getIntent().getSerializableExtra("SelectedNews");
        findView();

        editTextTitle.setText(selectedNews.getTitle());
        editTextContent.setText(selectedNews.getContent());
        textViewDatePosted.setText(selectedNews.getDatePosted());

        StorageReference gsReference = storage.getReferenceFromUrl("gs://nobulibot-ysta.appspot.com/" + FOLDER_NAME + "/" + selectedNews.getNewsID());
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdminNewsEditActivity.this).load(uri).into(imageViewNews);
            }
        });


        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        onImageSelected(uri);
                        String imageLocation = "gs://nobulibot-ysta.appspot.com/" + FOLDER_NAME + "/" + selectedNews.getNewsID();
                        mDatabase.child("News").child(selectedNews.getNewsID()).child("imageLocation").setValue(imageLocation);
                        putImageInStorage(selectedNews.getNewsID());
                    }
                });

        imageViewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

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

    public void findView() {
        editTextTitle = findViewById(R.id.editText_adminNewsAdd_title);
        editTextContent = findViewById(R.id.editText_adminNewsAdd_content);
        buttonSave = findViewById(R.id.button_adminNewsAdd_saveNews);
        textViewDatePosted = findViewById(R.id.textView_adminNewsAdd_datePosted);
        imageViewNews = findViewById(R.id.imageView_adminNewsAdd_imageUpload);
        progressBar = findViewById(R.id.progressBar_adminNewsAdd_uploadProgress);
    }

    public void onImageSelected(Uri uri) {
        imageViewNews.setImageURI(uri);
        uriSelectedImage = uri;
    }

    public void putImageInStorage(String quizID) {
        StorageReference quizStorageReference = storage.getReference(FOLDER_NAME).child(quizID);
        if (uriSelectedImage != null) {
//            progressBar.setVisibility(View.VISIBLE);
            quizStorageReference.putFile(uriSelectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = 100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(AdminNewsEditActivity.this, "Please click on photo to select image", Toast.LENGTH_LONG).show();
        }
    }
}