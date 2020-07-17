package com.e.saivities;

public class Employer_Details {
    private String address,dttm,name,num,qualification,exp,city,phone,id;
    public Employer_Details() {
    }


    public Employer_Details(String address, String dttm, String name, String num, String qualification,String exp,String city,String phone,String id) {
        this.address = address;
        this.dttm = dttm;
        this.name = name;
        this.num = num;
        this.qualification = qualification;
        this.exp = exp;
        this.city=city;
        this.phone=phone;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDttm() {
        return dttm;
    }

    public void setDttm(String dttm) {
        this.dttm = dttm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
