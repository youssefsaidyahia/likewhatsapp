package com.example.android.whatsapp;

import java.util.ArrayList;

public class messageData {

    String messageid,senderid,message;
    ArrayList<String>mediaUrlList;

    public messageData(String mid ,String sid ,  String m , ArrayList<String>mediaUrlLis){
        this.messageid=mid;
        this.senderid=sid;
        this.message=m;
        this.mediaUrlList=mediaUrlLis;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageid() {
        return messageid;
    }

    public String getSenderid() {
        return senderid;
    }

    public ArrayList<String> getMediaUrlList() {
        return mediaUrlList;
    }
}
