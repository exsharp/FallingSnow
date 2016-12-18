package com.zfliu.fallingsnow.Porfermor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

import com.zfliu.fallingsnow.Utils.WindowParams;
import com.zfliu.fallingsnow.View.MarqueeTextView;
import com.zfliu.fallingsnow.View.SnowView.SnowView;

public class MainService extends Service {

    private SnowView snowView = null;

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createView();
        return super.onStartCommand(intent, flags, startId);
    }

    private void createView(){

        MarqueeTextView tv = new MarqueeTextView(getApplicationContext());
        tv.setTextAndScroll("abcdefghijklmnopqrstuvwxyz");

        ((WindowManager)getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE))
                .addView(tv, WindowParams.CreateParams(null,false));

        snowView = new SnowView(getApplicationContext());
        ((WindowManager)getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE))
                .addView(snowView,snowView.getWindowParams());

    }
}
