package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import android.os.Bundle;

public class signup2 extends AppCompatActivity {

    private EditText mEtEmail, mEtPassword, mEtConfirmPassword;
    private TextView mTvSignUp;
    private Context Context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        mEtEmail = findViewById(R.id.etEmaill);
        mEtPassword = findViewById(R.id.etPasss);
        mEtConfirmPassword = findViewById(R.id.etPassConfirm);

        mTvSignUp = findViewById(R.id.txtBtnSignUp);


        mTvSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = mEtPassword.getText().toString();
                String confirmPass = mEtConfirmPassword.getText().toString();
                String email = mEtEmail.getText().toString();



                if (!pass.equals(confirmPass)) {
                    Toast.makeText( signup2.this,"Password Doesn't match", Toast.LENGTH_LONG).show();
                } else {
                 //   mProgressBar.setVisibility(View.VISIBLE);
                   // mRlFading.setVisibility(View.VISIBLE);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(signup2.this, "SignUp Complete", Toast.LENGTH_SHORT)
                                                .show();
                                        startActivity(new Intent(signup2.this, main.class));
                                        finish();
                                    } else {
                                        Toast.makeText( signup2.this,task.getResult().toString(), Toast.LENGTH_LONG).
                                                show();
                                    }

                                }
                            });
                }


            }
        });


    }
}
