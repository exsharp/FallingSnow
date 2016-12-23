package com.zfliu.fallingsnow.Porfermor.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.WindowManager;

import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Tools.GreetingsCtlr;
import com.zfliu.fallingsnow.Tools.RandomGifCtlr;
import com.zfliu.fallingsnow.Utils.Windows.WindowMgr;
import com.zfliu.fallingsnow.Utils.Windows.WindowParams;
import com.zfliu.fallingsnow.View.GifView;
import com.zfliu.fallingsnow.View.MarqueeTextView;
import com.zfliu.fallingsnow.View.SnowView.SnowView;

public class MainService extends Service {

    private boolean hasInit = false; // 是否已经初始化

    private MediaPlayer mediaPlayer = null;
    private MarqueeTextView marqueeTextView = null;
    private SnowView snowView = null;
    private GifView gifTreeView = null;
    private GreetingsCtlr greetingsCtlr = null;
    private RandomGifCtlr randomGifCtlr = null;

    private void createView(){

        gifTreeView = new GifView(getApplicationContext());
        gifTreeView.setGifResource(R.raw.tree);
        snowView = new SnowView(getApplicationContext());
        marqueeTextView = new MarqueeTextView(getApplicationContext());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.bgm);
        greetingsCtlr = new GreetingsCtlr(marqueeTextView);
        randomGifCtlr = new RandomGifCtlr(getApplicationContext());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSelf();
            }
        });
        mediaPlayer.start();

        WindowMgr.addView(marqueeTextView,WindowParams.CreateParams(null,false));
        WindowMgr.addView(snowView,snowView.getWindowParams());
        WindowMgr.addView(gifTreeView,gifTreeView.getParams());

        greetingsCtlr.Start();
        randomGifCtlr.Start();
    }

    private void destroyView(){
        mediaPlayer.stop();
        randomGifCtlr.Stop();
        WindowMgr.removeView(marqueeTextView);
        WindowMgr.removeView(snowView);
        WindowMgr.removeView(gifTreeView);
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
