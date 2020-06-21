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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Matrimony_Favourite extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView matrimony_navigation;
    private RecyclerView recyclerView_matrimony;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private String phonenumber;
    private up1 up;
    private mat_fav mat;
    Button matrimony_favourites;
    private ArrayList<up1> list;;
    private ViewHolderMatrimonyFavourite adapter;
    private DatabaseReference databaseReference,databaseReference1;
    private Query query,query1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony__favourite);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");
        recyclerView_matrimony=(RecyclerView)findViewById(R.id.matrimony_favourite_recyclewer);
        recyclerView_matrimony.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout=(DrawerLayout)findViewById(R.id.matrimony_favourite_drawer);
        matrimony_favourites=(Button)drawerLayout.findViewById(R.id.matrimony_favourites);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        matrimony_navigation=(NavigationView)findViewById(R.id.matrimony_favourite_navigation);
        matrimony_navigation.setNavigationItemSelectedListener(this);
        View v1=matrimony_navigation.getHeaderView(0);
        matrimony_favourites=(Button)v1.findViewById(R.id.matrimony_favourites);
        list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("mat_fav");
        databaseReference1= FirebaseDatabase.getInstance().getReference("Matrimony_Details");
        //query1=databaseReference1.orderByChild("cellno").equalTo(phonenumber);
        query=databaseReference.child(phonenumber);
        check();
        matrimony_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matrimony_favourites.setText("back");
                Intent i1=new Intent(Matrimony_Favourite.this,Matrimony_info.class);
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

                    mat = dataSnapshot1.getValue(mat_fav.class);
                    query1=databaseReference1.orderByChild("cellno").equalTo(mat.getMobile());
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                up = dataSnapshot1.getValue(up1.class);
                                list.add(up);
                            }
                            adapter=new ViewHolderMatrimonyFavourite(Matrimony_Favourite.this,list,phonenumber);
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


            Intent i1 = new Intent(Matrimony_Favourite.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Matrimony_Favourite.this, Main2Activity.class);
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
            Intent i1=new Intent(Matrimony_Favourite.this,Matrimony_info.class);
            startActivity(i1);
            finish();

        }

    }
}
