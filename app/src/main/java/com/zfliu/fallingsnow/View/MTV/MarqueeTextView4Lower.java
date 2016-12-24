package com.zfliu.fallingsnow.View.MTV;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by zfliu on 12/24/2016.
 */

public class MarqueeTextView4Lower extends LinearLayout implements IMarqueeTextView {

    MarqueeTextView mtv = null;

    public MarqueeTextView4Lower(Context context) {
        super(context);
        initView(context);
    }

    public MarqueeTextView4Lower(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MarqueeTextView4Lower(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        mtv = new MarqueeTextView(context);
        mtv.setLayoutParams(params);
        addView(mtv);

    }

    @Override
    public void setText(CharSequence text){
        mtv.setText(text);
    }

    @Override
    public void startScroll(int times){
        mtv.startScroll(times);
    }

    @Override
    public void setTextAndScroll(String text,int times){
        mtv.setTextAndScroll(text,times);
    }

    @Override
    public boolean isScrolling() {
        return mtv.isScrolling();
    }

    @Override
    public boolean post(Runnable action) {
        return mtv.post(action);
    }
}
