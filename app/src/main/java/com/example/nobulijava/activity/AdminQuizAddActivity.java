package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.nobulijava.R;
import com.example.nobulijava.model.QuizObj;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminQuizAddActivity extends AppCompatActivity{
    private DatabaseReference mDatabase;
    EditText editTextQuestion;
    Spinner spinnerAnswer;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_add);

        getSupportActionBar().setTitle("Add Quiz");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        spinnerAnswer = (Spinner) findViewById(R.id.spinner_answer);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quizAnswer_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerAnswer.setAdapter(adapter);

        buttonSave = (Button) findViewById(R.id.button_saveQuiz);
        editTextQuestion = (EditText) findViewById(R.id.editText_question);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionString = editTextQuestion.getText().toString();
                Boolean answerBoolean = Boolean.valueOf(spinnerAnswer.getSelectedItem().toString());


                QuizObj newQuiz = new QuizObj(questionString, answerBoolean);
                mDatabase.child("Quiz").push().setValue(newQuiz);
                finish();
            }
        });
    }
}