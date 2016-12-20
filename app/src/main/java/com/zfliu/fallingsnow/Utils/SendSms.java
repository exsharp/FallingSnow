package com.zfliu.fallingsnow.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jacky on 2016/12/20.
 */

public class SendSms {
    private String PhoneNumber = "";  //手机号码
    private TelephonyManager telephonyManager;
    private String IMSI;     //国际移动用户识别码
    private Context cxt;
    private String ProvidersName = "N/A";

    public SendSms(Context context){
        cxt = context;
        telephonyManager = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void getProvidersName() {
        try{
            IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void SendSMS(String number,String text,Context context){
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, context.getClass()), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, text, pi, null);
    }

    public void SendChaXunPhoneNumberSms(){
        getProvidersName();
        if(ProvidersName!="N/A"){
            if(ProvidersName == "中国移动"){
                SendSMS("10086","bj",cxt);
            }else if(ProvidersName == "中国联通"){
                SendSMS("10010","CXHM",cxt);
            }else{
                SendSMS("10001","501",cxt);  //中国电信
            }
        }else{
            System.out.println("获取手机卡运营商失败");
        }
    }

    public boolean getPhoneNumber() {
        PhoneNumber=telephonyManager.getLine1Number();
        if(PhoneNumber.length()!=11){
            return false;
        }else{
            System.out.println("PhoneNumber:"+PhoneNumber);
            SharedPreferences pref = cxt.getSharedPreferences("fallingSnowPref", MODE_PRIVATE);
            if(pref.getString("PhoneNumber","0")!=PhoneNumber){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("PhoneNumber",PhoneNumber);
                editor.commit();
            }
            return true;
        }
    }

}
