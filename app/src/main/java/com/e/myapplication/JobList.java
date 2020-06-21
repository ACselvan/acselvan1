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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JobList extends AppCompatActivity {
     Query query;
    RecyclerView recyclerView_job;
    private DatabaseReference databaseReference;
    ArrayList<Employer_Details> list;
    Employer_Details emp;
    ViewHolderJob adapter;
    private SimpleDateFormat sdf;
    private String currentDateandTime;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        recyclerView_job=(RecyclerView)findViewById(R.id.recycler_job);
        recyclerView_job.setLayoutManager(new LinearLayoutManager(this));
        databaseReference= FirebaseDatabase.getInstance().getReference("Employer_Details");
        query=FirebaseDatabase.getInstance().getReference("Employer_Details");
        list=new ArrayList<>();
        sharedPreferences = getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        check();
    }
    private void check()
    {
        sdf = new SimpleDateFormat("yyyyMMdd");
        currentDateandTime = sdf.format(new Date());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {



                    emp = dataSnapshot1.getValue(Employer_Details.class);
                    if (Integer.parseInt(currentDateandTime)<=Integer.parseInt(emp.getExp())) {
                    list.add(emp);
                    }
                    else if (Integer.parseInt(currentDateandTime)>Integer.parseInt(emp.getExp())) {
                        dataSnapshot1.getRef().removeValue();
                    }


                }
                adapter=new ViewHolderJob(JobList.this,list);
                recyclerView_job.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
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


            Intent i1 = new Intent(JobList.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(JobList.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
