package com.zfliu.fallingsnow.Porfermor;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.zfliu.fallingsnow.Network.HTTP;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Utils.WindowMgr;
import com.zfliu.fallingsnow.Utils.WindowParams;
import com.zfliu.fallingsnow.View.MarqueeTextView;
import com.zfliu.fallingsnow.View.SnowView.SnowView;

public class MainService extends Service {

    private boolean hasInit = false; // 是否已经初始化

    private MediaPlayer mediaPlayer = null;
    private MarqueeTextView marqueeTextView = null;
    private SnowView snowView = null;

    private static int SHOW_TEXT_DELAYED = 5000;

    private void createView(){

        snowView = new SnowView(getApplicationContext());
        marqueeTextView = new MarqueeTextView(getApplicationContext());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.bgm);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSelf();
            }
        });
        //mediaPlayer.start();

        WindowMgr.addView(marqueeTextView,WindowParams.CreateParams(null,false));
        WindowMgr.addView(snowView,snowView.getWindowParams());

        HTTP.Get("1234",new HTTP.OnHttpStatusListener(){
            @Override
            public void Ok(String text) {
                marqueeTextView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        marqueeTextView.setTextAndScroll("123456");
                    }
                },SHOW_TEXT_DELAYED);
            }

            @Override
            public void Error() {
                marqueeTextView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        marqueeTextView.setTextAndScroll("123456");
                    }
                },SHOW_TEXT_DELAYED);
            }
        });
    }

    private void destroyView(){
        mediaPlayer.stop();
        WindowMgr.removeView(marqueeTextView);
        WindowMgr.removeView(snowView);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!hasInit){
            hasInit = true;
            createView();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        destroyView();
        super.onDestroy();
    }
}
