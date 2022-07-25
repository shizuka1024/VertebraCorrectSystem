package com.example.vcsystem.model;

public class ReserveModel {
    String dateSent3;
    String uid;

    public ReserveModel(String dateSent3, String uid) {
        this.dateSent3 = dateSent3;
        this.uid = uid;
    }

    public ReserveModel() {
    }

    public String getDateSent3() {
        return dateSent3;
    }

    public void setDateSent3(String dateSent3) {
        this.dateSent3 = dateSent3;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
