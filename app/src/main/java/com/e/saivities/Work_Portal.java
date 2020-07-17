package com.e.saivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Work_Portal extends AppCompatActivity implements PaymentResultListener {
ImageView hire,work;
    private  user user1;
    private Query query,query1;
    private String currentDateandTime,a1,verify,job;
    private SimpleDateFormat sdf;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    DatabaseReference databaseReference,databaseReference1;
    Date outputdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work__portal);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        a1=sharedPreferences.getString("phonenumber","");
        databaseReference= FirebaseDatabase.getInstance().getReference("user");
        query=databaseReference.orderByChild("mobile").equalTo(a1);
        databaseReference1= FirebaseDatabase.getInstance().getReference("Employer_Details");
        query1=databaseReference1.orderByChild("phone").equalTo(a1);
        editor=sharedPreferences.edit();
        getSupportActionBar().setTitle("Work Portal");
        hire=(ImageView)findViewById(R.id.work_hire);
        Checkout.preload(getApplicationContext());
        work=(ImageView)findViewById(R.id.work_employee);
        notification();
        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i1=new Intent(Work_Portal.this,Employportal.class);
                startActivity(i1);*/
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            Intent i1=new Intent(Work_Portal.this,Employportal.class);
                            startActivity(i1);
                        }
                        else
                        {
                            AlertDialog.Builder builder=new AlertDialog.Builder(Work_Portal.this);
                            builder.setTitle("you have a live ad");
                            builder.setMessage("you can see your add r create new one");
                            builder.setPositiveButton("My ad", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   Intent i1=new Intent(Work_Portal.this,Job_existing.class);
                                    startActivity(i1);
                                }
                            });
                            builder.setNegativeButton("new ad", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                  Intent i1=new Intent(Work_Portal.this,Employportal.class);
                                    startActivity(i1);
                                }
                            });
                            builder.create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sdf = new SimpleDateFormat("yyyyMMdd");
                currentDateandTime = sdf.format(new Date());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            user1=dataSnapshot1.getValue(user.class);
                            verify=user1.getJob_exp();
                            //Toast.makeText(getApplicationContext(),user1.getMat_exp(),Toast.LENGTH_SHORT).show();
                            if (Integer.parseInt(currentDateandTime)<=Integer.parseInt(user1.job_exp))
                            {
                                Intent i1=new Intent(Work_Portal.this,JobList.class);
                                startActivity(i1);
                            }
                            else if (verify.equals("0"))
                            {
                                final AlertDialog.Builder builder=new AlertDialog.Builder(Work_Portal.this);
                                builder.setTitle("only prmium members");
                                builder.setMessage("click payup to pay fee for entering matrimony");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Proceed To Payment", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startPayment();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        builder.create().hide();
                                    }
                                });
                                builder.create().show();
                            }
                            else
                            {
                                final AlertDialog.Builder builder1=new AlertDialog.Builder(Work_Portal.this);
                                builder1.setTitle("premium membership expired");
                                builder1.setMessage("finish the payment for continue matrimony service");
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startPayment();
                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        builder1.create().hide();
                                    }
                                });
                                builder1.create().show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logandhome,menu);
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


            Intent i1 = new Intent(Work_Portal.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Work_Portal.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        if (id==R.id.about)
        {

            Intent i1 = new Intent(Work_Portal.this, About.class);
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
            Intent i1=new Intent(Work_Portal.this,Main2Activity.class);
            startActivity(i1);
            finish();

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
        checkout.setImage(R.drawable.homepageimage);

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
            options.put("name", "Saivities");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #"+a1);
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
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
    private void exp(final String output, final String s)
    {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    dataSnapshot1.getRef().child("job_exp").setValue(output);
                    dataSnapshot1.getRef().child("job_payment_id").setValue(s);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void success() {
        Intent i1 = new Intent(Work_Portal.this, JobList.class);
        startActivity(i1);
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
                    user1=dataSnapshot1.getValue(user.class);
                    // verify=user1.getId();
                    job=user1.getJob_exp();
                    if (!user1.getJob_exp().equals("0")) {
                        String dateInString = job;  // Start date
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
                        if (Integer.parseInt(currentDateandTime) >= Integer.parseInt(dateInString) && Integer.parseInt(currentDateandTime) <= Integer.parseInt(job)) {
                            matrimonynotification();

                        }
                        else if (Integer.parseInt(currentDateandTime)>Integer.parseInt(job))
                        {dataSnapshot1.getRef().child("job_exp").setValue("0");

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void verfy(String jobExp)
    {

    }
    private void matrimonynotification()
    {
        AlertDialog.Builder mBuilder =new AlertDialog.Builder(Work_Portal.this);
        View mView=getLayoutInflater().inflate(R.layout.job_alert,null);
        TextView reniew=(TextView)mView.findViewById(R.id.job_reniew);
        Button close=(Button)mView.findViewById(R.id.close_job_alert);

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
//.