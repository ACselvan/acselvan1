package com.e.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload>mUploads;
    String Name, Age, Sex, Height, Income, Education, Fathersname, Mothersname, Siblings, cellno,job,company,imageurl;


    public ImageAdapter(Context context,List<Upload>uploads)
    {
        this.mContext= context;
        this.mUploads = uploads;
    }

    public ImageAdapter(String namee, String agee, String sexx, String heightt, String incomee, String educationn, String jobb, String educationn1, String mnn, String fnn, String sbll, String t100, String company, String imageurl) {


        this.Name = namee;
        this.Sex = sexx;
        this.Age = agee;
        this.Height = heightt;
        this.Income = incomee;
        this.Education = educationn;
        this.Fathersname = fnn;
        this.Mothersname = mnn;
        this.Siblings = sbll;
        this.cellno = t100;
        this.job=jobb;
        this.company = company;
        this.imageurl=imageurl;
    }


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Upload> getmUploads() {
        return mUploads;
    }

    public void setmUploads(List<Upload> mUploads) {
        this.mUploads = mUploads;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getIncome() {
        return Income;
    }

    public void setIncome(String income) {
        Income = income;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        Education = education;
    }

    public String getFathersname() {
        return Fathersname;
    }

    public void setFathersname(String fathersname) {
        Fathersname = fathersname;
    }

    public String getMothersname() {
        return Mothersname;
    }

    public void setMothersname(String mothersname) {
        Mothersname = mothersname;
    }

    public String getSiblings() {
        return Siblings;
    }

    public void setSiblings(String siblings) {
        Siblings = siblings;
    }

    public String getCellno() {
        return cellno;
    }

    public void setCellno(String cellno) {
        this.cellno = cellno;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_horoscopedisplay,parent,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(v);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder( @NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.text.setText(uploadCurrent.getName());
        Picasso.with(mContext)
                .load(uploadCurrent.getImageurl())
                .resize(1000,1350)
                //.fit()
                //  .resizeDimen(1080,1080)
                //.centerInside()
                // .centerCrop()
                .into(holder.ima);


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView text;
        public ImageView ima;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
           // likee = itemView.findViewById(R.id.likee);

            //text = itemView.findViewById(R.id.text);
            ima = itemView.findViewById(R.id.ivhoroscope);

        }
    }


}
