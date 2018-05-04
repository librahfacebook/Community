package com.example.community;
/**
 * 个人资料设置并上传到数据库
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.community.domain.Config;
import com.example.community.domain.PersonalData;
import com.example.community.service.PersonalDataService;

import java.util.LinkedHashSet;
import java.util.Set;

public class PersonalDataActivity extends AppCompatActivity {

    private ImageView personalImage_edit;
    private EditText personalName_edit;
    private EditText personalSex_edit;
    private EditText personalYear_edit;
    private EditText personalPhone_edit;
    private EditText personalEmail_edit;
    private EditText personalIntroduce_edit;
    private Button imageTake;
    private Button personalSave;

    //将信息保存于SharePreferences中
    SharedPreferences.Editor editor;

    public static Config config;

    public  static PersonalData personalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        personalImage_edit=findViewById(R.id.personalImage_edit);
        personalName_edit=findViewById(R.id.personalName_edit);
        personalSex_edit=findViewById(R.id.personalSex_edit);
        personalYear_edit=findViewById(R.id.personalYear_edit);
        personalPhone_edit=findViewById(R.id.personalPhone_edit);
        personalEmail_edit=findViewById(R.id.personalEmail_edit);
        personalIntroduce_edit=findViewById(R.id.personalIntroduce_edit);

        imageTake=findViewById(R.id.imageTake);
        //个人信息保存
        personalSave=findViewById(R.id.personalSave);
        personalSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingData();
                PersonalDataService.queryFromServer(personalData,config);
                //睡眠1S后再判断(这里是为了防止产生第一次登录成功无法响应的事故)
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("Config", "onClick: "+config.Success);
                if(config.Success){
                    Toast.makeText(PersonalDataActivity.this,"用户信息修改成功",Toast.LENGTH_SHORT).show();
                    save();//保存用户信息
                    Intent intent=new Intent(PersonalDataActivity.this,MainFormActivity.class);
                    intent.putExtra("personalData",personalData);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(PersonalDataActivity.this,"信息保存失败，请重新保存",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //将上述信息进行保存
    private void SettingData(){
        //将上述信息保存于PersonalData类中
        personalData=new PersonalData();
        //登录账户
        SharedPreferences prefs=getSharedPreferences("Account_data",MODE_PRIVATE);
        String account=prefs.getString("account","null");
        personalData.setAccount(account);
        //用户照片
        String image="http://c.hiphotos.baidu.com/image/pic/item/d50735fae6cd7b89acbea9df032442a7d8330e9f.jpg";
        personalData.setImage(image);
        //用户姓名
        String name=personalName_edit.getText().toString();
        personalData.setName(name);
        //用户性别
        String sex=personalSex_edit.getText().toString();
        personalData.setSex(sex);
        //用户年龄
        String year=personalYear_edit.getText().toString();
        personalData.setYear(year);
        //用户电话
        String phone=personalPhone_edit.getText().toString();
        personalData.setPhone(phone);
        //用户电子邮箱
        String email=personalEmail_edit.getText().toString();
        personalData.setMail(email);
        //用户简介
        String introduce=personalIntroduce_edit.getText().toString();
        personalData.setIntroduce(introduce);
    }
    //保存于手机自身内存里
    public void save(){
        editor=getSharedPreferences("PersonalData",MODE_PRIVATE).edit();
        editor.putString("name",personalData.getName());
        editor.putString("sex",personalData.getSex());
        editor.putString("year",personalData.getYear());
        editor.putString("phone",personalData.getPhone());
        editor.putString("mail",personalData.getMail());
        editor.putString("introduce",personalData.getIntroduce());
        editor.putString("image",personalData.getImage());
        editor.apply();
    }
}
