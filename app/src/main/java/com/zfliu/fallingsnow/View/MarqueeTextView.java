package com.zfliu.fallingsnow.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.zfliu.fallingsnow.R;

public class MarqueeTextView extends TextView implements Runnable {

    private int currentScrollX = 0;     // 当前滚动位置X轴
    private int beginScrollX = 0;       // 初始位置
    private int endX;                   // 滚动到哪个位置

    private int speed = 5;              // 滚动速度

    private boolean isFirstDraw=true;   // 当首次或文本改变时重置
    private boolean isStop = false;     // 开始停止的标记

    private int SCROLL_DELAYED = 20;   // 每次滚动间隔
    private static final int BEGIN_TO_SCROLL_DELAYED = 1 * 1000; // 两次滚动之间间隔

    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setSingleLine(true);
        setTextSize(50);
    }

    public void startScroll() {
        isStop = false;
        removeCallbacks(this);  // 清空队列
        postDelayed(this, 0);  // 开始滚动时间
        setBackgroundColor(Color.BLACK);
        getBackground().setAlpha(30);
    }

    public void setTextAndScroll(CharSequence text){
        setText(text);
        startScroll();
    }

    public void stopScroll() {
        isStop = true;
    }

    private int getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        int textWidth = (int) paint.measureText(str);
        return textWidth;
    }

    @Override
    public boolean isFocused() {
        return true;
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
            postDelayed(this, BEGIN_TO_SCROLL_DELAYED);  // SCROLL_DELAYED毫秒之后重新滚动
        } else {
            postDelayed(this, SCROLL_DELAYED);  // delayed毫秒之后再滚动到指定位置
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

        scrollTo(0,0);
        initScrollPos();

        post(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirstDraw) {
            initScrollPos();
            isFirstDraw = false;
        }
    }
}