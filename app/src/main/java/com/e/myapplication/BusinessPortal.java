package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BusinessPortal extends AppCompatActivity {
Button  add_business;
    private  user user1;
    private Query query;
    private String currentDateandTime,a1,verify;
    private SimpleDateFormat sdf;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_portal);
        add_business=(Button)findViewById(R.id.add_business);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        a1=sharedPreferences.getString("phonenumber","");
        databaseReference= FirebaseDatabase.getInstance().getReference("user");
        query=databaseReference.orderByChild("mobile").equalTo(a1);
        editor=sharedPreferences.edit();
        add_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                Intent i1=new Intent(BusinessPortal.this,Business_catalogue.class);
                                startActivity(i1);
                            }
                            else if (verify.equals("0"))
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(BusinessPortal.this);
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
                                AlertDialog.Builder builder=new AlertDialog.Builder(BusinessPortal.this);
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
}
