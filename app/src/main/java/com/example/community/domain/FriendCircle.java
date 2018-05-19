package com.example.community.domain;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * 朋友圈动态消息封装类
 */
public class FriendCircle implements Serializable{
    private String account;
    private String name;
    private Bitmap bitmap;
    private String circleText;
    private int ImageCount;
    private List<String> circleImageUrlList;
    private List<byte[]> circleImageList;
    private List<String> circleImageStrList;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<String> getCircleImageUrlList() {
        return circleImageUrlList;
    }

    public void setCircleImageUrlList(List<String> circleImageUrlList) {
        this.circleImageUrlList = circleImageUrlList;
    }

    public List<String> getCircleImageStrList() {
        return circleImageStrList;
    }

    public void setCircleImageStrList(List<String> circleImageStrList) {
        this.circleImageStrList = circleImageStrList;
    }

    public int getImageCount() {
        return ImageCount;
    }

    public void setImageCount(int imageCount) {
        ImageCount = imageCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCircleText() {
        return circleText;
    }

    public void setCircleText(String circleText) {
        this.circleText = circleText;
    }

    public List<byte[]> getCircleImageList() {
        return circleImageList;
    }

    public void setCircleImageList(List<byte[]> circleImageList) {
        this.circleImageList = circleImageList;
    }
}
