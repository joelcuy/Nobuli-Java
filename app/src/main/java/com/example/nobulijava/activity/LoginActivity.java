package com.example.nobulijava.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView toSignup;
    Button buttonLoginAccount;
    EditText editTextEmail;
    EditText editTextPassword;

    public static FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private UserObj user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_500));
        }

        toSignup = findViewById(R.id.loginScreen_textView_linkSignup);
        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signup);
                finish();
            }
        });

        buttonLoginAccount = findViewById(R.id.loginScreen_button_login);
        editTextEmail = findViewById(R.id.loginScreen_editText_email);
        editTextPassword = findViewById(R.id.loginScreen_editText_password);
        buttonLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLoginAccount.setEnabled(false);
                String emailString = editTextEmail.getText().toString();
                String passwordString = editTextPassword.getText().toString();
                logIn(emailString, passwordString);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            mDatabase.child("User").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    user = dataSnapshot.getValue(UserObj.class);
                    if (user.getIsAdmin()) {
                        startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    private void logIn(String email, String password) {


        if (email.trim().equals("") || password.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Email or Passwords cannot be blank!", Toast.LENGTH_SHORT).show();
            buttonLoginAccount.setEnabled(true);
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            buttonLoginAccount.setEnabled(true);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                mDatabase.child("User").child(mAuth.getCurrentUser().getUid()).child("isAdmin").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        //Decides if is Admin or regular user
                                        if (task.getResult().getValue().toString().equals("false")) {
                                            startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                                        } else {
                                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                        }
                                        finish();
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Incorrect password or email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}