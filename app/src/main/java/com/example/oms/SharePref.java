package com.example.oms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
    private static SharedPreferences mSharedPref;
    public static final String LoginName = "LoginName";
    private SharePref()
    {

    }
    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }
}
