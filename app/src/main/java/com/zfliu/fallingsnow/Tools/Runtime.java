package com.zfliu.fallingsnow.Tools;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;

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

    public static DisplayMetrics getDisplayMetrics(){
        DisplayMetrics dm =CtxApplication.getContext().getResources().getDisplayMetrics();
        return dm;
    }

    public static boolean isLowerAPI(){
        int version = android.os.Build.VERSION.SDK_INT;
        if (version <= 21){
            return true;
        }
        return false;
    }

    public static void jumpToSetting(Activity activity){
        Uri packageURI = Uri.parse("package:" + "com.zfliu.fallingsnow");
        Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
        activity.startActivityForResult(intent,1);
    }
}
