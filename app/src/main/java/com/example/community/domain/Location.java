package com.example.community.domain;

import com.baidu.mapapi.model.LatLng;

import org.litepal.crud.DataSupport;

/**
 * 用户个人位置信息的封装
 */
public class Location extends DataSupport{
    //用户账户
    private String account;
    //用户的位置经纬度信息
    private String longitude;
    private String latitude;
    //用户的具体位置信息
    private String address;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
