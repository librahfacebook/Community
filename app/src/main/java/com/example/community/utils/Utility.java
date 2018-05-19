package com.example.community.utils;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.community.R;
import com.example.community.domain.Config;
import com.example.community.domain.FriendCircle;
import com.example.community.domain.PersonalData;
import com.example.community.service.ImageService;
import com.example.community.service.PersonalDataService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 解析json数据并进行封装
 */
public class Utility {

    public static PersonalData dataExecute(){
        PersonalData personalData=new PersonalData();
        personalData.setAccount(Config.Account);
        PersonalDataService.queryFromServer(personalData);
        //睡眠1S后再判断
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("Config", "dataExecute: "+Config.Response);
        try{
            Log.d("Response", "dataExecute: "+Config.Response);
            if((Config.Response).equals("")){
                personalData=null;
            }else{
                JSONObject jsonObject=new JSONObject(Config.Response);
                personalData.setName(jsonObject.getString("name"));
                personalData.setSex(jsonObject.getString("sex"));
                personalData.setYear(jsonObject.getString("year"));
                personalData.setPhone(jsonObject.getString("phone"));
                personalData.setMail(jsonObject.getString("mail"));
                personalData.setIntroduce(jsonObject.getString("introduce"));
                personalData.setImage(jsonObject.getString("image"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return personalData;
    }
    //返回动态圈数据
    public static Object circleExcute(String account){
        List<FriendCircle> circleList=new ArrayList<>();
        ImageService.queryFromServer(account);
        //睡眠1S后再判断
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        //Log.d("Config回送内容", "circleExcute: "+Config.CircleResponse);
        if(!Config.CircleResponse.equals("")){
            try{
                JSONObject jsonObject=new JSONObject(Config.CircleResponse);
                String headPhoto="http://"+Config.IP+":8080/circle/"+jsonObject.getString("headPhoto");
                Log.d("头像URl地址", "circleExcute: "+headPhoto);
                Config.image=null;
                ImageUtils.getHttpBitmap(headPhoto);
                try{
                    Thread.sleep(200);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Bitmap headImage=ImageUtils.toRoundBitmap(Config.image);
                JSONArray jsonArray=new JSONArray(jsonObject.getString(account));
                FriendCircle friendCircle=null;
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    friendCircle=new FriendCircle();
                    friendCircle.setAccount(account);
                    friendCircle.setName(Config.Name);
                    String circleText=object.getString("circleText");
                    friendCircle.setCircleText(circleText);
                    //解析动态图片
                    JSONArray array=new JSONArray(object.getString("image"));
//                    //Log.d("图片数组", "circleExcute: "+object.getString("image"));
                    friendCircle.setImageCount(array.length());
                    List<String> imageList=new ArrayList<>();
                    for(int j=0;j<array.length();j++){
                        JSONObject imageObject=array.getJSONObject(j);
                        String imageUrl="http://"+Config.IP+":8080/circle/"+imageObject.getString("image"+j);
                        imageList.add(imageUrl);
                        //Log.d("图片URL地址", "circleExcute: "+imageUrl);
                    }
                    friendCircle.setCircleImageUrlList(imageList);
                    friendCircle.setBitmap(headImage);
                    circleList.add(friendCircle);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return circleList;
    }
}
