package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;



public class Main2Activity extends AppCompatActivity implements PaymentResultListener {
TextView t1,t2,t3;

    DatabaseReference databaseReference,databaseReference1;
    private  user user1;
    private Query query,query1,query2;
    private String currentDateandTime,matrimony;
    private SimpleDateFormat sdf,sdf3;
    String a1,verify="";
    private List<images> slideLists;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

   LinearLayout matrimony_linear,business_linear,job_linear;
    private images up;
    private mat_fav up_fav;
    ImageView matrimony_image,Business_image,job_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Checkout.preload(getApplicationContext());
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        business_linear = findViewById(R.id.linear_business);
        matrimony_linear = findViewById(R.id.linear_matrimony);
        job_linear = findViewById(R.id.linear_job);
        matrimony_image = findViewById(R.id.marriage_image);
        Business_image = findViewById(R.id.busiess_catelogue_image);
        job_image = findViewById(R.id.job_image);
         DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference("homepage_images"),per=FirebaseDatabase.getInstance().getReference("Matrimony_Details"),use=FirebaseDatabase.getInstance().getReference("user"),databaseReferenc5=FirebaseDatabase.getInstance().getReference("mat_fav");
        slideLists = new ArrayList<>();
        sharedPreferences = getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        a1 = sharedPreferences.getString("phonenumber", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        getSupportActionBar().setTitle("Home");
        databaseReference.keepSynced(true);
        query = databaseReference.orderByChild("mobile").equalTo(a1);
        query1 = databaseReference.orderByChild("mobile").equalTo(a1);
        editor = sharedPreferences.edit();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Matrimony_Details");
        databaseReference1.keepSynced(true);
        query2 = databaseReference1.orderByChild("cellno").equalTo(a1);
        notification();
        slide();
        per.keepSynced(true);
        use.keepSynced(true);
        mDatabase.keepSynced(true);
        databaseReferenc5.keepSynced(true);


        business_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, Business_catalogue.class);
                startActivity(i);

            }
        });

        matrimony_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sdf = new SimpleDateFormat("yyyyMMdd");
                currentDateandTime = sdf.format(new Date());

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            user1 = dataSnapshot1.getValue(user.class);
                            verify = user1.getMat_exp();
                            //Toast.makeText(getApplicationContext(),user1.getMat_exp(),Toast.LENGTH_SHORT).show();
                            if (Integer.parseInt(currentDateandTime) <= Integer.parseInt(user1.mat_exp)) {
                                success();

                            } else if (verify.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                                builder.setTitle("only prmium members");
                                builder.setMessage("click payup to pay fee for entering matrimony");
                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startPayment();
                                    }
                                });
                                builder.create().show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                                builder.setTitle("premium membership expired");
                                builder.setMessage("finish the payment for continue matrimony service");
                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startPayment();
                                    }
                                });
                                builder.create().show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        job_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Main2Activity.this, Work_Portal.class);
                startActivity(k);


            }
        });
/*        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(Main2Activity.this,BusinessPortal.class);
                startActivity(i1);
            }
        });


 */
    }
private void slide()
{

    FirebaseDatabase.getInstance().getReference("homepage_images").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                up = dataSnapshot1.getValue(images.class);
                Picasso.with(getApplicationContext()).load(up.getImage2()).into(job_image);
                Picasso.with(getApplicationContext()).load(up.getImage1()).into(matrimony_image);
                Picasso.with(getApplicationContext()).load(up.getImage()).into(Business_image);

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
/*
private void slide1()
{
    FirebaseDatabase.getInstance().getReference("homepage_images").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                up = dataSnapshot1.getValue(images.class);


            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
private void slide2()
{
    FirebaseDatabase.getInstance().getReference("homepage_images").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                up = dataSnapshot1.getValue(images.class);

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            int id=item.getItemId();
        if (id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            editor.putString("phonenumber", "");
            editor.commit();


            Intent i1 = new Intent(Main2Activity.this, logIn.class);
            startActivity(i1);
            finish();
        }
        if (id==R.id.about)
        {

            Intent i1 = new Intent(Main2Activity.this, About.class);
            startActivity(i1);

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed()
    {
        int backButtonCount=0;
        backButtonCount++;
        if(backButtonCount == 1)
        {
            Toast.makeText(this, "one", Toast.LENGTH_SHORT).show();
        }
        else if (backButtonCount == 1)
        {
            Toast.makeText(this, "two", Toast.LENGTH_SHORT).show();

        }
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
            options.put("amount", "50000");

            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDateandTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 180);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String output = sdf1.format(c.getTime());
        exp(output,s);
        success();
    }

    @Override
    public void onPaymentError(int i, final String s) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    dataSnapshot1.getRef().child("mat_payment_failure_id").setValue(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void exp(final String output, final String s)
    {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    dataSnapshot1.getRef().child("mat_exp").setValue(output);
                    dataSnapshot1.getRef().child("mat_payment_id").setValue(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void success()
    {
        FirebaseDatabase.getInstance().getReference("Matrimony_Details").orderByChild("cellno").equalTo(a1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  up1 up=dataSnapshot.getValue(up1.class);
                if (dataSnapshot.getChildrenCount() == 0) {
                    Intent i1 = new Intent(Main2Activity.this, NR.class);
                    startActivity(i1);
                    finish();
                } else {
                    Intent i1 = new Intent(Main2Activity.this, Matrimony_info.class);
                    //Toast.makeText(getApplicationContext(),up.getSex(),Toast.LENGTH_SHORT).show();
                    startActivity(i1);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void notification()
    {
        sdf = new SimpleDateFormat("yyyyMMdd");
        currentDateandTime = sdf.format(new Date());
       FirebaseDatabase.getInstance().getReference("user").orderByChild("mobile").equalTo(a1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {


                    FirebaseDatabase.getInstance().getReference("user").orderByChild("mobile").equalTo(a1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                            {
                                user1=dataSnapshot1.getValue(user.class);
                                // verify=user1.getId();
                                matrimony=user1.getMat_exp();
                                if (!user1.getJob_exp().equals("0")) {
                                    String dateInString = matrimony;  // Start date
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
                                    Calendar c = Calendar.getInstance();
                                    try {
                                        c.setTime(Objects.requireNonNull(sdf1.parse(dateInString)));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    c.add(Calendar.DATE, -5);
                                    SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
                                    Date resultdate = new Date(c.getTimeInMillis());
                                    dateInString = sdf4.format(resultdate);
                                    if (Integer.parseInt(currentDateandTime) >= Integer.parseInt(dateInString) && Integer.parseInt(currentDateandTime) <= Integer.parseInt(matrimony)) {
                                        matrimonynotification();
                                    }
                                    else if (Integer.parseInt(currentDateandTime)>Integer.parseInt(matrimony))
                                    {dataSnapshot1.getRef().child("mat_exp").setValue("0");
                                        matrimonmy_details();
                                        mat_fav();
                                        mat_fav1();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void matrimonmy_details()
    {
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    dataSnapshot1.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void mat_fav()
    {
        FirebaseDatabase.getInstance().getReference("mat_fav").child(a1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    dataSnapshot1.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void mat_fav1()
    {
        FirebaseDatabase.getInstance().getReference("mat_fav").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()) {
                    up_fav =dataSnapshot2.getValue(mat_fav.class);
                    if (up_fav.getMobile().equals(a1)) {
                        dataSnapshot2.getRef().removeValue();
                    }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
                    private void matrimonynotification()
                    {
                        AlertDialog.Builder mBuilder =new AlertDialog.Builder(Main2Activity.this);
                        View mView=getLayoutInflater().inflate(R.layout.matrimony_alert,null);
                        TextView reniew=(TextView)mView.findViewById(R.id.matrimony_reniew);
                        Button close=(Button)mView.findViewById(R.id.close_matrimony_alert);

                        reniew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startPayment();
                            }
                        });
                        mBuilder.setView(mView);
                        final AlertDialog dialog=mBuilder.create();
                        dialog.show();
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.hide();
                            }
                        });
                    }
}
