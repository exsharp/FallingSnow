package com.zfliu.fallingsnow.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.zfliu.fallingsnow.Tools.Resource;
import com.zfliu.fallingsnow.Tools.Runtime;
import com.zfliu.fallingsnow.Utils.Random;
import com.zfliu.fallingsnow.Utils.Windows.WindowParams;

/**
 * Created by zfliu on 12/23/2016.
 */

public class RandomGifView extends GifView {

    private int pos_x; //屏幕的x
    private int pos_y; //屏幕的y
    private int lastGif = 0;
    private WindowParams params = WindowParams.CreateBothWrapContentParams();

    public RandomGifView(Context context) {
        super(context);
    }

    public RandomGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RandomGifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void randomGif(){
        //随机GIF
        int id = 0;
        while (true){
            id = Resource.gifResource();
            if (lastGif != id){
                lastGif = id;
                break;
            }
        }
        setGifResource(id);
    }

    private void randomPos(){
        //随机位置
        DisplayMetrics dm = Runtime.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        pos_x = Random.getRandom(0,width - width/10);
        pos_y = Random.getRandom(height/10,height - height/10);
    }

    public WindowParams getUpdatedParams(){
        randomGif();
        randomPos();
        params.setPosition(pos_x,pos_y);
        return params;
    }
}
