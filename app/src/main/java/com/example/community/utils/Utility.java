package com.example.community.utils;

import android.util.Log;

import com.example.community.domain.Config;
import com.example.community.domain.PersonalData;
import com.example.community.service.PersonalDataService;

import org.json.JSONObject;


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
            if(!Config.Response.equals("")){
                JSONObject jsonObject=new JSONObject(Config.Response);
                personalData.setName(jsonObject.getString("name"));
                personalData.setSex(jsonObject.getString("sex"));
                personalData.setYear(jsonObject.getString("year"));
                personalData.setPhone(jsonObject.getString("phone"));
                personalData.setMail(jsonObject.getString("mail"));
                personalData.setIntroduce(jsonObject.getString("introduce"));
                personalData.setImage(jsonObject.getString("image"));
            }else{
                personalData=null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return personalData;
    }
}
