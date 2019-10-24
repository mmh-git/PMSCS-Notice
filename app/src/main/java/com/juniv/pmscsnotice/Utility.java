package com.juniv.pmscsnotice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class Utility {
    public      int getNoticeID(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("JUNotice0",context.MODE_PRIVATE);
        int NID = prefs.getInt("NID", 0); //0 is the default value.
        return  NID;
    }
    public     void setNoticeID(Context context,int nid)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences
                ("JUNotice0", context.MODE_PRIVATE).edit();
        editor.putInt("NID",nid);

        editor.apply();
    }



   public void PopulateList(List<Notice> notices, String msg)
    {
        int indexOfHref= msg.indexOf("href");
        int indexOfCardTitle;
        int intTrail;
        String Discussion;
        String URL,TITLE;

        if(indexOfHref>-1) {
            msg = msg.substring(indexOfHref+6, msg.length());// length of href=" is six
            intTrail = msg.indexOf("NOTICE");
            URL = msg.substring(0, intTrail+6);// length of NOTICE=6
            msg=msg.substring(intTrail+1,msg.length());

            indexOfCardTitle= msg.indexOf("card-title");
            msg = msg.substring(indexOfCardTitle+12, msg.length());// length of card-title"> is six
            intTrail = msg.indexOf("</h2>");
            TITLE = msg.substring(0, intTrail);// length of NOTICE=6
            msg=msg.substring(intTrail+5,msg.length());
            String len="discussion/";
            Discussion=URL.substring(URL.indexOf("discussion")+len.length(),URL.indexOf("?")) ;
            notices.add(new Notice(URL,TITLE,Discussion));
            PopulateList(notices,msg);

        }

    }
}
