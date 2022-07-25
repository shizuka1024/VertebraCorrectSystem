package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ReadModel60 implements Serializable {
    @Exclude
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer lbs60;

    public Integer getLbs60() {
        return lbs60;
    }

    public void setLbs60(Integer lbs60) {
        this.lbs60 = lbs60;
    }

    public ReadModel60(Long lbs60) {
        this.lbs60 = Math.toIntExact(lbs60);
    }
}
