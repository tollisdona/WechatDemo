package com.example.wechatdemo.bean;

public class Friend {
    private String nickname;
    private String avatar;
    private String uid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        return "Friend{" +
                "nickname='" + nickname + '\'' +
                ", avatar=" + avatar +
                ", uid='" + uid + '\'' +
                '}';
    }
}
