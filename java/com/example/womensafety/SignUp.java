package com.example.womensafety;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button Back_login;
    Button Signup;

    ProgressBar progressBar;
    TextInputLayout FnameText, UsnameText, MailText, PhoneText, PassText;
    //UserDetails userDetails;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Signup = findViewById(R.id.signup_button);
        FnameText = findViewById(R.id.fullname);
        UsnameText = findViewById(R.id.signup_username);
        MailText = findViewById(R.id.email);
        PhoneText = findViewById(R.id.phone_number);
        PassText = findViewById(R.id.signup_password);
        progressBar = findViewById(R.id.progressbar);

        firebaseAuth = FirebaseAuth.getInstance();


        Back_login = findViewById(R.id.back_login_screen);

        Signup.setOnClickListener(this);


        Back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View view) {

        String fullname = FnameText.getEditText().getText().toString().trim();
        String username = UsnameText.getEditText().getText().toString().trim();
        String email = MailText.getEditText().getText().toString().trim();
        String phone_num = PhoneText.getEditText().getText().toString().trim();
        String password = PassText.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone_num) || TextUtils.isEmpty(password)) {

            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
        } else {
            userRegister(fullname, username, email, phone_num, password);
        }
    }


        private void userRegister (String fullname, String username, String email, String
        phone_num, String password){

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String userId = firebaseUser.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put("UserId", userId);
                        hashMap.put("Fullname", fullname);
                        hashMap.put("Username", username);
                        hashMap.put("Email", email);
                        hashMap.put("Phone_num", phone_num);
                        hashMap.put("Password", password);

                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Sign Up is Succesfull", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
