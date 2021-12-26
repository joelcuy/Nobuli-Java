package com.example.nobulijava.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.nobulijava.R;
import com.example.nobulijava.model.AboutCyberbullyObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class UserAboutActivity extends AppCompatActivity {

    private TextView textViewAboutCyberbully;
    private TextView textViewContact;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_about);

        getSupportActionBar().setTitle("About Cyberbullying");

        findViewById();

        mDatabase.child("About").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AboutCyberbullyObj aboutCyberbullyObj = snapshot.getValue(AboutCyberbullyObj.class);
                textViewAboutCyberbully.setText(aboutCyberbullyObj.getAboutCyberbully());
                textViewContact.setText(aboutCyberbullyObj.getContact());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findViewById() {
        textViewAboutCyberbully = findViewById(R.id.textView_userAbout_aboutCyberbullying);
        textViewContact = findViewById(R.id.textView_userAbout_contact);
    }
}