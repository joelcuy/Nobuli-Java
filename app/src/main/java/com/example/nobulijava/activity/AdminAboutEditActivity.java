package com.example.nobulijava.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nobulijava.R;
import com.example.nobulijava.model.AboutCyberbullyObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminAboutEditActivity extends AppCompatActivity {
    private EditText editTextAboutCyberbully;
    private EditText editTextContact;
    private Button buttonSaveAbout;
    private LinearLayout linearLayout;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_about_edit);

        getSupportActionBar().setTitle("Set About Cyberbullying");

        findViewById();

        mDatabase.child("About").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AboutCyberbullyObj aboutCyberbullyObj = snapshot.getValue(AboutCyberbullyObj.class);
                editTextAboutCyberbully.setText(aboutCyberbullyObj.getAboutCyberbully());
                        editTextContact.setText(aboutCyberbullyObj.getContact());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonSaveAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aboutCyberbullyString = editTextAboutCyberbully.getText().toString().trim();
                String contactString = editTextContact.getText().toString().trim();

                HashMap<String, String> aboutChildMap = new HashMap<>();
                aboutChildMap.put("aboutCyberbully", aboutCyberbullyString);
                aboutChildMap.put("contact", contactString);
                mDatabase.child("About").setValue(aboutChildMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdminAboutEditActivity.this, "Successfully Updated Database", Toast.LENGTH_LONG).show();
//                        Snackbar.make(linearLayout, "Successfully Updated Database", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void findViewById() {
        editTextAboutCyberbully = findViewById(R.id.editText_adminAbout_aboutCyberbullying);
        editTextContact = findViewById(R.id.editText_adminAbout_contact);
        buttonSaveAbout = findViewById(R.id.button_adminAbout_saveAbout);
        linearLayout = findViewById(R.id.linearLayout_adminAbout);
    }
}