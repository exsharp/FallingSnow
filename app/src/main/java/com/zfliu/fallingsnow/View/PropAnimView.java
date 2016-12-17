package com.zfliu.fallingsnow.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.zfliu.fallingsnow.R;

/**
 * Created by zfliu on 12/8/2016.
 */

public class PropAnimView extends View {
    public PropAnimView(Context context) {
        super(context);
    }

    public PropAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
    }
}
