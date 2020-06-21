package com.e.myapplication;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;
import static com.e.myapplication.viewhoroscope.iv;

public class ViewHolder extends RecyclerView.Adapter<ViewHolder.myViewHolder> {
    private DatabaseReference mDatabase,per=FirebaseDatabase.getInstance().getReference("Business_fav"),use=FirebaseDatabase.getInstance().getReference("user");

    Context context;
    ArrayList<Upload> upload;
    String phonenumber;

    public ViewHolder(Context context, ArrayList<Upload> upload, String phonenumber) {
        this.context = context;
        this.upload = upload;
        this.phonenumber=phonenumber;
    }

    @NonNull
    @Override
    // public ViewHolder.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    public ViewHolder.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder.myViewHolder(LayoutInflater.from(context).inflate(R.layout.categories_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder.myViewHolder holder, final int position) {
        per.keepSynced(true);
        use.keepSynced(true);
        mDatabase = FirebaseDatabase.getInstance().getReference("Business_fav");
            holder.firmname.setText(upload.get(position).getFirmname());
            holder.proprietor_name.setText(upload.get(position).getProprietor_name());
            holder.conduct_number.setText(upload.get(position).getContact_number());
            holder.city.setText(upload.get(position).getCity());
            holder.address.setText(upload.get(position).getAddress());
            holder.description.setText(upload.get(position).getDescription());
        Picasso.with(context).load(upload.get(position).getImageurl()).into(holder.iv);
        FirebaseDatabase.getInstance().getReference("Business_fav").child(phonenumber).orderByChild("mobile").equalTo(upload.get(position).getContact_number()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                {
                    holder.favourite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                }else
                {
                    holder.favourite.setBackgroundResource(R.drawable.favourite);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference("user").orderByChild("mobile").equalTo(phonenumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseDatabase.getInstance().getReference("Business_fav").child(phonenumber).orderByChild("mobile").equalTo(upload.get(position).getContact_number()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0)
                                {
                                    String id=mDatabase.push().getKey();
                                    mDatabase.child(phonenumber).child(id).child("mobile").setValue(upload.get(position).getContact_number());
                                    holder.favourite.setBackgroundResource(R.drawable.favourite);
                                }else
                                {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                    builder.setTitle("Remove user from favourites");
                                    builder.setMessage("Do you want to remove this user from favourite?");
                                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            mDatabase.child(phonenumber).orderByChild("mobile").equalTo(upload.get(position).getContact_number()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        return upload.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView firmname,proprietor_name,conduct_number,city,address,description;
        ImageView iv;
        Button favourite;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            firmname=(TextView)itemView.findViewById(R.id.firmname);
            proprietor_name=(TextView)itemView.findViewById(R.id.proprietor_name);
            conduct_number=(TextView)itemView.findViewById(R.id.conduct_number);
            city=(TextView)itemView.findViewById(R.id.city);
            address=(TextView)itemView.findViewById(R.id.address);
            description=itemView.findViewById(R.id.description);
            iv=(ImageView)itemView.findViewById(R.id.iv);
            favourite=(Button)itemView.findViewById(R.id.business_fav);
        }
    }
}
