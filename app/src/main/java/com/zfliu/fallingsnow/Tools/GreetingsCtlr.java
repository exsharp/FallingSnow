package com.zfliu.fallingsnow.Tools;

import android.os.Handler;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.Network.HTTP;
import com.zfliu.fallingsnow.Utils.Resource;
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
        handler.post(defaultGreeting);//最开始要显示的文字
        handler.post(new Runnable() {
            @Override
            public void run() {
                if ((repeatTimes++) < DEFAULT_GET_PHONE_TIMES){
                    if (CtxApplication.getPhoneNumber() != null){
                        //获得手机号码成功
                        handler.post(httpRunnable);
                        return;
                    }
                    handler.postDelayed(this,THREAD_SLEEP_TIME);
                }else{
                    //一分钟时间都没有获得手机号码
                    handler.post(withoutPhoneNumber);
                }
            }
        });
    }
    private Runnable defaultGreeting = new Runnable() {
        @Override
        public void run() {
            marquView.post(new Runnable() {
                @Override
                public void run() {
                    marquView.setTextAndScroll(Resource.defaultGreeting(),1);
                }
            });
        }
    };

    private Runnable httpRunnable = new Runnable() {
        @Override
        public void run() {
            String Number = CtxApplication.getPhoneNumber();
            HTTP.Get(Number,new HTTP.OnHttpStatusListener(){
                @Override
                public void Ok(String text) {
                    text = text.replace("\r","");
                    String texts[] = text.split("\n");
                    for (final String tt : texts){
                        try {
                            while (marquView.isScrolling()){
                                Thread.sleep(1000);
                            }
                            marquView.post(new Runnable() {
                                @Override
                                public void run() {
                                    marquView.setTextAndScroll(tt,1);
                                }
                            });
                            Thread.sleep(1000); //为了让上面的POST生效
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void Error() {
                    //HTTP获取失败，尝试？
                    //TODO
                }
            });
        }
    };

    private Runnable withoutPhoneNumber = new Runnable() {
        @Override
        public void run() {
            //没有获得手机号码时调用
        }
    };
}
