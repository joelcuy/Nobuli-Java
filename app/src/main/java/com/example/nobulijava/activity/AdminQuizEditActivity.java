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

import com.bumptech.glide.Glide;
import com.example.nobulijava.R;
import com.example.nobulijava.model.QuizObj;
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

public class AdminQuizEditActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText editTextQuestion;
    private Spinner spinnerAnswer;
    private Button buttonSave;
    private ImageView imageViewQuiz;
    ProgressBar progressBar;
    String FOLDER_NAME = "quiz_image";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri uriSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_edit);

        getSupportActionBar().setTitle("Edit Quiz");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        findView();


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quizAnswer_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerAnswer.setAdapter(adapter);


        //Get obj from previous activity
        QuizObj selectedQuiz = (QuizObj) getIntent().getSerializableExtra("SelectedQuiz");

        String[] quizAnswer_array = getResources().getStringArray(R.array.quizAnswer_array);
        Integer position = 0;
        for (String quizAnswer : quizAnswer_array) {
            if (selectedQuiz.getAnswer().equals(Boolean.valueOf(quizAnswer))) {
                break;
            }
            position += 1;
        }

        spinnerAnswer.setSelection(position);
        editTextQuestion.setText(selectedQuiz.getQuestion());

        StorageReference gsReference = storage.getReferenceFromUrl("gs://nobulibot-ysta.appspot.com/" + FOLDER_NAME + "/" + selectedQuiz.getQuizID());
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdminQuizEditActivity.this).load(uri).into(imageViewQuiz);
            }
        });


        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        onImageSelected(uri);
                        putImageInStorage(selectedQuiz.getQuizID());
                    }
                });

        imageViewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionString = editTextQuestion.getText().toString();
                Boolean answerBoolean = Boolean.valueOf(spinnerAnswer.getSelectedItem().toString());

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("question", questionString);
                childUpdates.put("answer", answerBoolean);
                mDatabase.child("Quiz").child(selectedQuiz.getQuizID()).updateChildren(childUpdates);

                finish();
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
            Toast.makeText(AdminQuizEditActivity.this, "Please click on photo to select image", Toast.LENGTH_LONG).show();
            System.out.println("fuck");
        }
    }
}