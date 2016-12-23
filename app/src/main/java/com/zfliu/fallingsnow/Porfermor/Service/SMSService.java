package com.zfliu.fallingsnow.Porfermor.Service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zfliu.fallingsnow.Utils.SMS.SmsObserver;

/**
 * Created by zfliu on 12/22/2016.
 */

public class SMSService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private SmsObserver smsObserver = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //注册短信Listener
        smsObserver = new SmsObserver(new Handler(),this);
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,smsObserver);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
