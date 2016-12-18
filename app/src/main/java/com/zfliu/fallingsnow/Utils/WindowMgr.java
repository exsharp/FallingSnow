package com.zfliu.fallingsnow.Utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.zfliu.fallingsnow.CtxApplication;

/**
 * Created by zfliu on 2016/10/31.
 */

public class WindowMgr {
    public static void addView(View view,WindowParams params){
        Context context = CtxApplication.getContext();
        ((WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE))
                .addView(view,params);
    }
    public static void removeView(View view){
        Context context = CtxApplication.getContext();
        ((WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE))
                .removeView(view);
    }
    public static void updateView(View view,WindowParams params){
        Context context = CtxApplication.getContext();
        ((WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE))
                .updateViewLayout(view,params);
    }
}