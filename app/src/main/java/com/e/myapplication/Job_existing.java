package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Job_existing extends AppCompatActivity {
    private RecyclerView recyclerView_job;
    private Query query;
    ArrayList<Employer_Details> list;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private String phonenumber;
    private Employer_Details emp;
    private ViewHolderJobExisting adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_existing);
        sharedPreferences = getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        phonenumber=sharedPreferences.getString("phonenumber","");
        recyclerView_job=(RecyclerView)findViewById(R.id.recycler_jobexisting);
        recyclerView_job.setLayoutManager(new LinearLayoutManager(this));
        query= FirebaseDatabase.getInstance().getReference("Employer_Details").orderByChild("phone").equalTo(phonenumber);
        list=new ArrayList<>();
        getSupportActionBar().setTitle("Your Ad");
        editor = sharedPreferences.edit();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    emp = dataSnapshot1.getValue(Employer_Details.class);
                    list.add(emp);


                }

                adapter=new ViewHolderJobExisting(Job_existing.this,list);
                recyclerView_job.setAdapter(adapter);
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

            Intent i1 = new Intent(Job_existing.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Job_existing.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        if (id==R.id.about)
        {

            Intent i1 = new Intent(Job_existing.this, About.class);
            startActivity(i1);

        }
        return super.onOptionsItemSelected(item);
    }
}