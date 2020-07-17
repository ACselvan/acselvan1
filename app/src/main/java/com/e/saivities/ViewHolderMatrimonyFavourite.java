package com.e.saivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewHolderMatrimonyFavourite extends RecyclerView.Adapter<ViewHolderMatrimonyFavourite.myViewHolder> {
    private Context context;
    private ArrayList<up1> up;
    private String phonenumber,mobile;
    private Query query,query1;
    private DatabaseReference mDatabase,per=FirebaseDatabase.getInstance().getReference("mat_fav"),use=FirebaseDatabase.getInstance().getReference("user");
    public ViewHolderMatrimonyFavourite(Context context, ArrayList<up1> up, String phonenumber) {
        this.context = context;
        this.up = up;
        this.phonenumber = phonenumber;
    }

    @NonNull
    @Override
    public ViewHolderMatrimonyFavourite.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderMatrimonyFavourite.myViewHolder(LayoutInflater.from(context).inflate(R.layout.matrimony_item_demo,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderMatrimonyFavourite.myViewHolder holder, final int position) {
        per.keepSynced(true);
        use.keepSynced(true);
        query=FirebaseDatabase.getInstance().getReference("mat_fav").child(phonenumber);
        mobile=up.get(position).getCellno();
        mDatabase = FirebaseDatabase.getInstance().getReference("mat_fav");
        query1=mDatabase.child(phonenumber).orderByChild("mobile").equalTo(up.get(position).getCellno());
        holder.name.setText(up.get(position).getName());
        holder.job.setText(up.get(position).getJob());
        holder.company.setText(up.get(position).getCompanyy());
        holder.income.setText(up.get(position).getIncome());

        Picasso.with(context).load(up.get(position).getProfileImage()).into(holder.iv);
        holder.viewhoroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(context,viewhoroscope.class);
                i1.putExtra("image",up.get(position).getImageurl());
                context.startActivity(i1);
            }
        });
        FirebaseDatabase.getInstance().getReference("mat_fav").child(phonenumber).orderByChild("mobile").equalTo(up.get(position).getCellno()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                {
                    //holder.favourite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    holder.name.setVisibility(View.INVISIBLE);
                    holder.job.setVisibility(View.INVISIBLE);
                    holder.company.setVisibility(View.INVISIBLE);
                    holder.income.setVisibility(View.INVISIBLE);

                    holder.iv.setVisibility(View.INVISIBLE);
                    holder.viewhoroscope.setVisibility(View.INVISIBLE);
                    holder.favourite.setVisibility(View.INVISIBLE);
                    holder.view_details.setVisibility(View.INVISIBLE);

                }else
                {
                    holder.favourite.setBackgroundResource(R.drawable.favourite);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
holder.view_details.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i1=new Intent(context,Matrimony_View_Details.class);
        i1.putExtra("phonenumber",up.get(position).getCellno());
        context.startActivity(i1);
    }
});
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference("user").orderByChild("mobile").equalTo(phonenumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseDatabase.getInstance().getReference("mat_fav").child(phonenumber).orderByChild("mobile").equalTo(up.get(position).getCellno()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0)
                                {
                                    String id=mDatabase.push().getKey();
                                    mDatabase.child(phonenumber).child(id).child("mobile").setValue(up.get(position).getCellno());
                                    holder.favourite.setBackgroundResource(R.drawable.favourite);
                                }else
                                {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                    builder.setTitle("Remove user from favourites");
                                    builder.setMessage("Do you want to remove this user from favourite?");
                                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            mDatabase.child(phonenumber).orderByChild("mobile").equalTo(up.get(position).getCellno()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                        appleSnapshot.getRef().removeValue();
                                                        holder.favourite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                                                        Toast.makeText(context,"profile removed from favourites successfully",Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.create().show();
                                    // holder.favourite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        // String id=mDatabase.push().getKey();
                        //mDatabase.child(phonenumber).child(id).child("mobile").setValue(up.get(position).getCellno());
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
        return up.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView name,job,company,income;
        ImageView iv;
        Button viewhoroscope,favourite,view_details;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            job=(TextView)itemView.findViewById(R.id.job);
            company=(TextView)itemView.findViewById(R.id.company);
            income=(TextView)itemView.findViewById(R.id.income);

            iv=(ImageView)itemView.findViewById(R.id.iv);
            viewhoroscope=(Button)itemView.findViewById(R.id.view_horoscope);
            favourite=(Button)itemView.findViewById(R.id.fav);
            view_details=(Button)itemView.findViewById(R.id.view_details);

        }
    }
}
