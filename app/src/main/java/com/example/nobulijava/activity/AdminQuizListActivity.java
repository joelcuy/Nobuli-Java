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

public class AdminQuizListActivity extends AppCompatActivity {

    private FloatingActionButton FABAddQuiz;
    private RecyclerView recyclerViewQuizList;

    private DatabaseReference mDatabase;

    private ArrayList<QuizObj> quizObjArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_list);

        getSupportActionBar().setTitle("List of Quizzes");

        FABAddQuiz = findViewById(R.id.floatingActionButton_adminQuizList_addQuiz);
        recyclerViewQuizList = findViewById(R.id.recyclerView_adminQuizList_quizList);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        FABAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminQuizListActivity.this, AdminQuizAddActivity.class));
            }
        });
        mDatabase.child("Quiz").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizObjArrayList = new ArrayList<>();
                for (DataSnapshot quizSnapshot: dataSnapshot.getChildren()) {
                    QuizObj quizObj = quizSnapshot.getValue(QuizObj.class);
                    quizObj.setQuizID(quizSnapshot.getKey());
                    quizObjArrayList.add(quizObj);

                }
                QuizAdapter quizAdapter = new QuizAdapter(quizObjArrayList, AdminQuizListActivity.this);
                recyclerViewQuizList.setAdapter(quizAdapter);
                //TODO when delete item, recyclerview scrolls back to top
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });








    }


}