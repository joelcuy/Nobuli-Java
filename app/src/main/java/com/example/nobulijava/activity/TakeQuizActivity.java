package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                }
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
                for (QuizObj quiz: quizObjArrayList){
                    System.out.println(quiz);
                }
//                //Check for answer when click next
//                if (radioTrue.isChecked()) {
//                    checkAnswer(true);
//                } else if (radioFalse.isChecked()) {
//                    checkAnswer(false);
//                }
//
//                // go to next Question
//                // limiting Question bank range
//                if (currentQuestionIndex <= noOfQuestion) {
//                    currentQuestionIndex
//                            = currentQuestionIndex + 1;
//                    // making buttons
//                    // invisible
//                    if (currentQuestionIndex == noOfQuestion) {
//                        textViewQuestion.setText(getString(
//                                R.string.correct, correct));
//                        buttonNext.setVisibility(
//                                View.GONE);
//                        buttonPrevious.setVisibility(
//                                View.GONE);
//                        radioTrue.setVisibility(
//                                View.GONE);
//                        radioFalse.setVisibility(
//                                View.GONE);
//                        imageQuizImage.setVisibility(View.GONE);
//                        buttonTryAgain.setVisibility(View.VISIBLE);
//                        calcScore();
//                        textViewQuestion.setText("You answered " + correct + " out of 6! \n Here's " + correct + " water droplets for you!");
//                    } else {
//                        updateQuestion();
//                    }
//                }
//
//                break;

            case R.id.button_previous:
//                if (currentQuestionIndex > 0) {
//                    currentQuestionIndex
//                            = (currentQuestionIndex - 1)
//                            % questionBank.length;
//                    updateQuestion();
//                }
                break;

            case R.id.button_tryAgain:
//                Intent intent_current = getIntent();
//                finish();
//                startActivity(intent_current);
                break;

            case R.id.button_mainMenu:
//                Intent intent_current = getIntent();
//                finish();
//                startActivity(intent_current);
                break;
        }
    }

    private void updateQuestion() {
        radioGroupAnswer.clearCheck();

//        questionTextView.setText(
//                questionBank[currentQuestionIndex]
//                        .getAnswerResId());
        // setting the textview with new Question
//        switch (currentQuestionIndex) {
//            case 0:
//                // setting up image for each
//                // Question
//                imageQuizImage.setImageResource(R.drawable.a);
//                break;
//            case 1:
//                imageQuizImage.setImageResource(R.drawable.b);
//                break;
//            case 2:
//                imageQuizImage.setImageResource(R.drawable.c);
//                break;
//            case 3:
//                imageQuizImage.setImageResource(R.drawable.d);
//                break;
//            case 4:
//                imageQuizImage.setImageResource(R.drawable.e);
//                break;
//            case 5:
//                imageQuizImage.setImageResource(R.drawable.f);
//                break;
//        }
    }

//    private void checkAnswer(boolean userChooseCorrect) {
//        boolean answerIsTrue
//                = questionBank[currentQuestionIndex]
//                .isAnswerTrue();
//        // getting correct ans of current Question
//        int toastMessageId;
//        // if ans matches with the
//        // button clicked
//
//        if (userChooseCorrect == answerIsTrue) {
//            toastMessageId = R.string.correct_answer;
//            answerArray[currentQuestionIndex] = 1;
//        } else {
//            // showing toast
//            // message correct
//            toastMessageId = R.string.wrong_answer;
//            answerArray[currentQuestionIndex] = 0;
//        }
//
//        Toast.makeText(Quiz.this, toastMessageId,
//                Toast.LENGTH_SHORT).show();
//    }
//
//    private void calcScore(){
//        for (int i = 0; i<noOfQuestion; i++){
//            correct = correct + answerArray[i];
//        }
//    }
}