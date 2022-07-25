package com.example.vcsystem.model;

public class BulletinBoardModel {
    String title;
    String content;
    String publishName;
    String photo;

    public BulletinBoardModel(String title, String content, String publishName, String photo) {
        this.title = title;
        this.content = content;
        this.publishName = publishName;
        this.photo = photo;
    }

    public BulletinBoardModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
