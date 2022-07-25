package com.example.vcsystem.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FHPupload {
    private String userEmail;
    private int NoFHP, sFHP, mFHP;
    @ServerTimestamp
    private Date dateSent;
    Date date = new Date();
    SimpleDateFormat sdfhave = new SimpleDateFormat("yyyy-MM-dd");
    //進行轉換
    String dateSent4 = sdfhave.format(date);

    public FHPupload(String userEmail,int NoFHP, int sFHP, int mFHP) {
        this.userEmail = userEmail;
        this.NoFHP = NoFHP;
        this.sFHP = sFHP;
        this.mFHP = mFHP;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getNoFHP() {
        return NoFHP;
    }

    public void setNoFHP(int noFHP) {
        NoFHP = noFHP;
    }

    public int getsFHP() {
        return sFHP;
    }

    public void setsFHP(int sFHP) {
        this.sFHP = sFHP;
    }

    public int getmFHP() {
        return mFHP;
    }

    public void setmFHP(int mFHP) {
        this.mFHP = mFHP;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getDateSent4() {
        return dateSent4;
    }

    public void setDateSent4(String dateSent4) {
        this.dateSent4 = dateSent4;
    }
}
