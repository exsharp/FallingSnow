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
    private static final int REDRAW_DELAY = 5;
    private static final int CREATE_SNOW_DELAY = 300;
    private static final int CREATE_SNOW_EACH_TIME = 2;

    private ArrayList<SnowFlake> snowflakes = new ArrayList<>();

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            final int width = w;
            final int height = h;
            final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);

            snowflakes.clear();

            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (snowflakes.size() >= NUM_SNOWFLAKES){
                        removeCallbacks(this);
                    }else{
                        for (int i = 0; i < CREATE_SNOW_EACH_TIME; ++i) {
                            SnowFlake snow = SnowFlake.create(width, height, paint);
                            snowflakes.add(snow);
                        }
                        postDelayed(this,CREATE_SNOW_DELAY);
                    }
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SnowFlake snowFlake : snowflakes) {
            snowFlake.draw(canvas);
        }
        getHandler().postDelayed(runnable, REDRAW_DELAY);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
}
