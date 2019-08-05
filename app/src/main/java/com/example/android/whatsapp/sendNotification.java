package com.example.android.whatsapp;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class sendNotification
{
    public   sendNotification(String message , String heading , String  notKey){

            try {
                JSONObject notificationContent = new JSONObject(
                        "{'contents':{'en':'" + message + "'},"+
                                "'include_player_ids':['" + notKey + "']," +
                                "'headings':{'en': '" + heading + "'}}");
                OneSignal.postNotification(notificationContent, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

