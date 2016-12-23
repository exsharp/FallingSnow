package com.zfliu.fallingsnow.Porfermor;

import android.widget.ImageView;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Utils.Windows.WindowMgr;
import com.zfliu.fallingsnow.Utils.Windows.WindowParams;

/**
 * Created by zfliu on 12/18/2016.
 */

public class ToastDemo {
    //测试悬浮窗是否开启

    private static ImageView imageView= null;

    public static void Show(){
        if (imageView == null){
            imageView = new ImageView(CtxApplication.getContext());
            imageView.setImageResource(R.drawable.snow);
        }
        if (imageView.getWindowToken() == null){
            WindowParams params = WindowParams.CreateWrapContentParams();
            WindowMgr.addView(imageView,params);
        }
    }

    public static void Hide(){
        if (imageView != null && imageView.getWindowToken() != null){
            WindowMgr.removeView(imageView);
        }
    }
}
