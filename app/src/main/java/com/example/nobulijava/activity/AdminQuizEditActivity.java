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

import java.util.HashMap;
import java.util.Map;

public class AdminQuizEditActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText editTextQuestion;
    Spinner spinnerAnswer;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_edit);

        getSupportActionBar().setTitle("Edit Quiz");

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


        //Get obj from previous activity
        QuizObj selectedQuiz = (QuizObj) getIntent().getSerializableExtra("SelectedQuiz");

        String[] quizAnswer_array = getResources().getStringArray(R.array.quizAnswer_array);
        Integer position = 0;
        for (String quizAnswer: quizAnswer_array){
            if (selectedQuiz.getAnswer().equals(Boolean.valueOf(quizAnswer))){
                break;
            }
            position += 1;
        }

        spinnerAnswer.setSelection(position);
        editTextQuestion.setText(selectedQuiz.getQuestion());


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
}