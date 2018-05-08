package com.example.community.service;

import android.text.TextUtils;
import android.util.Log;

import com.example.community.domain.Config;
import com.example.community.domain.User;
import com.example.community.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 向服务器发出注册请求
 */
public class UserService {
    final static String url="http://192.168.1.106:8080/community/UserManageServlet";
    public static void queryFromServer(String action, User user,final Config config){
        HttpUtil.sendRequestWithOkHttp(url, action, user, new Callback() {
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
}
