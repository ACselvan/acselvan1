package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class viewhoroscope extends AppCompatActivity {
static ImageView iv;
TextView t;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewhoroscope);
        iv = findViewById(R.id.image);
        t = findViewById(R.id.t);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        String image = getIntent().getStringExtra("image");
        t.setText(image);
        editor = sharedPreferences.edit();
        Picasso.with(this).load(image).into(iv);
       // iv.setImageURI(Uri.parse(image));

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


            Intent i1 = new Intent(viewhoroscope.this, logIn.class);

            startActivity(i1);
            finish();
        }
        if (id==R.id.home1)
        {
            Intent i1 = new Intent(viewhoroscope.this, Main2Activity.class);
            startActivity(i1);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
