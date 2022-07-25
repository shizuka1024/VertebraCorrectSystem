package com.example.vcsystem.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ReadModelPitch {

    private String userEmail;
    private double pitch;

    @ServerTimestamp
    private Date dateSent;

    public ReadModelPitch(String mEmail, String mPitch) {
        this.userEmail = mEmail;
        this.pitch = Double.parseDouble(mPitch);
    }

    public ReadModelPitch(Double pitch) {
        this.pitch = pitch;
    }

    public String getuserEmail() {
        return userEmail;
    }

    public void setSenderName(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getPitch() {
        return pitch;
    }

    public long getPitchInt() {
        return Math.round(pitch);
    }

    public void setPitch(Double pitch) {
        this.pitch = pitch;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
