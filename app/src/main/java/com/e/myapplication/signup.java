package com.e.myapplication;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity  {

    private TextView mBtnSignIn;
    private EditText mEtEmail, mEtPassword;
    private RelativeLayout root, mRlSignUp, mRlFadingLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mBtnSignIn = findViewById(R.id.tin);
        mEtPassword = findViewById(R.id.etPass);
        mEtEmail = findViewById(R.id.etEmail);


        mBtnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                mProgressBar.setVisibility(View.INVISIBLE);
                                mRlFadingLayout.setVisibility(View.INVISIBLE);

                                if (task.isSuccessful()) {
                                    startActivity(new Intent(signup.this, main.class));
                                    finish();
                                } else {
                                    Toast.makeText(signup.this,"the "+task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}
