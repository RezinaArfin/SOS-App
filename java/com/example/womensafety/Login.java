package com.example.womensafety;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button SignUpScreen;
    TextInputLayout emailText, passwordText;
    Button Login,forgetpassbtn;

    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        Login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressbar);
        firebaseAuth = FirebaseAuth.getInstance();
        SignUpScreen = findViewById(R.id.signup_scr);


        Login.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Homepage.class));
            finish();
        }

        forgetpassbtn=findViewById(R.id.forget_pass);
        forgetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,forgetPassword.class);
                startActivity(i);

            }
        });

        SignUpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onClick(View v) {
        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        if(email.isEmpty()){
            emailText.setError("Email is required.");
            emailText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Please provide valid email address.");
            emailText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordText.setError("Password is required.");
            passwordText.requestFocus();
            return;
        }
        if(password.length()<=8){
            passwordText.setError("Password must be 8 characters.");
            passwordText.requestFocus();
            return;
        }
        userLogin(email, password);

    }

        private void userLogin (String email, String password){

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(Login.this, Homepage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }




