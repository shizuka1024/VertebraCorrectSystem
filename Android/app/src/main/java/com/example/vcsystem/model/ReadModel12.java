package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ReadModel12 implements Serializable {
    @Exclude
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer lbs12;

    public Integer getLbs12() {
        return lbs12;
    }

    public void setLbs12(Integer lbs12) {
        this.lbs12 = lbs12;
    }

    public ReadModel12(Long lbs12) {
        this.lbs12 = Math.toIntExact(lbs12);
    }
}
