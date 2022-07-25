package com.example.vcsystem.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class ReadModelNoFHP implements Serializable {
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

    Integer NoFHP;

    public Integer getNoFHP() {
        return NoFHP;
    }

    public void setNoFHP(Integer NoFHP) {
        this.NoFHP = NoFHP;
    }

    public ReadModelNoFHP(Long NoFHP) {
        this.NoFHP = Math.toIntExact(NoFHP);
    }
}
