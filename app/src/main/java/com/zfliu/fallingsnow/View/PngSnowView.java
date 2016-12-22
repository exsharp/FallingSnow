package com.zfliu.fallingsnow.View;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zfliu.fallingsnow.R;

/**
 * Created by zfliu on 12/8/2016.
 */

public class PngSnowView extends View {

    private Bitmap bitmap;
    private Paint paint;
    private Matrix matrix;
    private RectF rect;
    private float scale = 1f;
    int center_x,center_y;

    public PngSnowView(Context context) {
        super(context);
    }

    public PngSnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        rect = new RectF();
        paint = new Paint();
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);

        center_x = (int)(bitmap.getWidth() * scale / 2);
        center_y = (int)(bitmap.getHeight() * scale / 2);

        matrix.setScale(scale,scale,center_x,center_y);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        matrix.postRotate(5,center_x,center_y);
//        matrix.postTranslate(50,50);
//        matrix.mapRect(rect);
//        center_x += rect.left;
//        center_y += rect.top;
        canvas.drawBitmap(bitmap,matrix,paint);

        getHandler().postDelayed(runnable, 5);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

}
