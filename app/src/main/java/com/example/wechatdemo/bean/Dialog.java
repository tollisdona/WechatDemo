package com.example.wechatdemo.bean;

import org.w3c.dom.Text;

public class Dialog {
    private String avatar;
    private int type;
    private String content;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
