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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Business_catalogue extends AppCompatActivity implements AdapterView.OnItemClickListener, PaymentResultListener {
ListView lv;
Button addbusiness,b1;
EditText e1;
    private  user user1;
    private Query query;
    private String currentDateandTime,a1,business,verify;
    private SimpleDateFormat sdf;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
DatabaseReference businessCategoryTable;
List<String> CategoryList = new ArrayList<>();
    private Business_fav up_fav;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_catalogue);
        lv=findViewById(R.id.lv);
        b1 = findViewById(R.id.b1);
        e1= findViewById(R.id.e1);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        a1=sharedPreferences.getString("phonenumber","");
        databaseReference= FirebaseDatabase.getInstance().getReference("user");
        query=databaseReference.orderByChild("mobile").equalTo(a1);
        editor=sharedPreferences.edit();
        getSupportActionBar().setTitle("Business");
        addbusiness=findViewById(R.id.addbusiness);
        businessCategoryTable= FirebaseDatabase.getInstance().getReference("Categories");
        Checkout.preload(getApplicationContext());
        notification();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i =i+1;

                String name = e1.getText().toString().trim();
                Upload upload  = new Upload(name);
                businessCategoryTable.child(String.valueOf(i)).setValue(upload);
            }
        });
       businessCategoryTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList.clear();
                for(DataSnapshot categorysnapshot : dataSnapshot.getChildren())
                {
                 Upload upload = categorysnapshot.getValue(Upload.class);
               String NAME = upload.getName().toString();
                CategoryList.add(NAME);


                }
                ArrayAdapter adapter = new ArrayAdapter<>(Business_catalogue.this,android.R.layout.simple_list_item_1,CategoryList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(this);
        addbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdf = new SimpleDateFormat("yyyyMMdd");
                currentDateandTime = sdf.format(new Date());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            user1=dataSnapshot1.getValue(user.class);
                            verify=user1.getBusss_exp();
                            //Toast.makeText(getApplicationContext(),user1.getMat_exp(),Toast.LENGTH_SHORT).show();
                            if (Integer.parseInt(currentDateandTime)<=Integer.parseInt(user1.busss_exp))
                            {success();
                                /*
                                FirebaseDatabase.getInstance().getReference("Business_Details").orderByChild("contact_number").equalTo(a1).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() == 0)
                                        {
                                            Intent i=new Intent(Business_catalogue.this,Getting_Business_details.class);
                                            startActivity(i);

                                        }else
                                        {
                                            Intent i=new Intent(Business_catalogue.this,Business_Edit.class);
                                            startActivity(i);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });*/
                                /*Intent i=new Intent(Business_catalogue.this,Getting_Business_details.class);
                                startActivity(i);*/
                            }
                            else if (verify.equals("0"))
                            {
                                final AlertDialog.Builder builder=new AlertDialog.Builder(Business_catalogue.this);
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
                                final AlertDialog.Builder builder1=new AlertDialog.Builder(Business_catalogue.this);
                                builder1.setTitle("premium membership expired");
                                builder1.setMessage("finish the payment for continue matrimony service");
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("Proceed To Payment", new DialogInterface.OnClickListener() {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selected_category = (String) parent.getItemAtPosition(position);
        //Intent i = new Intent(Business_catalogue.this,Category.class);
        Intent i = new Intent(Business_catalogue.this,Business_info.class);
        Bundle b = new Bundle();
        b.putString("selected-category",selected_category);
        editor.putString("selected-category",selected_category);
        editor.commit();
        i.putExtras(b);
        startActivity(i);
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


            Intent i1 = new Intent(Business_catalogue.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Business_catalogue.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        if (id==R.id.about)
        {

            Intent i1 = new Intent(Business_catalogue.this, About.class);
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
            Intent i1=new Intent(Business_catalogue.this,Main2Activity.class);
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
                    dataSnapshot1.getRef().child("busss_exp").setValue(output);
                    dataSnapshot1.getRef().child("bussiness_payment_id").setValue(s);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void success() {
        FirebaseDatabase.getInstance().getReference("Business_Details").orderByChild("contact_number").equalTo(a1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                {
                    Intent i=new Intent(Business_catalogue.this,Getting_Business_details.class);
                    startActivity(i);

                }else
                {
                    Intent i=new Intent(Business_catalogue.this,Business_Edit.class);
                    startActivity(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void notification()
    {sdf = new SimpleDateFormat("yyyyMMdd");
        currentDateandTime = sdf.format(new Date());
        FirebaseDatabase.getInstance().getReference("user").orderByChild("mobile").equalTo(a1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {user1=dataSnapshot1.getValue(user.class);
                    business=user1.getBusss_exp();
                    if (!user1.getJob_exp().equals("0")) {
                        String dateInString = business;  // Start date
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
                        if (Integer.parseInt(currentDateandTime) >= Integer.parseInt(dateInString) && Integer.parseInt(currentDateandTime) <= Integer.parseInt(business)) {
                            matrimonynotification();
                        }
                        else if (Integer.parseInt(currentDateandTime)>Integer.parseInt(business))
                        {dataSnapshot1.getRef().child("busss_exp").setValue("0");
                            business_details();
                            business_fav();
                            business_fav1();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void business_fav()
    {
        FirebaseDatabase.getInstance().getReference("Business_fav").child(a1).addValueEventListener(new ValueEventListener() {
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
    private void business_details()
    {
        FirebaseDatabase.getInstance().getReference("Business_Details").orderByChild("contact_number").equalTo(a1).addValueEventListener(new ValueEventListener() {
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
    private void business_fav1()
    {
        FirebaseDatabase.getInstance().getReference("Business_fav").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()) {
                        up_fav =dataSnapshot2.getValue(Business_fav.class);
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
        AlertDialog.Builder mBuilder =new AlertDialog.Builder(Business_catalogue.this);
        View mView=getLayoutInflater().inflate(R.layout.business_alert,null);
        TextView reniew=(TextView)mView.findViewById(R.id.business_reniew);
        Button close=(Button)mView.findViewById(R.id.close_business_alert);

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
