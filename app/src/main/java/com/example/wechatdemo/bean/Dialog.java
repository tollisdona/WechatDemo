package com.example.wechatdemo.bean;

public class Dialog {
    private String avatar;
    private String from;
    private String content;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "avatar=" + avatar +
                ", from=" + from +
                ", content='" + content + '\'' +
                '}';
    }
}
