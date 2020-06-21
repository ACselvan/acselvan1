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

public class Matrimony_Image_Edit extends AppCompatActivity {
ImageView horoscope_image,profile_image;
Button get_horoscope,upload_horoscope,getimage,uploadimage;
    private static final int PICK_IMAGE_REQUEST = 7;
    private static final int PICK_IMAGE_REQUESTT = 7;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    Uri ImageUri,ImageUri2;
    private StorageReference st,stt;
    private int i;
    DatabaseReference Matrimony_details;
    private String phonenumber,HoroscopeImage,profileImage;
    private up1 up;
    private ProgressBar pb,pb1;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony__image__edit);
        horoscope_image=(ImageView)findViewById(R.id.iv_edit);
        profile_image=(ImageView)findViewById(R.id.iv1_edit);
        upload_horoscope=(Button)findViewById(R.id.uploadhoroscope_edit) ;
        get_horoscope=(Button)findViewById(R.id.gethoroscope) ;
        getimage=(Button)findViewById(R.id.uploadimage_edit) ;
        uploadimage=(Button)findViewById(R.id.uploadprofile_edit) ;
        pb=(ProgressBar)findViewById(R.id.pb_edit);
        pb1=(ProgressBar)findViewById(R.id.pb1_edit);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        phonenumber=sharedPreferences.getString("phonenumber","");
        Matrimony_details = FirebaseDatabase.getInstance().getReference("Matrimony_Details");
        stt = FirebaseStorage.getInstance().getReference("Uploads/profilephoto");

        st = FirebaseStorage.getInstance().getReference("Uploads/Horoscope");
        query=Matrimony_details.orderByChild("cellno").equalTo(phonenumber);
        upload_horoscope.setVisibility(View.INVISIBLE);
        uploadimage.setVisibility(View.INVISIBLE);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    up=dataSnapshot1.getValue(up1.class);
                    Picasso.with(getApplicationContext()).load(up.getImageurl()).into(horoscope_image);
                    Picasso.with(getApplicationContext()).load(up.getProfileImage()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        get_horoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;

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
                            snapshot.getRef().child("profileImage").setValue(profileImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        upload_horoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().child("imageurl").setValue(HoroscopeImage);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1;
                fileChooser2();
            }
        });
    }
    private void fileChooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);

    }
    private  String getFileExtension2(Uri uri)
    {
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private  String getFileExtension(Uri uri)
    {
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void fileChooser2() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUESTT);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (i == 0) {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                ImageUri = data.getData();
                Picasso.with(this).load(ImageUri).into(horoscope_image);
                horoscope_image.setImageURI(ImageUri);


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
                                                    pb.setProgress(0);
                                                    Toast.makeText(Matrimony_Image_Edit.this,"Horoscope Uploaded Succesfully",Toast.LENGTH_SHORT).show();

                                                }
                                            },500);
                                            HoroscopeImage = uri.toString();
                                            upload_horoscope.setVisibility(View.VISIBLE);

                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Matrimony_Image_Edit.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                            pb.setProgress((int)progress);
                        }
                    });

                }


            }

        }


        if(i!=0) {
            if (requestCode == PICK_IMAGE_REQUESTT && resultCode == RESULT_OK && data != null && data.getData() != null) {
                ImageUri2 = data.getData();
                Picasso.with(this).load(ImageUri2).into(profile_image);
                profile_image.setImageURI(ImageUri2);


                if(ImageUri2 != null) {
                    final StorageReference fileReference2 = stt.child(System.currentTimeMillis() + "." + getFileExtension2(ImageUri2));

                    fileReference2.putFile(ImageUri2)

                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                                            Handler handlerr =new Handler();
                                            handlerr.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb1.setProgress(0);
                                                    Toast.makeText(Matrimony_Image_Edit.this,"profilephoto Uploaded Succesfully",Toast.LENGTH_SHORT).show();

                                                }
                                            },500);
                                            profileImage = uri.toString();
                                            uploadimage.setVisibility(View.VISIBLE);

                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Matrimony_Image_Edit.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progresss = (100.0 * taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                            pb1.setProgress((int)progresss);
                        }
                    });





                }
            }}
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


            Intent i1 = new Intent(Matrimony_Image_Edit.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(Matrimony_Image_Edit.this, Main2Activity.class);
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
            Intent i1=new Intent(Matrimony_Image_Edit.this,Matrimony_info.class);
            startActivity(i1);
            finish();

        }

    }
}
