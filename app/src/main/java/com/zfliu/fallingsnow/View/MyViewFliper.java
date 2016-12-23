package com.zfliu.fallingsnow.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.zfliu.fallingsnow.R;

/**
 * Created by Jacky on 2016/12/23.
 */

public class MyViewFliper extends ViewFlipper {

    private Context context;
    private int imgSize;

    public MyViewFliper(Context context) {
        super(context);
    }

    public MyViewFliper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void addImageView(){

        int []view = {
                R.drawable.step1,
                R.drawable.step2,
                R.drawable.step3,
                R.drawable.step4,
        };
        imgSize = view.length;
        for (int v : view){
            addView(v);
        }
    }

    @Override
    public void showNext() {
        if(getDisplayedChild()!=imgSize-1){
            super.showNext();
        }
    }

    @Override
    public void showPrevious() {
        if(getDisplayedChild()!=0){
            super.showPrevious();
        }
    }

    public void addView(int id) {
        ImageView image = getImageView(id);
        super.addView(image);
    }

    private ImageView getImageView(int id){
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(id);
        return imageView;
    }

    private float startX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX=event.getX();
                Log.d("MotionEvent","down");
                return true;
            case MotionEvent.ACTION_UP:
                //向右滑动
                Log.d("MotionEvent","UP");
                if(event.getX()-startX>50){
                    showPrevious();
                }
                //向左滑动
                if(startX-event.getX()>50) {
                    showNext();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }
}
