package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class ReadModelmFHP implements Serializable {
    @Exclude
    private String id;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer mFHP;

    public Integer getmFHP() {
        return mFHP;
    }

    public void setmFHP(Integer mFHP) {
        this.mFHP = mFHP;
    }

    public ReadModelmFHP(Long mFHP) {
        this.mFHP = Math.toIntExact(mFHP);
    }
}
