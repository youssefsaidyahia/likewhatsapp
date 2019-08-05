package com.example.android.whatsapp;

import java.io.Serializable;
import java.util.ArrayList;

public class chatData implements Serializable{
    String Chatid;

    private ArrayList<usedata> usedata= new ArrayList<>();
   public chatData(String cid){
       this.Chatid=cid;
   }

    public String getChatid() {
        return Chatid;
    }

    public ArrayList<usedata> getUsedata() {
        return usedata;
    }

    public void addUsedata(usedata usedata1) {
         usedata.add(usedata1);
    }
}
