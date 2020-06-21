package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.e.myapplication.viewhoroscope.iv;

public class Getting_Business_details extends AppCompatActivity {
    Spinner s;
    private static final int PICK_IMAGE_REQUEST = 7;
   TextView r3,r5;
    EditText t7,t8,t9,t10,r4,r6,city_business;
    Button b2;
    Uri ImageUri;
    String imageurl,city="",phonenumber;
    String Firmname,Address,Timing,Contact_number,category,Description,Proprietor_name;
    DatabaseReference Business_details;
    private ImageView image_business;
    private ProgressBar image_progress;
    private Button get_image;
    DatabaseReference businessCategoryTable;
    List<String> CategoryList = new ArrayList<>();
    private StorageReference st,stt;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Spinner spinner;
    DatabaseReference businessCategoryTable1;
    List<String> CategoryList1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting__business_details);
        s=findViewById(R.id.s);
        image_business=(ImageView) findViewById(R.id.business_image);
        image_progress=(ProgressBar)findViewById(R.id.business_progress);
        get_image=(Button)findViewById(R.id.get_image_business);
        spinner=findViewById(R.id.spinner_business);
        t8=findViewById(R.id.t8);
        //t9=findViewById(R.id.t9);
        t10=findViewById(R.id.t10);
        b2=findViewById(R.id.b2);
        r3=findViewById(R.id.r3);
        r5=findViewById(R.id.r5);
        r4=findViewById(R.id.r4);
        r6=findViewById(R.id.r6);
        businessCategoryTable1= FirebaseDatabase.getInstance().getReference("city_business");
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");

        Business_details= FirebaseDatabase.getInstance().getReference("Business_Details");
        businessCategoryTable= FirebaseDatabase.getInstance().getReference("Categories");
        st = FirebaseStorage.getInstance().getReference("Uploads/business");
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
                ArrayAdapter adapter = new ArrayAdapter<>(Getting_Business_details.this,android.R.layout.simple_list_item_1,CategoryList);
                s.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        businessCategoryTable1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList1.clear();//to prevent repititoin and again retrievin
                for(DataSnapshot categorysnapshot : dataSnapshot.getChildren())
                {
                    Upload upload = categorysnapshot.getValue(Upload.class);
                    String NAME = upload.getName().toString();
                    CategoryList1.add(NAME);


                }
                ArrayAdapter adapter = new ArrayAdapter<>(Getting_Business_details.this,android.R.layout.simple_list_item_1,CategoryList1);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               category= s.getSelectedItem().toString();
              //  Contact_number=t7.getText().toString().trim();
                Contact_number=phonenumber;
                        Proprietor_name=t8.getText().toString().trim();
                        Address=r6.getText().toString().trim();
                        Description=r4.getText().toString().trim();
          //  Timing=t9.getText().toString().trim();
                        Firmname=t10.getText().toString().trim();
            city=spinner.getSelectedItem().toString();
            if (!category.equals("")&&!Contact_number.equals("")&&!Proprietor_name.equals("")&&!Address.equals("")&&!Description.equals("")&&!Firmname.equals("")&&!city.equals("")) {


                String id = Business_details.push().getKey();
                // Upload upload=new Upload(Firmname,Address,Contact_number,category,Description,Proprietor_name);
                Upload upload = new Upload(Address, Contact_number, category, Firmname, Description, Proprietor_name, imageurl, city);
                Business_details.child(id).setValue(upload);
                city="";
                Intent i1=new Intent(Getting_Business_details.this,Business_catalogue.class);
                startActivity(i1);
                finish();
            }
else {
                AlertDialog.Builder builder=new AlertDialog.Builder(Getting_Business_details.this);
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
        });

    }
    private void fileChooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                ImageUri = data.getData();
                Picasso.with(this).load(ImageUri).into(image_business);
                image_business.setImageURI(ImageUri);


                if(ImageUri != null) {
                    final StorageReference fileReference = st.child(System.currentTimeMillis() + "." + getFileExtension(ImageUri));

                    fileReference.putFile(ImageUri)

                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                                            Handler handler =new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    image_progress.setProgress(0);
                                                    //Toast.makeText(Getting_Business_details.this,"Horoscope Uploaded Succesfully",Toast.LENGTH_SHORT).show();

                                                }
                                            },500);
                                            imageurl = uri.toString();
                                           // Toast.makeText(Getting_Business_details.this,uri.toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Getting_Business_details.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                            image_progress.setProgress((int)progress);
                        }
                    });

                }


            }





    }
    private  String getFileExtension(Uri uri)
    {
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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


            Intent i1 = new Intent(Getting_Business_details.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Getting_Business_details.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
