package com.zfliu.fallingsnow.View.SnowView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;

import com.zfliu.fallingsnow.Tools.Runtime;
import com.zfliu.fallingsnow.Utils.*;

class SnowFlake {
    private static final float ANGE_RANGE = 0.1f;
    private static final float HALF_ANGLE_RANGE = ANGE_RANGE / 2f;
    private static final float HALF_PI = (float) Math.PI / 2f;
    private static final float ANGLE_SEED = 25f;
    private static final float ANGLE_DIVISOR = 10000f;
    private static final float INCREMENT_LOWER = 2f;
    private static final float INCREMENT_UPPER = 4f;
    private static final float FLAKE_SIZE_LOWER = 7f;
    private static final float FLAKE_SIZE_UPPER = 20f;
    private static final float LOWER_RESOLUTION_SCALE = 0.7F;

    private final Random random;
    private final Point position;
    private float angle;
    private final float increment;
    private final float flakeSize;
    private final Paint paint;

    public static SnowFlake create(int width, int height, Paint paint) {
        Random random = new Random();
        float angle = random.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        float flakeSize = 0;
        DisplayMetrics dm = Runtime.getDisplayMetrics();
        if (dm.widthPixels < 1080){
            flakeSize = random.getRandom(
                    FLAKE_SIZE_LOWER * LOWER_RESOLUTION_SCALE,
                    FLAKE_SIZE_UPPER * LOWER_RESOLUTION_SCALE);
        }else{
            flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        }

        int x = random.getRandom(width);
        //int y = random.getRandom(height);
        int y = (int) (-flakeSize - 1);;
        Point position = new Point(x, y);
        return new SnowFlake(random, position, angle, increment, flakeSize, paint);
    }

    SnowFlake(Random random, Point position, float angle, float increment, float flakeSize, Paint paint) {
        this.random = random;
        this.position = position;
        this.angle = angle;
        this.increment = increment;
        this.flakeSize = flakeSize;
        this.paint = paint;
    }

    private void move(int width, int height) {
        double x = position.x + (increment * Math.cos(angle));
        double y = position.y + (increment * Math.sin(angle));

        angle += random.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR;

        position.set((int) x, (int) y);

        if (!isInside(width, height)) {
            reset(width);
        }
    }

    private boolean isInside(int width, int height) {
        int x = position.x;
        int y = position.y;

        // width 设备宽度
        // height 设备高度
        return (x >= -flakeSize - 1) && (x + flakeSize <= width) && (y >= -flakeSize - 1) && (y - flakeSize < height);
    }

    private void reset(int width) {
        position.x = random.getRandom(width);
        position.y = (int) (-flakeSize - 1);
        angle = random.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
    }

    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        move(width, height);
        canvas.drawCircle(position.x, position.y, flakeSize, paint);
    }
}
