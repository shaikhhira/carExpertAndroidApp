package com.example.carexpertsystem.HelperClasses;
//related to login file

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private static Context ctx;

    private static final String SHARED_PREF="mySharedpref";
    private static final String KEY_USEREMAIL="LUSERNAME";
    private static final String KEY_USERID="LID";

    private SharedPrefManager(Context context) {
        ctx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean userLogin(int LID , String LUSERNAME){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putInt(KEY_USERID,LID);
        editor.putString(KEY_USEREMAIL,LUSERNAME);

        editor.apply();

        return  true;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USEREMAIL,null)!=null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
       SharedPreferences.Editor editor=sharedPreferences.edit();
       editor.clear();
       editor.apply();
       return true;
    }
    public String getUserEmail(){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USEREMAIL,null);
    }
}
