package com.e.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Job_Portal extends AppCompatActivity {
    TextView tr,ty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job__portal);
        tr=findViewById(R.id.tr);
        ty=findViewById(R.id.ty);
      tr.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i=new Intent(Job_Portal.this,Employportal.class);
              startActivity(i);

          }
      });
      ty.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent j=new Intent(Job_Portal.this,studentdetails.class);
              startActivity(j);
          }
      });

    }
}
