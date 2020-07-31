package com.e.saivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Business_Edit extends AppCompatActivity {
EditText firmname,propriotorname,address,city,descreiption;
Button submit;
Spinner spinner;
TextView mobile;
    private Spinner s;
    DatabaseReference businessCategoryTable1;
    List<String> CategoryList1 = new ArrayList<>();
    List<String> CategoryList = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    DatabaseReference Business_details,businessCategoryTable;
    String phonenumber;
    Query query;
    Upload upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__edit);
        firmname=(EditText)findViewById(R.id.firmname_edit);
        propriotorname=(EditText)findViewById(R.id.proprietorname_edit);
        address=(EditText)findViewById(R.id.address_edit);
        s=findViewById(R.id.spinner_businessedit);
        businessCategoryTable1= FirebaseDatabase.getInstance().getReference("city_business");
        descreiption=(EditText)findViewById(R.id.description_edit);
        submit=(Button)findViewById(R.id.submit_edit);
        spinner=(Spinner)findViewById(R.id.spinner_edit);
        mobile=(TextView)findViewById(R.id.mobile_edit);
        Business_details= FirebaseDatabase.getInstance().getReference("Business_Details");
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");
        query=Business_details.orderByChild("contact_number").equalTo(phonenumber);
        businessCategoryTable= FirebaseDatabase.getInstance().getReference("Categories");
        getSupportActionBar().setTitle("Business Edit");
        FirebaseDatabase.getInstance().getReference("Categories").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList.clear();//to prevent repititoin and again retrievin
                for(DataSnapshot categorysnapshot : dataSnapshot.getChildren())
                {
                    Upload upload = categorysnapshot.getValue(Upload.class);
                    String NAME = upload.getName().toString();
                    CategoryList.add(NAME);


                }
                ArrayAdapter adapter = new ArrayAdapter<>(Business_Edit.this,android.R.layout.simple_list_item_1,CategoryList);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    upload = dataSnapshot1.getValue(Upload.class);
                    firmname.setText(upload.getFirmname());
                    propriotorname.setText(upload.getProprietor_name());
                    address.setText(upload.getAddress());

                    descreiption.setText(upload.getDescription());
                    mobile.setText(upload.getContact_number());

                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("city_business").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList1.clear();//to prevent repititoin and again retrievin
                for(DataSnapshot categorysnapshot : dataSnapshot.getChildren())
                {
                    Upload upload = categorysnapshot.getValue(Upload.class);
                    String NAME = upload.getName().toString();
                    CategoryList1.add(NAME);


                }
                ArrayAdapter adapter1 = new ArrayAdapter<>(Business_Edit.this,android.R.layout.simple_list_item_1,CategoryList1);
                s.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            String address1=address.getText().toString();
                            String category1=spinner.getSelectedItem().toString();
                            String city1=s.getSelectedItem().toString();
                            String proprietor_name=propriotorname.getText().toString();
                            String description1=descreiption.getText().toString();
                            String firmname1=firmname.getText().toString();
                            if (!address1.equals("")&&!category1.equals("")&&!city1.equals("")&&!proprietor_name.equals("")&&!description1.equals("")&&!firmname1.equals("")) {
                                snapshot.getRef().child("address").setValue(address1);
                                snapshot.getRef().child("category").setValue(category1);
                                snapshot.getRef().child("city").setValue(city1);
                                snapshot.getRef().child("firmname").setValue(firmname1);
                                snapshot.getRef().child("proprietor_name").setValue(proprietor_name);
                                snapshot.getRef().child("description").setValue(description1);
                                Intent i1=new Intent(Business_Edit.this,Business_catalogue.class);
                                startActivity(i1);
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(Business_Edit.this);
                                builder.setTitle("Empty Field");
                                builder.setMessage("All Fields are required");
                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

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


            Intent i1 = new Intent(Business_Edit.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Business_Edit.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        if (id==R.id.about)
        {

            Intent i1 = new Intent(Business_Edit.this, About.class);
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
            Intent i1=new Intent(Business_Edit.this,Business_catalogue.class);
            startActivity(i1);
            finish();

        }

    }
}
