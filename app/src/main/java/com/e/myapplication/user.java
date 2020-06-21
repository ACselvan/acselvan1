package com.e.myapplication;

public class user {
    public String busss_exp,id,job_exp,mat_exp,mobile,name;
    public user() {
    }

    public user(String busss_exp, String id, String job_exp, String mat_exp, String mobile, String name) {
        this.busss_exp = busss_exp;
        this.id = id;
        this.job_exp = job_exp;
        this.mat_exp = mat_exp;
        this.mobile = mobile;
        this.name = name;
    }

    public String getBusss_exp() {
        return busss_exp;
    }

    public void setBusss_exp(String busss_exp) {
        this.busss_exp = busss_exp;
    }

    public String getJob_exp() {
        return job_exp;
    }

    public void setJob_exp(String job_exp) {
        this.job_exp = job_exp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMat_exp() {
        return mat_exp;
    }

    public void setMat_exp(String mat_exp) {
        this.mat_exp = mat_exp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
