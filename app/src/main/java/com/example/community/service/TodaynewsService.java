package com.example.community.service;

import com.example.community.domain.Config;
import com.example.community.utils.HttpUtil;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TodaynewsService {
    static Calendar calendar=Calendar.getInstance();
    final static String url="http://api.juheapi.com/japi/toh?key=320ad55ad28f832807ad6a51de7850f4&v=1.0&month="+
            (calendar.get(Calendar.MONTH)+1)+"&day="+calendar.get(Calendar.DATE);
    public static void requestTodaynews(){
        HttpUtil.TodaynewsRequestOkHttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Config.Success=false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                Config.Response=responseText;
            }
        });
    }
}
