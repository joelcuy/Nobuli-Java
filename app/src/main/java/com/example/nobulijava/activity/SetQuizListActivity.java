package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nobulijava.R;
import com.example.nobulijava.adapters.QuizAdapter;
import com.example.nobulijava.model.QuizObj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SetQuizListActivity extends AppCompatActivity {

    private FloatingActionButton FABAddQuiz;
    private RecyclerView recyclerViewQuizList;

    private DatabaseReference mDatabase;

    private ArrayList<QuizObj> quizObjArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_quiz_list);

        FABAddQuiz = findViewById(R.id.floatingActionButton_addQuiz);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        FABAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetQuizListActivity.this, SetQuizDetailsActivity.class));
            }
        });
        mDatabase.child("Quiz").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot quizSnapshot: dataSnapshot.getChildren()) {
                    QuizObj quizObj = quizSnapshot.getValue(QuizObj.class);
                    quizObjArrayList.add(quizObj);
                    recyclerViewQuizList = findViewById(R.id.recyclerView_quizList);
                    QuizAdapter quizAdapter = new QuizAdapter(quizObjArrayList);
                    recyclerViewQuizList.setAdapter(quizAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }


}