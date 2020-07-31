package com.e.saivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewHolderJobExisting extends RecyclerView.Adapter<ViewHolderJobExisting.myViewHolder> {
    private Context context;
    private ArrayList<Employer_Details> emp;

    public ViewHolderJobExisting(Context context, ArrayList<Employer_Details> emp) {
        this.context = context;
        this.emp = emp;
    }

    @NonNull
    @Override
    public ViewHolderJobExisting.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderJobExisting.myViewHolder(LayoutInflater.from(context).inflate(R.layout.job_exiting_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJobExisting.myViewHolder holder, final int position) {

        holder.name_job.setText(emp.get(position).getName());
        holder.qualification_job.setText(emp.get(position).getQualification());
        holder.date_job.setText(emp.get(position).getDttm());
        holder.number_job.setText(emp.get(position).getNum());
        holder.address_job.setText(emp.get(position).getAddress());
        holder.phonenumber.setText(emp.get(position).getPhone());
        holder.description.setText(emp.get(position).getDescription());
        holder.city.setText(emp.get(position).getCity());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = FirebaseDatabase.getInstance().getReference("Employer_Details").orderByChild("id").equalTo(emp.get(position).getId());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                            dataSnapshot1.getRef().removeValue();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return emp.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView name_job,qualification_job,date_job,number_job,address_job,city,phonenumber,description;
        Button delete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name_job=(TextView)itemView.findViewById(R.id.name_job1);
            qualification_job=(TextView)itemView.findViewById(R.id.qualification_job1);
            date_job=(TextView)itemView.findViewById(R.id.date_job1);
            number_job=(TextView)itemView.findViewById(R.id.number_job1);
            address_job=(TextView)itemView.findViewById(R.id.address_job1);
            phonenumber=(TextView)itemView.findViewById(R.id.phonenumber_job1);
            description=(TextView)itemView.findViewById(R.id.description_job_item1);
            city=(TextView)itemView.findViewById(R.id.city_job_item1);
            delete=(Button)itemView.findViewById(R.id.job_existing_delete);
        }
    }
}
