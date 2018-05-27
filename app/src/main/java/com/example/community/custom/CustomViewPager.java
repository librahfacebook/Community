package com.example.community.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义一个获取速度的ViewPager
 */
public class CustomViewPager extends ViewPager{
    private long downTime;
    private float LastX;
    private float mSpeed;

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x=ev.getX();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime=System.currentTimeMillis();
                LastX=x;
                break;
            case MotionEvent.ACTION_MOVE:
                x=ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                //计算得到手指从按下到离开的滑动速度
                mSpeed=(x-LastX)*1000/(System.currentTimeMillis()-downTime);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public float getmSpeed() {
        return mSpeed;
    }

    public void setmSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
    }
}
