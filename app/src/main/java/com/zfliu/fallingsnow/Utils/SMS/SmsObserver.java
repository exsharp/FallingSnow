package com.zfliu.fallingsnow.Utils.SMS;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.Tools.Runtime;

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
        Cursor cursor = mContext.getContentResolver().query(
                Uri.parse("content://sms/inbox"), null, null, null, "date desc");

        while(cursor.moveToNext()){
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            if(address.equals("10086")||address.equals("10010")||address.equals("10001")){
                System.out.println("Body:"+body);
                boolean status = GetPhoneNumberFromSMSText(body);
                if(status){
                    Log.d("SmsObserve","获取手机号码成功");
                    return;//这里可能可以处理不弹出通知栏
                }else{
                    Log.d("SmsObserve","获取手机号码失败");
                }
                break;
            }
        }
        cursor.close();
        super.onChange(selfChange);
    }

    private boolean GetPhoneNumberFromSMSText(String sms){
        List<String> list=GetNumberInString(sms);
        for(String str:list){
            if(str.length()==11){
                Runtime.setPhoneNumber(str);
                return true;
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