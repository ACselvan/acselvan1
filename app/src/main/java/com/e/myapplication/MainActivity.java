package com.e.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
Button button;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    button=findViewById(R.id.button);
        login=(Button)findViewById(R.id.main_login);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
Intent i=new Intent(MainActivity.this,Main2Activity.class);
startActivity(i);
        }
    });
       //arun issaki raj
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,logIn.class));
            }
        });
    }

}
