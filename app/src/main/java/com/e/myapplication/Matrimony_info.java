package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Matrimony_info extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button matrimony_favourites,editprofile,editimage;
    private TextView name,fathername,mothername,cellno,sex,age,siblings,income,job,education,height,city,status_edit,children,noofchildren;
    private Query query,query1,query2;
    private ImageView profilimage;
    private String imageurl;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView matrimony_navigation;
    private RecyclerView recyclerView_matrimony;
    String usersGender,gender,phonenumber,id_user;
    up1 up;
    private DatabaseReference databaseReference,databaseReference1;
    ArrayList<up1> list;
    private ViewHolderMatrimony adapter;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    DatabaseReference businessCategoryTable;
    List<String> CategoryList = new ArrayList<>();
    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private CirclePageIndicator indicator;
    private String[] urls;
    int i = 0;
    private String a11,a2,a3,a4,a5,a7,a6,a8;
    private images up1;
    private ProgressBar mProgressCircle;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony_info);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");
        editor = sharedPreferences.edit();
        mProgressCircle = findViewById(R.id.progress_circle);
        recyclerView_matrimony=(RecyclerView)findViewById(R.id.matrimony_recycle);
        recyclerView_matrimony.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_matrimony.addItemDecoration(new DividerItemDecoration(recyclerView_matrimony.getContext(), DividerItemDecoration.VERTICAL));
        databaseReference= FirebaseDatabase.getInstance().getReference("Matrimony_Details");
        databaseReference1=FirebaseDatabase.getInstance().getReference("business_images");
        databaseReference1.keepSynced(true);
        databaseReference.keepSynced(true);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if (dayOfTheWeek.equals("Sunday"))
        {
            query=databaseReference.orderByChild("name");
        }
        else if (dayOfTheWeek.equals("Monday"))
        {
            query=databaseReference.orderByChild("fathersname");
        }
        else if (dayOfTheWeek.equals("Tuesday"))
        {
            query=databaseReference.orderByChild("mothersname");
        }
        else if (dayOfTheWeek.equals("Wednesday"))
        {
            query=databaseReference.orderByChild("mothersname");
        }
        else if (dayOfTheWeek.equals("Thursday"))
        {
            query=databaseReference.orderByChild("job");
        }
        else if (dayOfTheWeek.equals("Friday"))
        {
            query=databaseReference.orderByChild("companyy");
        }
        else if (dayOfTheWeek.equals("Saturday"))
        {
            query=databaseReference.orderByChild("education");
        }
        query2=databaseReference.orderByChild("cellno").equalTo(phonenumber);
        drawerLayout=(DrawerLayout)findViewById(R.id.matrimony_drawer);
        matrimony_favourites=(Button)drawerLayout.findViewById(R.id.matrimony_favourites);
        editprofile=(Button)drawerLayout.findViewById(R.id.matrimony_editprofile);
        editimage=(Button)drawerLayout.findViewById(R.id.matrimony_editimage);
        name=(TextView)drawerLayout.findViewById(R.id.drawer_name_matrimony);
        fathername=(TextView)drawerLayout.findViewById(R.id.drawer_fathername_matrimony);
        mothername=(TextView)drawerLayout.findViewById(R.id.drawer_mothernamename_matrimony);
        cellno=(TextView)drawerLayout.findViewById(R.id.drawer_cellno_matrimony);
        sex=(TextView)drawerLayout.findViewById(R.id.drawer_sex_matrimony);
        age=(TextView)drawerLayout.findViewById(R.id.drawer_age_matrimony);
        siblings=(TextView)drawerLayout.findViewById(R.id.drawer_siblings_matrimony);
        income=(TextView)drawerLayout.findViewById(R.id.drawer_income_matrimony);
        job=(TextView)drawerLayout.findViewById(R.id.drawer_job_matrimony);
        education=(TextView)drawerLayout.findViewById(R.id.drawer_education_matrimony);
        height=(TextView)drawerLayout.findViewById(R.id.drawer_height_matrimony);
        city=(TextView)drawerLayout.findViewById(R.id.drawer_city_matrimony);

        profilimage=(ImageView) drawerLayout.findViewById(R.id.profileimage_drawer_matrimony);
        getSupportActionBar().setTitle("Matrimony List");
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        matrimony_navigation=(NavigationView)findViewById(R.id.matrimony_navigation);
        matrimony_navigation.setNavigationItemSelectedListener(this);
        View v1=matrimony_navigation.getHeaderView(0);
        matrimony_favourites=(Button)v1.findViewById(R.id.matrimony_favourites);
        editprofile=(Button)v1.findViewById(R.id.matrimony_editprofile);
        name=(TextView)v1.findViewById(R.id.drawer_name_matrimony);
        fathername=(TextView)v1.findViewById(R.id.drawer_fathername_matrimony);
        mothername=(TextView)v1.findViewById(R.id.drawer_mothernamename_matrimony);
        cellno=(TextView)v1.findViewById(R.id.drawer_cellno_matrimony);
        sex=(TextView)v1.findViewById(R.id.drawer_sex_matrimony);
        age=(TextView)v1.findViewById(R.id.drawer_age_matrimony);
        siblings=(TextView)v1.findViewById(R.id.drawer_siblings_matrimony);
        income=(TextView)v1.findViewById(R.id.drawer_income_matrimony);
        job=(TextView)v1.findViewById(R.id.drawer_job_matrimony);
        education=(TextView)v1.findViewById(R.id.drawer_education_matrimony);
        height=(TextView)v1.findViewById(R.id.drawer_height_matrimony);
        city=(TextView)v1.findViewById(R.id.drawer_city_matrimony);
        profilimage=(ImageView) v1.findViewById(R.id.profileimage_drawer_matrimony);
        editimage=(Button)v1.findViewById(R.id.matrimony_editimage);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        query1=databaseReference.orderByChild("cellno").equalTo(phonenumber);
        list=new ArrayList<>();
        slide();
        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i1=new Intent(Matrimony_info.this,Matrimony_Image_Edit.class);
            startActivity(i1);
            }
        });
        check1();
        drawer();
        //check();
        matrimony_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(Matrimony_info.this,Matrimony_Favourite.class);
                startActivity(i1);
            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(Matrimony_info.this,Matrimony_Edit.class);
                startActivity(i1);
            }
        });
    }

    private void check(final String usersGender)
    {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {



                    up = dataSnapshot1.getValue(up1.class);
                    assert up != null;
                    if (usersGender.equals(up.getSex())) {
                    list.add(up);
                     }

                    // mProgressCircle.setVisibility(View.INVISIBLE);
                }
                adapter=new ViewHolderMatrimony(Matrimony_info.this,list,phonenumber);
                recyclerView_matrimony.setAdapter(adapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        query=databaseReference.orderByChild("sex").equalTo(usersGender);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {



                    up = dataSnapshot1.getValue(up1.class);
                    // if (upload.getFirmname().equals("ac")) {
                    list.add(up);
                    // }

                    // mProgressCircle.setVisibility(View.INVISIBLE);
                }
                adapter=new ViewHolderMatrimony(Matrimony_info.this,list,phonenumber);
                recyclerView_matrimony.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }
    private void check1()
    {

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {



                    up = dataSnapshot1.getValue(up1.class);
                    // if (upload.getFirmname().equals("ac")) {
                   usersGender=up.getSex();
                    // }
                    if (up.getSex().equals("Male"))
                    {
                        usersGender="Female";
                        check(usersGender);
                    }
                    else if (up.getSex().equals("Female"))
                    {
                        usersGender="Male";
                        check(usersGender);
                    }

                    // mProgressCircle.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
    private void drawer()
    {
            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                        up = dataSnapshot1.getValue(up1.class);
                        Picasso.with(getApplicationContext()).load(up.getProfileImage()).into(profilimage);
                        name.setText(up.getName());
                        fathername.setText(up.getFathersname());
                        mothername.setText(up.getMothersname());
                        cellno.setText(up.getCellno());
                        sex.setText(up.getSex());
                        age.setText(up.getAge());
                        siblings.setText(up.getSiblings());
                        education.setText(up.getEducation());
                        income.setText(up.getIncome());
                        job.setText(up.getJob());
                        height.setText(up.getHeight());
                        city.setText(up.getCity());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        int id=item.getItemId();
        if (id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            editor.putString("phonenumber", "");
            editor.commit();


            Intent i1 = new Intent(Matrimony_info.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Matrimony_info.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        if (id==R.id.about)
        {

            Intent i1 = new Intent(Matrimony_info.this, About.class);
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
            Intent i1=new Intent(Matrimony_info.this,Main2Activity.class);
            startActivity(i1);
            finish();

        }

    }
    private void slide()
    {
        FirebaseDatabase.getInstance().getReference("business_images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    up1 = dataSnapshot1.getValue(images.class);
                    a11=up1.getImage();
                    a2=up1.getImage1();
                    a3=up1.getImage2();
                    a4=up1.getImage3();
                    a5=up1.getImage4();
                    a6=up1.getImage5();
                    a7=up1.getImage6();
                    a8=up1.getImage7();
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
        viewPager.setAdapter(new ViewPagerAdapter(Matrimony_info.this,urls));
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
}
