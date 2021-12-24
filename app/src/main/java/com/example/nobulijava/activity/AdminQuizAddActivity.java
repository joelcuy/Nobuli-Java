package com.example.nobulijava.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nobulijava.R;
import com.example.nobulijava.model.QuizObj;
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

public class AdminQuizAddActivity extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    ImageView imageViewQuiz;
    ProgressBar progressBar;
    String FOLDER_NAME = "quiz_image";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri uriSelectedImage;
    EditText editTextQuestion;
    Spinner spinnerAnswer;
    Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_add);

        getSupportActionBar().setTitle("Add Quiz");
        findView();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quizAnswer_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerAnswer.setAdapter(adapter);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionString = editTextQuestion.getText().toString();
                Boolean answerBoolean = Boolean.valueOf(spinnerAnswer.getSelectedItem().toString());


                QuizObj newQuiz = new QuizObj(questionString, answerBoolean);
                DatabaseReference pushedQuizRef = mDatabase.child("Quiz").push();
                String quizID = pushedQuizRef.getKey();

                pushedQuizRef.setValue(newQuiz).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        putImageInStorage(quizID);
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

        imageViewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
    }

    public void findView() {
        spinnerAnswer = (Spinner) findViewById(R.id.spinner_adminQuizAdd_answer);
        buttonSave = (Button) findViewById(R.id.button_adminQuizAdd_saveQuiz);
        editTextQuestion = (EditText) findViewById(R.id.editText_adminQuizAdd_question);
        imageViewQuiz = findViewById(R.id.imageView_adminQuizAdd_imageUpload);
        progressBar = findViewById(R.id.progressBar_adminQuizAdd_uploadProgress);
    }

    public void onImageSelected(Uri uri) {
        imageViewQuiz.setImageURI(uri);
        uriSelectedImage = uri;
    }

    public void putImageInStorage(String quizID) {
        StorageReference quizStorageReference = storage.getReference(FOLDER_NAME).child(quizID);
        if (uriSelectedImage != null) {
            progressBar.setVisibility(View.VISIBLE);
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
            Toast.makeText(AdminQuizAddActivity.this, "Please click on photo to select image", Toast.LENGTH_LONG).show();
            System.out.println("fuck");
        }
    }
}