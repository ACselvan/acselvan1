package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {
TextView t1,t2,t3;
Button signout,date,next;
    private CardView linear_business,linear_job,linear_matrimony;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String dat;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    private  user user1;
    private Query query,query1,query2,query3;
    private String currentDateandTime;
    private SimpleDateFormat sdf;
    String a1,verify="";
    private List<images> slideLists;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    //private String[] urls = new String[]{"https://firebasestorage.googleapis.com/v0/b/saivities.appspot.com/o/Uploads%2FHoroscope%2F1592375971175.jpg?alt=media&token=4519883c-9dc7-468c-8750-382b46939604","https://firebasestorage.googleapis.com/v0/b/saivities.appspot.com/o/Uploads%2FHoroscope%2F1592375971175.jpg?alt=media&token=4519883c-9dc7-468c-8750-382b46939604","https://firebasestorage.googleapis.com/v0/b/saivities.appspot.com/o/Uploads%2FHoroscope%2F1592375971175.jpg?alt=media&token=4519883c-9dc7-468c-8750-382b46939604"};
   LinearLayout matrimony_linear,business_linear,job_linear;
    private images up;

    ImageView matrimony_image,Business_image,job_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        business_linear = findViewById(R.id.linear_business);
        matrimony_linear = findViewById(R.id.linear_matrimony);
        job_linear = findViewById(R.id.linear_job);
        matrimony_image = findViewById(R.id.marriage_image);
        Business_image = findViewById(R.id.busiess_catelogue_image);
        job_image = findViewById(R.id.job_image);

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
        query2 = databaseReference.orderByChild("cellno").equalTo(a1);
        //    date=(Button)findViewById(R.id.date);
        slide();
        slide1();
        slide2();
        signout = (Button) findViewById(R.id.signout);
        //check();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                editor.putString("phonenumber", "");
                editor.commit();
                finish();

                Intent i1 = new Intent(Main2Activity.this, logIn.class);
                startActivity(i1);
            }
        });
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
                               /* Intent i1=new Intent(Main2Activity.this,Matrimony.class);
                                startActivity(i1);*/
                            } else if (verify.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                                builder.setTitle("only prmium members");
                                builder.setMessage("click payup to pay fee for entering matrimony");
                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(), "it will leads to payment page", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getApplicationContext(), "it will leads to payment page", Toast.LENGTH_SHORT).show();
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
    private void check()
    {
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    user1=dataSnapshot1.getValue(user.class);
                   // verify=user1.getId();
                    editor.putString("id_user",user1.getId());
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

private void slide()
{
    FirebaseDatabase.getInstance().getReference("homepage_images").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                up = dataSnapshot1.getValue(images.class);


                Picasso.with(getApplicationContext()).load(up.getImage()).into(Business_image);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
private void slide1()
{
    FirebaseDatabase.getInstance().getReference("homepage_images").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                up = dataSnapshot1.getValue(images.class);

                Picasso.with(getApplicationContext()).load(up.getImage1()).into(matrimony_image);
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
                Picasso.with(getApplicationContext()).load(up.getImage2()).into(job_image);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}

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
}
