package com.e.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class studentdetails extends AppCompatActivity {
    EditText p1,p2,p3,p4;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentdetails);

        p1=findViewById(R.id.p1);
        p2=findViewById(R.id.p2);
        p3=findViewById(R.id.p3);
        p4=findViewById(R.id.p4);
        b1=findViewById(R.id.b1);

    }
}
