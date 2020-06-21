package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Context;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Business_info extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
RecyclerView recyclerView_business;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    Button business_favourites,edit,imageedit;
    private TextView firmname,proprietorname,category,city,address,description,contactnumber;
    private ImageView image;
    NavigationView matrimony_navigation;
private Query query;
private String selected_category,phonenumber;

private DatabaseReference databaseReference;
    ArrayList<Upload> list;
    Upload upload;
    ViewHolder adapter;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_info);
        drawerLayout=(DrawerLayout)findViewById(R.id.business_drawer);
        business_favourites=(Button)drawerLayout.findViewById(R.id.business_favourites);
        edit=(Button)drawerLayout.findViewById(R.id.edit_business);
        imageedit=(Button)drawerLayout.findViewById(R.id.business_image_edit);
        firmname=(TextView)drawerLayout.findViewById(R.id.drawer_firmname_business);
        proprietorname=(TextView)drawerLayout.findViewById(R.id.drawer_proprietorname_business);
        category=(TextView)drawerLayout.findViewById(R.id.drawer_category_business);
        city=(TextView)drawerLayout.findViewById(R.id.drawer_city_business);
        address=(TextView)drawerLayout.findViewById(R.id.drawer_addresss_business);
        description=(TextView)drawerLayout.findViewById(R.id.drawer_description_business);
        contactnumber=(TextView)drawerLayout.findViewById(R.id.drawer_contactnumber_business);
        image=(ImageView)drawerLayout.findViewById(R.id.profileimage_drawer_business);
        //Toast.makeText(getApplicationContext(),id_user,Toast.LENGTH_SHORT).show();
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");
        editor = sharedPreferences.edit();
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        matrimony_navigation=(NavigationView)findViewById(R.id.business_navigation);
        matrimony_navigation.setNavigationItemSelectedListener(this);
        View v1=matrimony_navigation.getHeaderView(0);
        business_favourites=(Button)v1.findViewById(R.id.business_favourites);
        firmname=(TextView)v1.findViewById(R.id.drawer_firmname_business);
        edit=(Button)v1.findViewById(R.id.edit_business);
        proprietorname=(TextView)v1.findViewById(R.id.drawer_proprietorname_business);
        category=(TextView)v1.findViewById(R.id.drawer_category_business);
        city=(TextView)v1.findViewById(R.id.drawer_city_business);
        address=(TextView)v1.findViewById(R.id.drawer_addresss_business);
        description=(TextView)v1.findViewById(R.id.drawer_description_business);
        contactnumber=(TextView)v1.findViewById(R.id.drawer_contactnumber_business);
        image=(ImageView)v1.findViewById(R.id.profileimage_drawer_business);
        imageedit=(Button)v1.findViewById(R.id.business_image_edit);
        recyclerView_business=(RecyclerView)findViewById(R.id.recyclerview_business);
        recyclerView_business.setLayoutManager(new LinearLayoutManager(this));
        databaseReference= FirebaseDatabase.getInstance().getReference("Business_Details");
        query=databaseReference.orderByChild("contact_number").equalTo(phonenumber);
        list=new ArrayList<>();
        selected_category = sharedPreferences.getString("selected-category","");//getIntent().getStringExtra("selected-category");
        check();
        drawer();
        business_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(Business_info.this,Business_Favourites.class);
                startActivity(i1);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(Business_info.this,Business_Edit.class);
                startActivity(i1);
            }
        });
        imageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i1=new Intent(Business_info.this,Business_Image_Edit.class);
                startActivity(i1);
            }
        });
    }

    public void check()
    {
        databaseReference.orderByChild("category").equalTo(selected_category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {



                    upload = dataSnapshot1.getValue(Upload.class);
                       // if (upload.getFirmname().equals("ac")) {
                            list.add(upload);
                       // }

               // mProgressCircle.setVisibility(View.INVISIBLE);
            }
                adapter=new ViewHolder(Business_info.this,list,phonenumber);
                recyclerView_business.setAdapter(adapter);
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
query.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {


            upload = dataSnapshot1.getValue(Upload.class);
            Picasso.with(getApplicationContext()).load(upload.getImageurl()).into(image);
            firmname.setText("firmname:"+upload.getFirmname());
            proprietorname.setText("proprietor name:"+upload.getProprietor_name());
            category.setText("category: "+upload.getCategory());
            city.setText("city: "+upload.getCity());
            address.setText("address: "+upload.getAddress());
            description.setText("description:"+upload.getDescription());
            contactnumber.setText("contact number:"+upload.getContact_number());
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

        int id=item.getItemId();
        if (id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            editor.putString("phonenumber", "");
            editor.commit();


            Intent i1 = new Intent(Business_info.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Business_info.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
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
            Intent i1=new Intent(Business_info.this,Business_catalogue.class);
            startActivity(i1);
            finish();

        }

    }
}
