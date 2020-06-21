package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {
    ListView lv;
String selected_category;
DatabaseReference Business_Details;
List<Upload> uploads;
UploadList uploadlist;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //   t=findViewById(R.id.t);
        uploads=new ArrayList<Upload>();
        lv = findViewById(R.id.lv);
        //   Bundle b = new Bundle();
        selected_category = (getIntent().getStringExtra("selected-category"));
        Business_Details = FirebaseDatabase.getInstance().getReference("Business_Details");
        Business_Details.orderByChild("category").equalTo(selected_category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               uploads.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Upload upload = snapshot.getValue(Upload.class);
                    uploads.add(upload);
                    uploadlist = new UploadList(Category.this,uploads,Business_Details);
                    lv.setAdapter(uploadlist);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
