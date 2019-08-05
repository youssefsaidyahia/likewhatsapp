package com.example.android.whatsapp;

import java.io.Serializable;

public class usedata implements Serializable{
    private String name,phone,uid , notification_key;
    private Boolean selected=false;
    public usedata(String ui){
        uid=ui;
    }
    public usedata(String ui, String n ,String p){
        name=n;
        phone=p;
        uid=ui;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotification_key() {
        return notification_key;
    }

    public void setNotification_key(String notification_key) {
        this.notification_key = notification_key;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
