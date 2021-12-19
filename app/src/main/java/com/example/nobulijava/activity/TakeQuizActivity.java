package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobulijava.R;
import com.example.nobulijava.model.QuizObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TakeQuizActivity extends AppCompatActivity implements View.OnClickListener{
    // setting up things
    private RadioButton radioFalse;
    private RadioButton radioTrue;
    private Button buttonNext;
    private Button buttonPrevious;
    private Button buttonTryAgain;
    private Button buttonMainMenu;
    private RadioGroup radioGroupAnswer;
    private TextView textViewQuestion;
    private ImageView imageQuizImage;

    private DatabaseReference mDatabase;

    private ArrayList<QuizObj> quizObjArrayList = new ArrayList<>();
    private ArrayList<Integer> scoreArrayList = new ArrayList<>();
    private Integer currentQuestionIndex = 0;
    private Integer score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // setting up the buttons
        // associated with id
        radioFalse = findViewById(R.id.radio_false);
        radioTrue = findViewById(R.id.radio_true);
        buttonNext = findViewById(R.id.button_next);
        buttonPrevious = findViewById(R.id.button_previous);
        buttonTryAgain = findViewById(R.id.button_tryAgain);
        buttonMainMenu = findViewById(R.id.button_mainMenu);
        radioGroupAnswer = findViewById(R.id.radioGroup_answer);
        textViewQuestion = findViewById(R.id.textView_takeQuiz_question);
        imageQuizImage = findViewById(R.id.image_quizImage);

        radioFalse.setOnClickListener(this);
        radioTrue.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonTryAgain.setOnClickListener(this);
        buttonMainMenu.setOnClickListener(this);

        buttonTryAgain.setVisibility(View.GONE);
        buttonMainMenu.setVisibility(View.GONE);

        mDatabase.child("Quiz").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot quizSnapshot: dataSnapshot.getChildren()) {
                    QuizObj quizObj = quizSnapshot.getValue(QuizObj.class);
                    quizObjArrayList.add(quizObj);
                    scoreArrayList.add(0);
                }
                textViewQuestion.setText(quizObjArrayList.get(0).getQuestion());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_next:
                //Check for answer when click next
                if (radioTrue.isChecked()) {
                    checkAnswer(true);
                } else if (radioFalse.isChecked()) {
                    checkAnswer(false);
                }

                // go to next Question
                currentQuestionIndex = currentQuestionIndex + 1;
                updateQuestion(currentQuestionIndex);
                break;

            case R.id.button_previous:
                currentQuestionIndex = currentQuestionIndex - 1;
                updateQuestion(currentQuestionIndex);
                break;

            case R.id.button_tryAgain:
                startActivity(new Intent(TakeQuizActivity.this, TakeQuizActivity.class));
                finish();
                break;

            case R.id.button_mainMenu:
                startActivity(new Intent(TakeQuizActivity.this, UserDashboardActivity.class));
                finish();
                break;
        }
    }

    private void updateQuestion(Integer currentQuestionIndex) {

        // making buttons invisible
        if (currentQuestionIndex < quizObjArrayList.size()) {
            radioGroupAnswer.clearCheck();
            textViewQuestion.setText(quizObjArrayList.get(currentQuestionIndex).getQuestion());
        } else {
            calcScore();
            textViewQuestion.setText(getString(R.string.score) + " " + score.toString());
//            textViewQuestion.setText(score.toString());
            buttonNext.setVisibility(View.GONE);
            buttonPrevious.setVisibility(View.GONE);
            radioTrue.setVisibility(View.GONE);
            radioFalse.setVisibility(View.GONE);
            imageQuizImage.setVisibility(View.GONE);
            buttonTryAgain.setVisibility(View.VISIBLE);
            buttonMainMenu.setVisibility(View.VISIBLE);
        }
    }

    private void checkAnswer(Boolean userChoice) {
        Boolean answer = quizObjArrayList.get(currentQuestionIndex).getAnswer();
        int toastMessageID;

        if (userChoice == answer) {
            toastMessageID = R.string.correct_answer;
            scoreArrayList.set(currentQuestionIndex, 1);
        } else {
            toastMessageID = R.string.wrong_answer;
            scoreArrayList.set(currentQuestionIndex, 0);
        }

        Toast.makeText(TakeQuizActivity.this, toastMessageID,
                Toast.LENGTH_SHORT).show();
    }

    private void calcScore(){
        for (int i = 0; i < scoreArrayList.size(); i++){
            score = score + scoreArrayList.get(i);
        }
    }
}