package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.net.Uri;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Business_Image_Edit extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private static final int PICK_IMAGE_REQUEST = 7;
    DatabaseReference Business_details,businessCategoryTable;
    String phonenumber;
    Query query;
    Uri ImageUri;
    Upload upload;
    String imageurl;
    Button getimage,uploadimage;
    private StorageReference st;
    ImageView image;
    private ProgressBar image_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__image__edit);
        Business_details= FirebaseDatabase.getInstance().getReference("Business_Details");
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");
        query=Business_details.orderByChild("contact_number").equalTo(phonenumber);
        image=(ImageView)findViewById(R.id.business_image_edit);
        getimage=(Button)findViewById(R.id.get_image_business_edit);
        uploadimage=(Button)findViewById(R.id.upload_edit);
        st = FirebaseStorage.getInstance().getReference("Uploads/business");
        image_progress=(ProgressBar)findViewById(R.id.business_progress_edit);
        uploadimage.setVisibility(View.INVISIBLE);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    upload = dataSnapshot1.getValue(Upload.class);
                    Picasso.with(getApplicationContext()).load(upload.getImageurl()).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().child("imageurl").setValue(imageurl);
                            Intent i1=new Intent(Business_Image_Edit.this,Business_info.class);
                            startActivity(i1);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    private void fileChooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);

    }
    private  String getFileExtension(Uri uri)
    {
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            Picasso.with(this).load(ImageUri).into(image);
            image.setImageURI(ImageUri);


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
                                        uploadimage.setVisibility(View.VISIBLE);
                                        // Toast.makeText(Getting_Business_details.this,uri.toString(),Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Business_Image_Edit.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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


            Intent i1 = new Intent(Business_Image_Edit.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Business_Image_Edit.this, Main2Activity.class);
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
            Intent i1=new Intent(Business_Image_Edit.this,Business_catalogue.class);
            startActivity(i1);
            finish();

        }

    }
}
