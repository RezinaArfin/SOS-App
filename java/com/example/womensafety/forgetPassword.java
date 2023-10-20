package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class forgetPassword extends AppCompatActivity {
    private TextInputLayout emailText;
    private Button resetpassbtn,backlogin;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailText=findViewById(R.id.email1);
        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressbar);
        resetpassbtn=findViewById(R.id.resetpassword);
        backlogin=findViewById(R.id.backlogin);

        resetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgetPassword.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void resetPassword() {
        String email=emailText.getEditText().getText().toString().trim();
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
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(forgetPassword.this, "Check your email to reset your password.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(forgetPassword.this, "Try Again! Something happened wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}