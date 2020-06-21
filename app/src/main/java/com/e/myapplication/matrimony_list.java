package com.e.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.e.myapplication.R.layout.matrimony_item;

public class matrimony_list  extends ArrayAdapter<up1> {
    Context context;
    List<up1> up;
    DatabaseReference Matrimony_Details;
    String image;

    public matrimony_list(Context context, List<up1> up, DatabaseReference Matrimony_Details) {
        super(context, R.layout.matrimony_item,up);
        this.context =context;
        this.up = up;
        this.Matrimony_Details=Matrimony_Details;

    }


    public View getView(int pos, View view, ViewGroup v)
    {
        final View listViewItem = LayoutInflater.from(context).inflate(matrimony_item,(ViewGroup) view ,false);
        TextView name = listViewItem.findViewById((R.id.name));
        TextView job = listViewItem.findViewById((R.id.job));
        ImageView iv = listViewItem.findViewById(R.id.iv);
        Button viewhoroscope=listViewItem.findViewById(R.id.view_horoscope);
        final TextView company = listViewItem.findViewById((R.id.company));
        TextView income = listViewItem.findViewById((R.id.income));
        TextView phonenumber = listViewItem.findViewById((R.id.phone));

        final up1 ups = up.get(pos);
        name.setText(ups.getName());
        job.setText(ups.getJob());
        String profilephoto = ups.getProfileImage();
        Picasso.with(context).load(ups.getProfileImage()).into(iv);
      //  iv.setImageURI(Uri.parse(profilephoto));
        company.setText(ups.getImageurl());
        income.setText(ups.getIncome());
        phonenumber.setText(ups.getCellno());
        viewhoroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                image=ups.getImageurl();

                b.putString("image",image);
                Intent i= new Intent(getContext(),viewhoroscope.class);
                i.putExtras(b);
                context.startActivity(i);
            }
        });
        return listViewItem;




    }

}
