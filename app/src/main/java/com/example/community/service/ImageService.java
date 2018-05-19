package com.example.community.service;

import android.util.Log;

import com.example.community.domain.Config;
import com.example.community.domain.FriendCircle;
import com.example.community.utils.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 上传图片至服务器
 */
public class ImageService {
    final static String url="http://"+ Config.IP+":8080/community/CircleManageServlet";
    public static void queryFromServer(FriendCircle friendCircle, final Config config){
        HttpUtil.ImageUploadOkHttp(url, friendCircle, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                config.Success=false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Log.d("发送内容", "onResponse: "+responseText);
                if(responseText!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(responseText);
                        if(jsonObject!=null){
                            String status=jsonObject.getString("status");
                            if(status.equals("1")){
                                config.Success=true;
                            }else if(status.equals("0")){
                                config.Success=false;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //解析服务器返回来的动态数据
    public static void queryFromServer(final String account){
        HttpUtil.CircleRequestOkHttp(url, account, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Config.CircleResponse="";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Log.d("动态回送消息", "onResponse: "+responseText);
                Config.CircleResponse=responseText;
            }
        });
    }
}
