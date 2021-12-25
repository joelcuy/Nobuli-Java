package com.example.nobulijava.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobulijava.R;
import com.example.nobulijava.model.NewsObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminNewsAddActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private TextView textViewDatePosted;
    private Button buttonSaveNews;
    private DateTimeFormatter dateTimeFormatter;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    ImageView imageViewNews;
    ProgressBar progressBar;
    String FOLDER_NAME = "news_image";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri uriSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_add);
        getSupportActionBar().setTitle("Add News");
        findView();



        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        textViewDatePosted.setText(dateTimeFormatter.format(currentDateTime));

        buttonSaveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = editTextTitle.getText().toString();
                String contentString = editTextContent.getText().toString().trim() + "\n";
                String datePostedString = textViewDatePosted.getText().toString();

                NewsObj newNews = new NewsObj(titleString, contentString, datePostedString);
                DatabaseReference pushedNewsRef = mDatabase.child("News").push();
                String newsID = pushedNewsRef.getKey();

                String imageLocation = "gs://nobulibot-ysta.appspot.com/" + FOLDER_NAME + "/" + newNews.getNewsID();
                pushedNewsRef.setValue(newNews).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        putImageInStorage(newsID);

                        pushedNewsRef.child("imageLocation").setValue(imageLocation);
                    }
                });
                finish();
            }
        });

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        onImageSelected(uri);
                    }
                });

        imageViewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
    }

    public void findView(){
        editTextTitle = findViewById(R.id.editText_adminNewsAdd_title);
        editTextContent = findViewById(R.id.editText_adminNewsAdd_content);
        textViewDatePosted = findViewById(R.id.textView_adminNewsAdd_datePosted);
        buttonSaveNews = findViewById(R.id.button_adminNewsAdd_saveNews);
        imageViewNews = findViewById(R.id.imageView_adminNewsAdd_imageUpload);
        progressBar = findViewById(R.id.progressBar_adminNewsAdd_uploadProgress);
    }

    public void onImageSelected(Uri uri) {
        imageViewNews.setImageURI(uri);
        uriSelectedImage = uri;
    }

    public void putImageInStorage(String newsID) {
        StorageReference newsStorageReference = storage.getReference(FOLDER_NAME).child(newsID);
        if (uriSelectedImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            newsStorageReference.putFile(uriSelectedImage)
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
            Toast.makeText(AdminNewsAddActivity.this, "Please click on photo to select image", Toast.LENGTH_LONG).show();
            System.out.println("fuck");
        }
    }
}