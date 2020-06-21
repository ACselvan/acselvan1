package com.e.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewHolderJob extends RecyclerView.Adapter<ViewHolderJob.myViewHolder> {
    private Context context;
    private ArrayList<Employer_Details> emp;

    public ViewHolderJob(Context context, ArrayList<Employer_Details> emp) {
        this.context = context;
        this.emp = emp;
    }

    @NonNull
    @Override
    public ViewHolderJob.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderJob.myViewHolder(LayoutInflater.from(context).inflate(R.layout.job_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJob.myViewHolder holder, int position) {
        holder.name_job.setText(emp.get(position).getName());
        holder.qualification_job.setText(emp.get(position).getQualification());
        holder.date_job.setText(emp.get(position).getDttm());
        holder.number_job.setText(emp.get(position).getNum());
        holder.address_job.setText(emp.get(position).getAddress());
        holder.city.setText(emp.get(position).getCity());
       // Picasso.with(context).load(list.get(position).getImageurl()).into(holder.job_image);
    }

    @Override
    public int getItemCount() {
        return emp.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView name_job,qualification_job,date_job,number_job,address_job,city;
        //ImageView job_image;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name_job=(TextView)itemView.findViewById(R.id.name_job);
            qualification_job=(TextView)itemView.findViewById(R.id.qualification_job);
            date_job=(TextView)itemView.findViewById(R.id.date_job);
            number_job=(TextView)itemView.findViewById(R.id.number_job);
            address_job=(TextView)itemView.findViewById(R.id.address_job);
            city=(TextView)itemView.findViewById(R.id.city_job_item);
            //job_image=(ImageView)itemView.findViewById(R.id.job_image);
        }
    }
}
