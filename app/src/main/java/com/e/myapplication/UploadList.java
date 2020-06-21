package com.e.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

class UploadList extends ArrayAdapter<Upload> {
    Activity context;
    List<Upload>uploads;
    DatabaseReference business_details;

    public UploadList(Activity context, List<Upload> uploads, DatabaseReference business_details) {
        super(context,R.layout.categories_item, uploads);
        this.context =context;
        this.uploads = uploads;
        this.business_details=business_details;

    }

    public View getView(int pos, View view, ViewGroup v)
    {
        final View listViewItem = LayoutInflater.from(context).inflate(R.layout.categories_item,(ViewGroup) view ,false);
        TextView name = listViewItem.findViewById((R.id.name));
        TextView prop_name = listViewItem.findViewById((R.id.job));

        TextView address = listViewItem.findViewById((R.id.name));
        TextView phone = listViewItem.findViewById((R.id.income));
        TextView description = listViewItem.findViewById((R.id.phone));

        final Upload upload = uploads.get(pos);
        phone.setText(upload.getFirmname());
        address.setText(upload.getAddress());
        prop_name.setText(upload.getName());
        name.setText(upload.getContact_number());
        description.setText(upload.getDescription());
        return listViewItem;




    }
}
