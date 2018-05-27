package com.example.community.utils;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.example.community.domain.FriendCircle;
import com.example.community.domain.Location;
import com.example.community.domain.PersonalData;
import com.example.community.domain.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.OutputKeys;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp发送请求与接受请求类
 */
public class HttpUtil {

    //登录和注册账户的请求
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
    //个人资料上传的请求
    public static void PersonalDataRequestOkHttp(final String address, final PersonalData personalData,okhttp3.Callback callback){

        OkHttpClient client=new OkHttpClient();
        //存放提交的参数
        RequestBody requestBody=new FormBody.Builder()
                .add("action","insert")
                .add("account",personalData.getAccount())
                .add("image",personalData.getImage())
                .add("name",personalData.getName())
                .add("sex",personalData.getSex())
                .add("year",personalData.getYear())
                .add("phone",personalData.getPhone())
                .add("mail",personalData.getMail())
                .add("introduce",personalData.getIntroduce())
                .build();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    //个人资料查询的请求
    public static void PersonalDataQueryOkHttp(final String address, final PersonalData personalData,Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //存放提交的参数
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "query")
                .add("account", personalData.getAccount())
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    //所有用户资料的查询请求
    public static void PersonalDataAllQueryOkHttp(final String address,Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("action","queryAll").build();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    //上传图片的请求
    public static void ImageUploadOkHttp(final String address, final FriendCircle friendCircle, Callback callback){
        OkHttpClient client=new OkHttpClient();
        FormBody.Builder builder=new FormBody.Builder();
        //发送请求类型(上传)
        builder.add("action","upload");
        //发送账户
        builder.add("account",friendCircle.getAccount());
        Log.d("发送账户", "ImageUploadOkHttp: "+friendCircle.getAccount());
        //发送头像
        builder.add("headPhoto",ImageUtils.convertToString(friendCircle.getBitmap(),50));
        //发送动态消息
        builder.add("circleText",friendCircle.getCircleText());
        //发送图片个数
        builder.add("imageCount",friendCircle.getImageCount()+"");
        //遍历list中的数据，将其进行发送
        int i=0;
        for(String str:friendCircle.getCircleImageStrList()){
            builder.add("image"+(i++),str);
           // Log.d("图片字符串", "ImageUploadOkHttp: "+str);
        }
        //构建请求体
        RequestBody requestBody=builder.build();
        //构建请求
        Request request=new Request.Builder()
                        .url(address)
                        .post(requestBody)
                        .build();
        //发送异步请求
        client.newCall(request).enqueue(callback);
    }
    //查询某人动态的请求
    public static void CircleRequestOkHttp(final String address,final String account,Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                                .add("action","query")
                                .add("account",account).build();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    //上传用户位置信息的请求
    public static void LocationUploadOkHttp(final String address, final Location location,Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                                .add("action","upload")
                                .add("account",location.getAccount())
                                .add("longitude",location.getLongitude())
                                .add("latitude",location.getLatitude())
                                .add("address",location.getAddress()).build();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    //获取用户位置信息的请求
    public static void LocationRequestOkHttp(final String address, Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                                .add("action","query").build();
        Request request=new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
