package com.example.community.service;

import android.util.Log;

import com.example.community.domain.Config;
import com.example.community.domain.Location;
import com.example.community.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 用户位置信息的上传与请求
 */
public class LocationService {
    final static String url="http://"+ Config.IP+":8080/community/LocationManageServlet";
    public static void queryFromServer(Location location){
        HttpUtil.LocationUploadOkHttp(url, location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Config.Success=false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Log.d("text", "onResponse: "+responseText);
                if(responseText!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(responseText);
                        if(jsonObject!=null){
                            String status=jsonObject.getString("status");
                            //Log.d("json", "onResponse: "+status);
                            if("1".equals(status))
                                Config.Success=true;
                            else if("0".equals(status))
                                Config.Success=false;
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //解析服务器返回来的用户位置信息json数据
    public static void queryFromServer(){
        HttpUtil.LocationRequestOkHttp(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Config.Response="";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Log.d("用户位置信息", "onResponse: "+responseText);
                Config.Response=responseText;
            }
        });
    }
}
