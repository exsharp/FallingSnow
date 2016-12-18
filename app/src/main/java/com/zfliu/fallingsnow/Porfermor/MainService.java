package com.zfliu.fallingsnow.Porfermor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

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

    private void createView(){

        snowView = new SnowView(getApplicationContext());
        marqueeTextView = new MarqueeTextView(getApplicationContext());
        marqueeTextView.setTextAndScroll("abcdefghijklmnopqrstuvwxyz");

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.bgm);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSelf();
            }
        });
        mediaPlayer.start();

        WindowMgr.addView(marqueeTextView,WindowParams.CreateParams(null,false));
        WindowMgr.addView(snowView,snowView.getWindowParams());
    }

    private void destroyView(){
        mediaPlayer.stop();
        WindowMgr.removeView(marqueeTextView);
        WindowMgr.removeView(snowView);
    }
}
