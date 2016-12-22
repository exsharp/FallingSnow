package com.zfliu.fallingsnow.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.zfliu.fallingsnow.CtxApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zfliu on 12/22/2016.
 */

public class Runtime {

    private static final String SF= "fallingSnowPref";

    public static boolean isFirstTime(){
        SharedPreferences pref = CtxApplication.getContext().getSharedPreferences(SF, MODE_PRIVATE);
        return pref.getBoolean("startFirst", true);
    }

    public static void setFirstTimeToFalse(){
        SharedPreferences pref = CtxApplication.getContext().getSharedPreferences(SF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("startFirst", false);
        editor.apply();
    }

    public static String getPhoneNumber(){
        SharedPreferences pref = CtxApplication.getContext().getSharedPreferences(SF, MODE_PRIVATE);
        String num = pref.getString("PhoneNumber","0");
        if (num.length() == 11){
            return num;
        }
        return null;
    }

    public static void setPhoneNumber(String number){
        SharedPreferences pref = CtxApplication.getContext().getSharedPreferences(SF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PhoneNumber",number);
        editor.apply();
    }
}
