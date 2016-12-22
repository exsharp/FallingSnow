package com.zfliu.fallingsnow.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.Tools.Runtime;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jacky on 2016/12/20.
 */

public class SendSms {
    private TelephonyManager telephonyManager;
    private Context cxt;
    private String ProvidersName = "N/A";

    public SendSms(Context context){
        cxt = context;
        telephonyManager = (TelephonyManager)cxt.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private void getProvidersName() {
        try{
            String IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            } else if (IMSI.startsWith("46000")||IMSI.startsWith("46002")||IMSI.startsWith("46007")) {
                ProvidersName = "中国移动";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void SendChaXunPhoneNumberSms(){
        getProvidersName();
        if(!ProvidersName.equals("N/A")){
            switch (ProvidersName){
                case "中国移动":
                    SendSMS("10086","bj",cxt);
                    break;
                case "中国联通":
                    SendSMS("10010","CXHM",cxt);
                    break;
                default:
                    SendSMS("10001","501",cxt); //中国电信
            }
        }else{
            System.out.println("获取手机卡运营商失败");
        }
    }

    private void SendSMS(String number,String text,Context context){
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, context.getClass()), 0);
        SmsManager sms = SmsManager.getDefault();
        try {
            sms.sendTextMessage(number, null, text, pi, null);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean getPhoneNumber() {

        String PhoneNumber = telephonyManager.getLine1Number();
        if(PhoneNumber.length()!=11){
            return false;
        }
        Runtime.setPhoneNumber(cxt,PhoneNumber);
        return true;
    }
}
