package com.zfliu.fallingsnow.Utils.Windows;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by zfliu on 2016/10/31.
 */

public class WindowParams extends LayoutParams {
    public WindowParams() {
        super();
    }
    private void defaultParams(){
        type = LayoutParams.TYPE_TOAST;
        format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置Window flag
        flags = LayoutParams.FLAG_NOT_TOUCH_MODAL   //不影响后面的事件
                | LayoutParams.FLAG_NOT_TOUCHABLE   //不可触摸
                | LayoutParams.FLAG_NOT_FOCUSABLE;  //不可聚焦，返回键？

        // 调整悬浮窗口至左上角，便于调整坐标
        gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        //windowManagerParams.x = x;
        //windowManagerParams.y = y;
        // 设置悬浮窗口长宽数据
        width = WindowManager.LayoutParams.MATCH_PARENT;
        height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static WindowParams CreateParams(Point point,boolean isFullScreen){
        WindowParams params = new WindowParams();
        params.defaultParams();

        if (point != null){

        }

        if (!isFullScreen){
            params.height = LayoutParams.WRAP_CONTENT;
        }

        return params;
    }

    public static WindowParams CreateBothWrapContentParams(){
        WindowParams params = new WindowParams();
        params.defaultParams();
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.WRAP_CONTENT;
        return params;
    }

    public static WindowParams CreateWrapContentParams(){
        WindowParams params = new WindowParams();
        params.defaultParams();
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.MATCH_PARENT;

        return params;
    }
}
