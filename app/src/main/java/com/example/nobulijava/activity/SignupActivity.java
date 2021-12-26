package com.example.nobulijava.activity;

import static com.example.nobulijava.activity.LoginActivity.mAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobulijava.R;
import com.example.nobulijava.model.UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    TextView textViewToLogin;
    Button buttonCreateAccount;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_500));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textViewToLogin = findViewById(R.id.signupScreen_textView_linkLogin);
        textViewToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        buttonCreateAccount = findViewById(R.id.signupScreen_button_createAccount);
        editTextEmail = findViewById(R.id.signupScreen_editText_email);
        editTextPassword = findViewById(R.id.signupScreen_editText_password);
        editTextConfirmPassword = findViewById(R.id.signupScreen_editText_confirmPassword);


        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCreateAccount.setEnabled(false);
                String emailString = editTextEmail.getText().toString();
                String passwordString = editTextPassword.getText().toString();
                String confirmPasswordString = editTextConfirmPassword.getText().toString();
                try {
                    if (emailString.trim().equals("") || passwordString.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Email or Passwords cannot be blank!", Toast.LENGTH_SHORT).show();
                    }
                    if (passwordString.equals(confirmPasswordString)) {
                        signUp(emailString, passwordString);
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        buttonCreateAccount.setEnabled(true);
                        if (task.isSuccessful()) {
                            // Sign in success, go to user dashboard
                            UserObj newUser = new UserObj(email, false, mAuth.getCurrentUser().getUid());
                            //TODO write to database
                            mDatabase.child("User").child(mAuth.getCurrentUser().getUid()).setValue(newUser);
                            startActivity(new Intent(SignupActivity.this, UserDashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}