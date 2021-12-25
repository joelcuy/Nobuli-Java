package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nobulijava.R;

public class UserReportActivity extends AppCompatActivity {
    private EditText editTextEmailSubject;
    private EditText editTextEmailBody;
    private Button buttonSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        getSupportActionBar().setTitle("Report");

        findViewById();

        String[] recipientAddress = {"gjoelcheah@gmail.com"};

        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail(recipientAddress,
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