package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Business_Favourites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView matrimony_navigation;
    private RecyclerView recyclerView_matrimony;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private String phonenumber;
    Button matrimony_favourites;
    private ViewHolderBusinessFavourite adapter;
    private DatabaseReference databaseReference,databaseReference1;
    private Query query,query1;
    private Business_fav mat;
    ArrayList<Upload> list;
    Upload upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__favourites);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");
        recyclerView_matrimony=(RecyclerView)findViewById(R.id.businessfavourite_recycle);
        recyclerView_matrimony.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout=(DrawerLayout)findViewById(R.id.businessfavourite_drawer);
        matrimony_favourites=(Button)drawerLayout.findViewById(R.id.matrimony_favourites);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        matrimony_navigation=(NavigationView)findViewById(R.id.businessfavourite_navigation);
        matrimony_navigation.setNavigationItemSelectedListener(this);
        View v1=matrimony_navigation.getHeaderView(0);
        matrimony_favourites=(Button)v1.findViewById(R.id.matrimony_favourites);
        list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Business_fav");
        databaseReference1= FirebaseDatabase.getInstance().getReference("Business_Details");
        query=databaseReference.child(phonenumber);
        editor = sharedPreferences.edit();
        check();
        matrimony_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(Business_Favourites.this,Business_info.class);
                startActivity(i1);
            }
        });
    }
private void check()
{

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                            mat = dataSnapshot1.getValue(Business_fav.class);

                            query1=databaseReference1.orderByChild("contact_number").equalTo(mat.getMobile());
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        upload = dataSnapshot1.getValue(Upload.class);
                                        list.add(upload);
                                    }
                                    adapter=new ViewHolderBusinessFavourite(Business_Favourites.this,list,phonenumber);
                                    recyclerView_matrimony.setAdapter(adapter);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
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


            Intent i1 = new Intent(Business_Favourites.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Business_Favourites.this, Main2Activity.class);
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
            Intent i1=new Intent(Business_Favourites.this,Business_info.class);
            startActivity(i1);
            finish();

        }

    }
}
