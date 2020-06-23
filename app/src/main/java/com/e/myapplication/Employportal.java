package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Employportal extends AppCompatActivity {
    EditText q1,q2,q3,q4,q5,city_edit;
    Button b1,datepicker,timepicker;
    DatabaseReference Employer_details;
    String Namee,Qualificationn,Addresss,Dttmm,Numm,city="";
    TextView date_text,time_text;
    String datechar="",timechar="",exp,a1;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    DatabaseReference businessCategoryTable;
    List<String> CategoryList = new ArrayList<>();
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employportal);
        datepicker=(Button)findViewById(R.id.Datepicker);
        timepicker=(Button)findViewById(R.id.Timepicker);
        q1=findViewById(R.id.q1);
        q2=findViewById(R.id.q2);
        q3=findViewById(R.id.q3);
        q4=findViewById(R.id.q4);
        q5=findViewById(R.id.q5);
        getSupportActionBar().setTitle("Your Ad");
        date_text=(TextView)findViewById(R.id.date_text);
        time_text=(TextView)findViewById(R.id.time_text);
        b1=findViewById(R.id.b1);
        spinner=findViewById(R.id.spinner_job);
        businessCategoryTable= FirebaseDatabase.getInstance().getReference("city_business");
        sharedPreferences = getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        a1=sharedPreferences.getString("phonenumber","");
        Employer_details = FirebaseDatabase.getInstance().getReference("Employer_Details");
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick();
            }
        });
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePick();
                          }
        });
        businessCategoryTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList.clear();//to prevent repititoin and again retrievin
                for(DataSnapshot categorysnapshot : dataSnapshot.getChildren())
                {
                    Upload upload = categorysnapshot.getValue(Upload.class);
                    String NAME = upload.getName().toString();
                    CategoryList.add(NAME);


                }
                ArrayAdapter adapter = new ArrayAdapter<>(Employportal.this,android.R.layout.simple_list_item_1,CategoryList);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city=spinner.getSelectedItem().toString();
                Namee=q1.getText().toString().trim();
                Qualificationn=q2.getText().toString().trim();
                Addresss=q3.getText().toString().trim();
                //Dttmm=q4.getText().toString().trim();
                Numm=q5.getText().toString().trim();
                String id = Employer_details.push().getKey();
                Dttmm="Date:"+datechar+",  time:"+timechar;
                if (!Namee.equals("")&&!Qualificationn.equals("")&&!Addresss.equals("")&&!Numm.equals("")&&!datechar.equals("")&&!timechar.equals("")&&!city.equals(""))
                {
                Employportalupload Employportalupload = new Employportalupload(Namee,Qualificationn,Addresss,Dttmm,Numm,exp,city,a1,id);
                Employer_details.child(id).setValue(Employportalupload);
                city="";
                Intent i1=new Intent(Employportal.this,Work_Portal.class);
                startActivity(i1);
                finish();
                }

                else
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Employportal.this);
                    builder.setTitle("Empty Field");
                    builder.setMessage("Enter all fields");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                }
            }
        });
        }
        private void datePick()
        {
            Calendar calendar=Calendar.getInstance();
            int Year=calendar.get(Calendar.YEAR),Month=calendar.get(Calendar.MONTH),Date=calendar.get(Calendar.DATE);
            DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    String expiry=year+" "+month+" "+date;
                    Calendar calendar1=Calendar.getInstance();
                    calendar1.set(Calendar.YEAR,year);
                    calendar1.set(Calendar.MONTH,month);
                    calendar1.set(Calendar.DATE,date);
                    exp= (String) DateFormat.format("yyyyMMdd",calendar1);;
                     datechar= (String) DateFormat.format("EEEE,dd MMM yyyy",calendar1);
                    date_text.setText(datechar);
                }
            },  Year,Month,Date);
            datePickerDialog.show();

        }
        private void timePick()
        {
                    final Calendar calendar=Calendar.getInstance();
                    int Hour=calendar.get(Calendar.HOUR);
                    int Minute=calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(Calendar.HOUR,hour);
                calendar1.set(Calendar.MINUTE,minute);
                    Time tme = new Time(hour,minute,0);//seconds by default set to zero
                    Format formatter;
                    formatter = new SimpleDateFormat("h:mm a");
                 timechar=formatter.format(tme);
                time_text.setText(timechar);
                }
            },Hour,Minute,false);
            timePickerDialog.show();
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


            Intent i1 = new Intent(Employportal.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Employportal.this, Main2Activity.class);
            startActivity(i1);
            finish();
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
            Intent i1=new Intent(Employportal.this,Work_Portal.class);
            startActivity(i1);
            finish();
        }

    }
    }

