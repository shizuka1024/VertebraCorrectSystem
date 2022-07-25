package com.example.vcsystem.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {
    private String mName;
    private String mMessage;
    private String mUid;
    @ServerTimestamp
    private Date dateSent;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

    public ChatMessage(){}

    public ChatMessage(String name, String message, String uid) {
        mName = name;
        mMessage = message;
        mUid = uid;
    }

    public String getName() { return mName; }

    public void setName(String name) { mName = name; }

    public String getMessage() { return mMessage; }

    public void setMessage(String message) { mMessage = message; }

    public String getUid() { return mUid; }

    public void setUid(String uid) { mUid = uid; }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

}
