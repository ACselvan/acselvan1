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

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    private String currentDateandTime;
    private SimpleDateFormat sdf;
    private Button login;
    private ImageView imageView;
    private NotificationManagerCompat notificationManagerCompat;
    private Animation frombottom,frombottom1;
    private LinearLayout image_linear,button_linear;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Checkout.preload(getApplicationContext());
        login=(Button)findViewById(R.id.main_login);
        imageView=findViewById(R.id.imageView);
        image_linear=findViewById(R.id.image_linear);
        button_linear=findViewById(R.id.button_linear);
        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        frombottom1=AnimationUtils.loadAnimation(this,R.anim.frombottom1);

        image_linear.setAnimation(frombottom1);
        button_linear.setAnimation(frombottom);
        /*YoYo.with(Techniques.RollOut)
                .duration(700)
                .repeat(1)
                .playOn(imageView);
        YoYo.with(Techniques.RollIn)
                .duration(700)
                .repeat(1)
                .playOn(login);*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,logIn.class));
            }
        });
    }
    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.technotribe);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Merchant Name");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
           // options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", "500");

            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
    private void matrimonynotification()
    {
//TODO Must write this code if integrating One Tap payments

    }

    @Override
    protected void onStart() {
        super.onStart();
       
    }
}
