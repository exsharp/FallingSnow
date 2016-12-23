package com.zfliu.fallingsnow.Tools;

import android.content.Context;
import android.os.Handler;

import com.zfliu.fallingsnow.Utils.Windows.WindowMgr;
import com.zfliu.fallingsnow.View.RandomGifView;

/**
 * Created by zfliu on 12/23/2016.
 */

public class RandomGifCtlr implements Runnable {

    private static final int REFRESH_DELAYED = 8000;
    private static final int REMOVE_DELAYED = 5000;
    private Handler handler = new Handler();
    private RandomGifView view = null;

    public RandomGifCtlr(Context context){
        view = new RandomGifView(context);
    }

    @Override
    public void run() {
        if (view != null){
            if (view.getWindowToken() == null){
                WindowMgr.addView(view,view.getUpdatedParams());
            }else{
                WindowMgr.updateView(view,view.getUpdatedParams());
            }
            handler.postDelayed(remove,REMOVE_DELAYED);
            handler.postDelayed(this,REFRESH_DELAYED);
        }
    }

    private Runnable remove = new Runnable() {
        @Override
        public void run() {
            WindowMgr.removeView(view);
        }
    };

    public void Start(){
        handler.postDelayed(this,8000);
    }

    public void Stop(){
        try{
            handler.removeCallbacks(this);
            handler.removeCallbacks(remove);
        } finally {
        }
        if (view != null && view.getWindowToken() != null){
            WindowMgr.removeView(view);
        }
    }
}
