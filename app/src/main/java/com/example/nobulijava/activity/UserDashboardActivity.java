package com.example.nobulijava.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.nobulijava.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserDashboardActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);


        Button buttonAbout = findViewById(R.id.button_aboutCyberbully);
        Button buttonChatBot = findViewById(R.id.button_chatBot);
        Button buttonQuiz = findViewById(R.id.button_quiz);
        Button buttonNews = findViewById(R.id.button_news);
        Button buttonReport = findViewById(R.id.button_report);


        buttonAbout.setOnClickListener(this);
        buttonChatBot.setOnClickListener(this);
        buttonQuiz.setOnClickListener(this);
        buttonNews.setOnClickListener(this);
        buttonReport.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserDashboardActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_aboutCyberbully:
                startActivity(new Intent(UserDashboardActivity.this, AboutActivity.class));
                break;

            case R.id.button_chatBot:
                startActivity(new Intent(UserDashboardActivity.this, ChatBotActivity.class));
                break;

            case R.id.button_quiz:
                startActivity(new Intent(UserDashboardActivity.this, TakeQuizActivity.class));
                break;

            case R.id.button_news:
                startActivity(new Intent(UserDashboardActivity.this, ReadNewsListActivity.class));
                break;

            case R.id.button_report:
                startActivity(new Intent(UserDashboardActivity.this, ReportActivity.class));
                break;
        }
    }
}