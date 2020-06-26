package  com.e.myapplication;

import android.location.Address;

import com.google.firebase.database.DatabaseReference;

import java.util.jar.Attributes;

class up1 {
    String Name, Age, Sex, Height, Income, Education, Fathersname, Mothersname, Siblings, cellno,job,companyy,imageurl,profileImage,city;
    DatabaseReference dref;

    public up1(String name) {
        this.Name = name;
    }


    public up1() {
    }
    public up1(String namee, String agee, String sexx, String heightt, String incomee, String educationn, String jobb, String educationn1, String mnn, String fnn, String sbll, String t100, String companyy, String imageurl, String profileImage, DatabaseReference matrimony_details,String city) {
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
        this.companyy = companyy;
        this.imageurl = imageurl;
        this.profileImage=profileImage;
        this.dref=matrimony_details;
        this.city=city;

    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompanyy() {
        return companyy;
    }

    public void setCompanyy(String company) {
        this.companyy = company;
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
}
