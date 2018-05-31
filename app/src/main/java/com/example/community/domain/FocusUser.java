package com.example.community.domain;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

public class FocusUser extends DataSupport{
    private String account;
    private String name;
    private String headImage;
    private String introduce;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
