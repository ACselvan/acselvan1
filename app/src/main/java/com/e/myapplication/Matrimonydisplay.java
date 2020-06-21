package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Matrimonydisplay extends AppCompatActivity {
    ListView lv;
    String usersGender,gender;
    DatabaseReference Matrimony_Details;
    List<up1> up;
    Button b;
    matrimony_list matrimony_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimonydisplay);
        b = findViewById(R.id.bu);
        up=new ArrayList<up1>();
        lv = findViewById(R.id.lv);
        Matrimony_Details = FirebaseDatabase.getInstance().getReference("Matrimony_Details");
        SharedPreferences sh = getSharedPreferences("My shared preferences",MODE_PRIVATE);
        gender  = sh.getString("sex","");




    b.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



                Matrimony_Details.orderByChild("Sex").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        up.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            up1 up1 = snapshot.getValue(up1.class);
                            up.add(up1);
                            matrimony_list = new matrimony_list(Matrimonydisplay.this,up,Matrimony_Details);

                            lv.setAdapter(matrimony_list);

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
