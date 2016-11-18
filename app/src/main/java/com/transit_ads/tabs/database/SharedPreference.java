package com.transit_ads.tabs.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;

/**
 * Created by USER on 10/17/2016.
 */
public class SharedPreference {

    public static final String PREFS_FILE_NAME = "ADS_PREFS";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String TABLET_IMIE = "IMIE";

    public SharedPreference() {
        super();
    }

//    public void saveGmailDetails(Context context, String user_name, String user_email , String photo_url) {
//        SharedPreferences settings;
//        Editor editor;
//
//        //settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE); //1
//        editor = settings.edit(); //2
//
//        editor.putString(USER_NAME, user_name); //1
//        editor.putString(USER_EMAIL, user_email); //2
//        editor.putString(USER_PHOTO, photo_url); //3
//
//        editor.commit(); //4
//    }

    public void saveImie(Context context, String tablet_imie) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(TABLET_IMIE, tablet_imie); //3

        editor.commit(); //4
    }
    public void saveUserName_phone(Context context, int user_id ,String user_name,String phone_num) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putInt(USER_ID, user_id);
        editor.putString(USER_NAME, user_name);
        editor.putString(USER_PHONE, phone_num);

        editor.commit(); //4
    }

    public String getValue(Context context,String key) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, null);
        return text;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context,String key) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(key);
        editor.commit();
    }
}
