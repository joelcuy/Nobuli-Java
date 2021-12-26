package com.example.nobulijava.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nobulijava.R;
import com.example.nobulijava.model.ReportRecipientObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class UserReportActivity extends AppCompatActivity {
    private EditText editTextEmailSubject;
    private EditText editTextEmailBody;
    private Button buttonSendEmail;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> recipientEmailArrayList = new ArrayList<>();
    String[] recipientEmailArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        getSupportActionBar().setTitle("Report");

        findViewById();

        mDatabase.child("Report").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipientEmailArrayList.clear();
                for (DataSnapshot reportRecipientSnapshot: snapshot.getChildren()){
                    ReportRecipientObj reportRecipientObj = reportRecipientSnapshot.getValue(ReportRecipientObj.class);
                    recipientEmailArrayList.add(reportRecipientObj.getEmail());
                }
                recipientEmailArray = new String[recipientEmailArrayList.size()];
                for (int i = 0; i < recipientEmailArrayList.size(); i++) {
                    recipientEmailArray[i] = recipientEmailArrayList.get(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail(recipientEmailArray,
                        editTextEmailSubject.getText().toString(),
                        editTextEmailBody.getText().toString()
                );
            }
        });
    }

    public void findViewById() {
        editTextEmailSubject = findViewById(R.id.editText_userReport_emailSubject);
        editTextEmailBody = findViewById(R.id.editText_userReport_emailBody);
        buttonSendEmail = findViewById(R.id.button_userReport_sendEmail);
    }

    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(intent);
    }
}