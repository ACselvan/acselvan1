package com.e.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Matrimony extends AppCompatActivity {
    TextView t2,t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Matrimony.this,NR.class);
                startActivity(i);

            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Matrimony.this,Matrimony_info.class);
                // Intent i=new Intent(Matrimony.this,Matrimonydisplay.class);
                startActivity(i);

            }
        });
    }
    }

