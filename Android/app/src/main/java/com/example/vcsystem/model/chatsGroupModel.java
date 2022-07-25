package com.example.vcsystem.model;

public class chatsGroupModel {

    String username;
    String uid;

    public chatsGroupModel(String username, String uid) {
        this.username = username;
        this.uid = uid;
    }

    public chatsGroupModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
