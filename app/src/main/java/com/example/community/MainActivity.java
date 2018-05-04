package com.example.community;
/**
 * 基于地理位置的校园交友社区
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.community.domain.Config;

public class MainActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //判断是否以及成功登陆过
        SharedPreferences prefs= getSharedPreferences("Account_data",MODE_PRIVATE);
        String account=prefs.getString("account",null);
        if(account!=null){
            Config.Account=account;
            Intent intent=new Intent(this,MainFormActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this,StartActivity.class);
            startActivity(intent);
            finish();
        }


    }


}
