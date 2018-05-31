package com.example.community.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.community.R;

public class HeartHonorLayout extends RelativeLayout{
    private AbstractPathAnimator mAnimator;

    public HeartHonorLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public HeartHonorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HeartHonorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeartLayout, defStyleAttr, 0);

        mAnimator = new MyHoPathAnimator(AbstractPathAnimator.Config.fromTypeArray(a));

        a.recycle();
    }

    public AbstractPathAnimator getAnimator() {
        return mAnimator;
    }

    public void setAnimator(AbstractPathAnimator animator) {
        clearAnimation();
        mAnimator = animator;
    }

    public void clearAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();
    }

    public void addHeart(int color) {
        HonorHeartView heartView = new HonorHeartView(getContext());
        heartView.setColor(color);
        mAnimator.start(heartView, this);
    }

    public void addHeart(int color, int heartResId, int heartBorderResId) {
        HonorHeartView heartView = new HonorHeartView(getContext());
        heartView.setColorAndDrawables(color, heartResId, heartBorderResId);
        mAnimator.start(heartView, this);
    }
}
