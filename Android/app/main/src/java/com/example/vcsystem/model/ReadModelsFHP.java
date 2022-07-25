package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ReadModelsFHP implements Serializable {
    @Exclude
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer sFHP;

    public Integer getsFHP() {
        return sFHP;
    }

    public void setsFHP(Integer sFHP) {
        this.sFHP = sFHP;
    }

    public ReadModelsFHP(Long sFHP) {
        this.sFHP = Math.toIntExact(sFHP);
    }
}
