package com.zfliu.fallingsnow.View.SnowView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zfliu.fallingsnow.Utils.WindowParams;

import java.util.ArrayList;

public class SnowView extends View {

    private WindowParams params = null;

    private static final int NUM_SNOWFLAKES = 100;
    private static final int DELAY = 5;

    private SnowFlake[] snowflakes;
    private ArrayList<SnowFlake> snowflakes;

    public SnowView(Context context) {
        super(context);
        params = WindowParams.CreateParams(null,true);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WindowParams getWindowParams(){
        return params;
    }

    protected void resize(int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        snowflakes = new SnowFlake[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            snowflakes[i] = SnowFlake.create(width, height, paint);
            Log.d("TAG",width + " " + height);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            resize(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SnowFlake snowFlake : snowflakes) {
            snowFlake.draw(canvas);
        }
        getHandler().postDelayed(runnable, DELAY);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private Runnable createSnow = new Runnable(){
        @Override
        public void run() {
            //生成雪花
            if ()
            removeCallbacks(this);
        }
    }
}
