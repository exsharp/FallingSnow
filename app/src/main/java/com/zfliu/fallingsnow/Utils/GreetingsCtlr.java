package com.zfliu.fallingsnow.Utils;

import android.os.Handler;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.Network.HTTP;
import com.zfliu.fallingsnow.View.MarqueeTextView;

/**
 * Created by zfliu on 12/21/2016.
 */

public class GreetingsCtlr {

    private static int THREAD_SLEEP_TIME = 5000;
    private static int DEFAULT_GET_PHONE_TIMES = 60 * 1000 / THREAD_SLEEP_TIME;
    private int repeatTimes = 0;
    private Handler handler = null;
    private MarqueeTextView marquView = null;

    public GreetingsCtlr(MarqueeTextView marqu){
        handler = new Handler();
        marquView = marqu;
    }

    public void Start(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                while ((repeatTimes++) < DEFAULT_GET_PHONE_TIMES){
                    if (CtxApplication.getPhoneNumber() != null){
                        handler.post(httpRunnable);
                        return;
                    }
                    try {
                        Thread.sleep(THREAD_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(defaultGreeting);
            }
        });
    }

    private Runnable httpRunnable = new Runnable() {
        @Override
        public void run() {
            String Number = CtxApplication.getPhoneNumber();
            HTTP.Get(Number,new HTTP.OnHttpStatusListener(){
                @Override
                public void Ok(final String text) {
                    marquView.post(new Runnable() {
                        @Override
                        public void run() {
                            marquView.setTextAndScroll(text);
                        }
                    });
                }
            });
        }
    };

    private Runnable defaultGreeting = new Runnable() {
        @Override
        public void run() {
            marquView.post(new Runnable() {
                @Override
                public void run() {
                    marquView.setTextAndScroll(Resource.defaultGreeting());
                }
            });
        }
    };
}
