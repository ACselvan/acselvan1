package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class horoscope extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 7;
    EditText name;
    ProgressBar pb;
    Button upload;
    ImageView iv;
    Uri ImageUri;
    String user_id;
    private StorageReference st;
    private DatabaseReference db;
    String r;
    private String Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope);
        upload =findViewById(R.id.upload);
        name = findViewById(R.id.name);
        pb = findViewById(R.id.pb);
        iv = findViewById(R.id.iv);

        SharedPreferences sh
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);

        user_id = sh.getString("name", "");



        st = FirebaseStorage.getInstance().getReference("Uploads/");
        db= FirebaseDatabase.getInstance().getReference("Uploads/");

        fileChooser();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFile();
            }
        });
    }
    private void fileChooser()
    {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data !=null && data.getData()!= null)

        {
            ImageUri =data.getData();
            Picasso.with(this).load(ImageUri).into(iv);
            iv.setImageURI(ImageUri);
        }

    }

    private  String getFileExtension(Uri uri)
    {
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private  void UploadFile(){

        if(ImageUri != null)
        {
            final StorageReference fileReference = st.child(System.currentTimeMillis()+"."+ getFileExtension(ImageUri));

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
                                            Toast.makeText(horoscope.this,"Upload Succesfully",Toast.LENGTH_SHORT).show();

                                        }
                                    },500);


                                    Upload upload;
                                    upload = new Upload(name.getText().toString().trim(),uri.toString());
// Retrieving the value using its keys
// the file name must be same in both saving
// and retrieving the data

                                    String UploadId = db.push().getKey();
                                    db.child(UploadId).setValue(upload);
                                    Intent i = new Intent(horoscope.this,viewhoroscope.class);
                                    startActivity(i);

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(horoscope.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                            pb.setProgress((int)progress);
                        }
                    });
        }
        else
        {
            Toast.makeText(this,"No filee selected",Toast.LENGTH_SHORT).show();
        }




    }



}
