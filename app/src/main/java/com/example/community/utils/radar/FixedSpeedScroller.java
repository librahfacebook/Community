package com.example.community.utils.radar;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller{

    private int mDuration = 1000;
    public FixedSpeedScroller(Context context, AccelerateInterpolator accelerateInterpolator) {
        super(context,accelerateInterpolator);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, duration);
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }
}
