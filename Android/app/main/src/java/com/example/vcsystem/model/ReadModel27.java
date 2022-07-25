package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ReadModel27 implements Serializable {
    @Exclude
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer lbs27;

    public Integer getLbs27() {
        return lbs27;
    }

    public void setLbs27(Integer lbs27) {
        this.lbs27 = lbs27;
    }

    public ReadModel27(Long lbs27) {
        this.lbs27 = Math.toIntExact(lbs27);
    }
}
