package com.e.saivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.play.core.appupdate.AppUpdateManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private Button login;

    private Animation frombottom,frombottom1;
    private LinearLayout image_linear,button_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Checkout.preload(getApplicationContext());
        login=(Button)findViewById(R.id.main_login);

        image_linear=findViewById(R.id.image_linear);
        button_linear=findViewById(R.id.button_linear);
        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        frombottom1=AnimationUtils.loadAnimation(this,R.anim.frombottom1);

        image_linear.setAnimation(frombottom1);
        button_linear.setAnimation(frombottom);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,logIn.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
       
    }
}
