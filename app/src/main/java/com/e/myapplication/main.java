package com.e.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import android.os.Bundle;

public class main extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView mTvEmail, mTvLogout;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mAuth = FirebaseAuth.getInstance();


        mTvLogout = findViewById(R.id.txtBtnLogout);
        mTvEmail = findViewById(R.id.tvEmail);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
//No one signed in
            startActivity(new Intent(this, signup.class));
            this.finish();
        }else{
//User logged in
            mTvEmail.setText(currentUser.getEmail());
        }



        mTvLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(main.this, signup.class));
                main.this.finish();

            }
        });


    }
}
