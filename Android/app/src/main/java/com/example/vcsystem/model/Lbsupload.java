package com.example.vcsystem.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Lbsupload {

    private String userEmail;
    private int lbs60, lbs49, lbs40, lbs27, lbs12;
    @ServerTimestamp
    private Date dateSent;
    Date date = new Date();
    //設定日期格式
    SimpleDateFormat sdfhaveh = new SimpleDateFormat("yyyy-MM-dd HH");
    //進行轉換
    String dateSent3 = sdfhaveh.format(date);

    public Lbsupload(String mEmail, int mlbs12, int mlbs27, int mlbs40, int mlbs49, int mlbs60) {
        this.userEmail = mEmail;
        this.lbs12 = mlbs12;
        this.lbs27 = mlbs27;
        this.lbs40 = mlbs40;
        this.lbs49 = mlbs49;
        this.lbs60 = mlbs60;
    }

    public String getuserEmail() {
        return userEmail;
    }

    public void setSenderName(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getlbs12() {
        return lbs12;
    }

    public void setlbs12(int lbs12) {
        this.lbs12 = lbs12;
    }

    public int getlbs27() {
        return lbs27;
    }

    public void setlbs27(int lbs27) {
        this.lbs12 = lbs27;
    }

    public int getlbs40() {
        return lbs40;
    }

    public void setlbs40(int lbs40) {
        this.lbs40 = lbs40;
    }

    public int getlbs49() {
        return lbs49;
    }

    public void setlbs49(int lbs49) {
        this.lbs49 = lbs49;
    }

    public int getlbs60() {
        return lbs60;
    }

    public void setlbs60(int lbs60) {
        this.lbs60 = lbs60;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getDateSent3() {
        return dateSent3;
    }

    public void setDateSent3(String dateSent3) {
        this.dateSent3 = dateSent3;
    }
}
