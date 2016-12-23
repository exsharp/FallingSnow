package com.zfliu.fallingsnow.View;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by zfliu on 12/23/2016.
 */

public class RandomGifView extends GifView {

    public RandomGifView(Context context) {
        super(context);
    }

    public RandomGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RandomGifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int pos_x; //屏幕的x
    private int pos_y; //屏幕的y

    private void randomGif(){
        //随机GIF
    }

    private void randomPos(){
        //随机位置
    }
}
