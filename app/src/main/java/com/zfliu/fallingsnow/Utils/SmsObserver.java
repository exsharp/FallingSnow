package com.zfliu.fallingsnow.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jacky on 2016/12/20.
 */


public class SmsObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param hadler The handler to run {@link #onChange} on, or null if none.
     */
    private Context mContext;

    public SmsObserver(Handler handler, Context mContext) {
        super(handler);
        this.mContext = mContext;
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.i("info","SmsObserver->onChange()");
        Cursor cursor = mContext.getContentResolver().query(
                Uri.parse("content://sms/inbox"), null, null, null, "date desc");

        int i =1;
        while(cursor.moveToNext()){
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            if(address.equals("10086")){
                System.out.println("Body:"+body);
                boolean status = GetPhoneNumberFromSMSText(body);
                if(status){
                    System.out.println("获取手机号码成功");
                }else{
                    System.out.println("获取手机号码失败");
                }
                break;
            }else{
                if(i<10){
                    System.out.println("address:"+address+"\nbody:"+body);
                }
            }
        }
        Log.i("info","SmsObserver->onChange()--while");
        cursor.close();
        super.onChange(selfChange);
    }

    private boolean GetPhoneNumberFromSMSText(String sms){
        List<String> list=GetNumberInString(sms);
        for(String str:list){
            if(str.length()==11){
                SharedPreferences pref = this.mContext.getSharedPreferences("fallingSnowPref",Context.MODE_PRIVATE);
                if(pref.getString("PhoneNumber","0").equals(str)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("PhoneNumber",str);
                    editor.apply();
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private static List<String> GetNumberInString(String str){
        List<String> list=new ArrayList<String>();
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                list.add(m.group());
        }
        return list;
    }
}