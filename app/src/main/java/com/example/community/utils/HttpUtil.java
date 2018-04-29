package com.example.community.utils;

import android.util.Log;

import com.example.community.domain.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.OutputKeys;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp发送请求与接受请求类
 */
public class HttpUtil {

    public static void sendRequestWithOkHttp(final String address,final String action,final User user,okhttp3.Callback callback){

        OkHttpClient client = new OkHttpClient();
        //存放待提交的参数
        Log.d("传入参数","action:"+action+" account:"+user.getAccount()+" paswd:"+user.getPassword());
        RequestBody requestBody = new FormBody.Builder()
                .add("action", action)
                .add("user_account", user.getAccount())
                .add("user_pwd", user.getPassword())
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);

    }
}
