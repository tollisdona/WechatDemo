package com.example.wechatdemo.bean;

import android.graphics.Bitmap;

public class Message {
    private String nickname;
    private String content;
    private String date;
    private String avatar;
    private String uid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Message{" +
                "nickname='" + nickname + '\'' +
                ", Content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", avatar=" + avatar +
                ", uid='" + uid + '\'' +
                '}';
    }
}
