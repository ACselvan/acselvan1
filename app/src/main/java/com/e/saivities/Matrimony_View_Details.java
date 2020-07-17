package com.e.saivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Matrimony_View_Details extends AppCompatActivity {
    private TextView name,fathername,mothername,cellno,sex,age,siblings,income,job,education,height,city,children,noofchildren,status_edit,company;
    private ImageView profilimage;
    private DatabaseReference databaseReference;
    private Query query2;
    private String phonenumber;
    private up1 up;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony__view__details);
        phonenumber= getIntent().getStringExtra("phonenumber");
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        profilimage=findViewById(R.id.profileimage_drawer_matrimony_details);
        name=findViewById(R.id.drawer_name_matrimony);
        fathername=findViewById(R.id.drawer_fathername_matrimony_details);
        mothername=findViewById(R.id.drawer_mothernamename_matrimony_details);
        cellno=findViewById(R.id.drawer_cellno_matrimony_details);
        sex=findViewById(R.id.drawer_sex_matrimony_details);
        age=findViewById(R.id.drawer_age_matrimony_details);
        siblings=findViewById(R.id.drawer_siblings_matrimony_details);
        income=findViewById(R.id.drawer_income_matrimony_details);
        job=findViewById(R.id.drawer_job_matrimony_details);
        education=findViewById(R.id.drawer_education_matrimony_details);
        height=findViewById(R.id.drawer_height_matrimony_details);
        city=findViewById(R.id.drawer_city_view_details);
        company=findViewById(R.id.drawer_company_matrimony_details);
        getSupportActionBar().setTitle("Details");
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        databaseReference= FirebaseDatabase.getInstance().getReference("Matrimony_Details");
        query2=databaseReference.orderByChild("cellno").equalTo(phonenumber);
        editor = sharedPreferences.edit();
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    height.setText(up.getHeight()+" cm");
                    city.setText(up.getCity());
                    company.setText(up.getCompanyy());
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


            Intent i1 = new Intent(Matrimony_View_Details.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Matrimony_View_Details.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        if (id==R.id.about)
        {

            Intent i1 = new Intent(Matrimony_View_Details.this, About.class);
            startActivity(i1);

        }
        return super.onOptionsItemSelected(item);
    }
}
