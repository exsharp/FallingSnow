package com.zfliu.fallingsnow.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * Created by zfliu on 12/23/2016.
 */

public class MarqueeTextView extends TextView implements Runnable {

    private static final int SCROLL_DELAYED = 20;   // 每次滚动时间间隔
    private static final int BEGIN_TO_SCROLL_DELAYED = 1000; // 两次滚动之间时间间隔
    private int SCROLL_TIMES = 2; //滚动次数

    private static final int TEXT_SIZE = 35;
    private static final int BG_ALPHA = 50;

    private int currentScrollX = 0;     // 当前滚动位置X轴
    private int beginScrollX = 0;       // 初始位置
    private int endX;                   // 滚动到哪个位置

    private int speed = 5;              // 滚动速度
    private int times = 0;              // 已经滚动次数

    private boolean isFirstDraw=true;   // 当首次或文本改变时重置
    private boolean isStop = false;     // 开始停止的标记

    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setSingleLine(true);
        setTextSize(TEXT_SIZE);
        setBackgroundColor(Color.BLACK);
        getBackground().setAlpha(BG_ALPHA);
    }

    private int getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        int textWidth = (int) paint.measureText(str);
        return textWidth;
    }
    public void startScroll(int times) {
        isStop = false;
        SCROLL_TIMES = times;
        removeCallbacks(this);  // 清空队列
        post(this);  // 开始滚动时间
    }

    public void setTextAndScroll(CharSequence text,int times){
        setText(text);
        startScroll(times);
    }

    public boolean isScrolling() {
        return !isStop;
    }

    @Override
    public void run() {
        if (isStop) {
            return;
        }
        currentScrollX += speed;  // 滚动速度每次加几个点
        scrollTo(currentScrollX, 0); // 滚动到指定位置
        if (currentScrollX >= endX) {   // 如果滚动的位置大于最大限度则滚动到初始位置
            scrollTo(beginScrollX, 0);
            currentScrollX = beginScrollX; // 初始化滚动速度
            times++;
            if (times < SCROLL_TIMES){
                postDelayed(this, BEGIN_TO_SCROLL_DELAYED);  // SCROLL_DELAYED毫秒之后重新滚动
            }else{
                isStop = true;
            }
        } else {
            postDelayed(this, SCROLL_DELAYED);  // delayed毫秒之后再滚动到指定位置
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirstDraw){
            isFirstDraw = false;
            isStop = false;
            scrollTo(0,0);
            initScrollPos();
        }
    }

    private void initScrollPos(){
        int textWidth = getTextWidth(); // 文本宽度
        int mWidth = getWidth(); // 控件宽度
        endX = textWidth > mWidth ? textWidth:mWidth;  // 滚动的最大距离，可根据需要来定

        beginScrollX = -((mWidth == 0 ) ? textWidth : mWidth);
        currentScrollX = beginScrollX + (beginScrollX / 10);
        scrollTo(currentScrollX,0);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        isStop = true;
        isFirstDraw = true;
        removeCallbacks(this);
    }
}
