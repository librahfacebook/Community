package com.example.community.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气的基本类
 */
public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}