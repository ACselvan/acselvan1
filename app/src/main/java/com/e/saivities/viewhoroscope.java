package com.e.saivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class viewhoroscope extends AppCompatActivity {
static ImageView iv;
TextView t;
Button compare;
private PhotoView photoView;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewhoroscope);
        iv = findViewById(R.id.image);
        photoView=findViewById(R.id.detailed_horoscope);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        String image = getIntent().getStringExtra("image");
        compare=findViewById(R.id.compare_horoscope);
        editor = sharedPreferences.edit();
        Picasso.with(this).load(image).into(iv);
        Picasso.with(this).load("https://firebasestorage.googleapis.com/v0/b/saivities.appspot.com/o/Uploads%2Foie_28836155QfOT8Bp.jpg?alt=media&token=37acf9ba-1bdc-4703-8a34-e540be99e345").into(photoView);
        getSupportActionBar().setTitle("Horoscope");
        photoView.setVisibility(View.INVISIBLE);
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoView.setVisibility(View.VISIBLE);
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
        if (id==R.id.about)
        {

            Intent i1 = new Intent(viewhoroscope.this, About.class);
            startActivity(i1);

        }
        return super.onOptionsItemSelected(item);
    }
}
