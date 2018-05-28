package com.example.community.domain;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * viewpager展示的封装类
 */
public class PersonInfo {
    private String account;//账户
    private Bitmap image;//设置头像
    private String name;//名字
    private String age;//年龄
    private boolean sex;//false为男，true为女
    private float distance;//距离

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
