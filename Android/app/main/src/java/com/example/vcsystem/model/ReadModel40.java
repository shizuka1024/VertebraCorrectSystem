package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ReadModel40 implements Serializable {
    @Exclude
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer lbs40;

    public Integer getLbs40() {
        return lbs40;
    }

    public void setLbs40(Integer lbs40) {
        this.lbs40 = lbs40;
    }

    public ReadModel40(Long lbs40) {
        this.lbs40 = Math.toIntExact(lbs40);
    }
}
