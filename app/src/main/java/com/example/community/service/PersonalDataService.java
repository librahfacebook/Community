package com.example.community.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.community.MainFormActivity;
import com.example.community.domain.Config;
import com.example.community.domain.PersonalData;
import com.example.community.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 向服务器发送请求上传个人资料
 */
public class PersonalDataService{

    final static String url="http://10.177.233.211:8080/community/PersonalManageServlet";
    public static void queryFromServer(PersonalData personalData, final Config config){
        HttpUtil.PersonalDataRequestOkHttp(url, personalData, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                config.Success=false;
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
                                config.Success=true;
                            else if("0".equals(status))
                                config.Success=false;
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //解析服务器返回来的json数据
    public static void queryFromServer(PersonalData personalData){
        HttpUtil.PersonalDataQueryOkHttp(url,personalData, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Config.Response="";
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Config.Response=responseText;
            }
        });
    }
}
