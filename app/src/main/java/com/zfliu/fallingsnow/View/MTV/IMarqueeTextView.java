package com.zfliu.fallingsnow.View.MTV;

/**
 * Created by zfliu on 12/24/2016.
 */

public interface IMarqueeTextView {
    void setText(CharSequence text);
    void startScroll(int times);
    void setTextAndScroll(String text,int times);
    boolean isScrolling();
    boolean post(Runnable runnable);
}
