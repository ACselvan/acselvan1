package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Business_catalogue extends AppCompatActivity implements AdapterView.OnItemClickListener {
ListView lv;
Button addbusiness,b1;
EditText e1;
    private  user user1;
    private Query query;
    private String currentDateandTime,a1,verify;
    private SimpleDateFormat sdf;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
DatabaseReference businessCategoryTable;
List<String> CategoryList = new ArrayList<>();
    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private CirclePageIndicator indicator;
    private String[] urls;
    int i = 0;
    private String a11,a2,a3,a4,a5,a7,a6,a8;
    private images up;
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
        addbusiness=findViewById(R.id.addbusiness);
        businessCategoryTable= FirebaseDatabase.getInstance().getReference("Categories");

        slide();
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

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            user1=dataSnapshot1.getValue(user.class);
                            verify=user1.getBusss_exp();
                            //Toast.makeText(getApplicationContext(),user1.getMat_exp(),Toast.LENGTH_SHORT).show();
                            if (Integer.parseInt(currentDateandTime)<=Integer.parseInt(user1.busss_exp))
                            {
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
                                /*Intent i=new Intent(Business_catalogue.this,Getting_Business_details.class);
                                startActivity(i);*/
                            }
                            else if (verify.equals("0"))
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(Business_catalogue.this);
                                builder.setTitle("only prmium members");
                                builder.setMessage("click payup to pay fee for entering matrimony");
                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(),"it will leads to payment page",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.create().show();
                            }
                            else
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(Business_catalogue.this);
                                builder.setTitle("premium membership expired");
                                builder.setMessage("finish the payment for continue matrimony service");
                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(),"it will leads to payment page",Toast.LENGTH_SHORT).show();
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
    private void slide()
    {
        FirebaseDatabase.getInstance().getReference("business_images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    up = dataSnapshot1.getValue(images.class);
                    a11=up.getImage();
                    a2=up.getImage1();
                    a3=up.getImage2();
                    a4=up.getImage3();
                    a5=up.getImage4();
                    a6=up.getImage5();
                    a7=up.getImage6();
                    a8=up.getImage7();
                }
                urls = new String[]{a11,a2,a3,a4,a5,a6,a7,a8};
                init();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void init()
    {viewPager=findViewById(R.id.view_pager_business);
        viewPager.setAdapter(new ViewPagerAdapter(Business_catalogue.this,urls));
        indicator = (CirclePageIndicator)
                findViewById(R.id.indicator_business);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 9000, 9000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

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
}
