package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ReadModel49 implements Serializable {
    @Exclude
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer lbs49;

    public Integer getLbs49() {
        return lbs49;
    }

    public void setLbs49(Integer lbs49) {
        this.lbs49 = lbs49;
    }

    public ReadModel49(Long lbs49) {
        this.lbs49 = Math.toIntExact(lbs49);
    }
}
