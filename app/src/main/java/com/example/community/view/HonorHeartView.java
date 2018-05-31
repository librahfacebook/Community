package com.example.community.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.community.R;
@SuppressLint("AppCompatCustomView")
public class HonorHeartView extends ImageView{
    private static final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int mHeartResId = R.drawable.heart3;
    private int mHeartBorderResId = R.drawable.heart_border;
    private static Bitmap sHeart;
    private static Bitmap sHeartBorder;
    private static final Canvas sCanvas = new Canvas();


    public HonorHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HonorHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HonorHeartView(Context context) {
        super(context);
    }

    /**
     * 设置每一个资源文件
     * @param color
     */
    public void setColor(int color) {
        Bitmap heart = createHeart(color);
        setImageDrawable(new BitmapDrawable(getResources(), heart));
    }

    /**
     * 设置图片的资源文件
     * @param color
     * @param heartResId
     * @param heartBorderResId
     */
    public void setColorAndDrawables(int color, int heartResId, int heartBorderResId) {
        if (heartResId != mHeartResId) {
            sHeart = null;
        }
        if (heartBorderResId != mHeartBorderResId) {
            sHeartBorder = null;
        }
        mHeartResId = heartResId;
        mHeartBorderResId = heartBorderResId;
        setColor(color);
    }

    /**
     * 根据图片设置bitmap
     * @param color
     * @return
     */
    public Bitmap createHeart(int color) {
        if (sHeart == null) {
            sHeart = BitmapFactory.decodeResource(getResources(), mHeartResId);
        }
        if (sHeartBorder == null) {
            sHeartBorder = BitmapFactory.decodeResource(getResources(), mHeartBorderResId);
        }
        Bitmap heart = sHeart;
        Bitmap heartBorder = sHeartBorder;
        Bitmap bm = createBitmapSafely(heartBorder.getWidth(), heartBorder.getHeight());
        if (bm == null) {
            return null;
        }
        Canvas canvas = sCanvas;
        canvas.setBitmap(bm);
        Paint p = sPaint;
        canvas.drawBitmap(heartBorder, 0, 0, p);
        p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        float dx = (heartBorder.getWidth() - heart.getWidth()) / 2f;
        float dy = (heartBorder.getHeight() - heart.getHeight()) / 2f;
        canvas.drawBitmap(heart, dx, dy, p);
        p.setColorFilter(null);
        canvas.setBitmap(null);
        return bm;
    }

    /**
     * @param width
     * @param height
     * @return
     */
    private static Bitmap createBitmapSafely(int width, int height) {
        try {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return null;
    }
}
