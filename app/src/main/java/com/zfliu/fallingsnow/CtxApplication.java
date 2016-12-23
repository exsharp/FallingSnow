package com.zfliu.fallingsnow;

import android.app.Application;
import android.content.Context;

/**
 * Created by zfliu on 12/18/2016.
 */

public class CtxApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
