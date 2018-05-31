package com.example.community.activity;

/**
 * 主界面设置底部导航栏，并且可以实现自由切换
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.community.R;
import com.example.community.domain.Config;
import com.example.community.domain.PersonalData;
import com.example.community.frame.CircleFragment;
import com.example.community.frame.ContactFragment;
import com.example.community.frame.MessageFragment;
import com.example.community.utils.ImageUtils;
import com.example.community.utils.Utility;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainFormActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    public static Activity MainForm=null;
    private ImageView personalImage;
    private Bitmap bitmap;
    private PersonalData pd;
    private TextView personalName;
    private TextView personalSex;
    private TextView personalYear;
    private TextView personalPhone;
    private TextView personalMail;
    private TextView personalIntroduce;


    private BottomNavigationBar bottomNavigationBar;
    private Button personalEdit;
    private Button reloginButton;
    private ArrayList<Fragment> fragments;
    private PersonalData personalData;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);
        initWindow();
        MainForm=this;
        //底部导航栏设置
        bottomNavigationBar=findViewById(R.id.bottomBar);
        //设置BottomNavigateBar的属性
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);//模式
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);//浮动形式
        bottomNavigationBar.setActiveColor("#00FF00");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.logo_message,"Message"))
                .addItem(new BottomNavigationItem(R.drawable.logo_contact,"Focus"))
                .addItem(new BottomNavigationItem(R.drawable.logo_circle,"Circle"))
                .setFirstSelectedPosition(0).initialise();
        fragments=getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

        //左侧个人资料设置
        personalEdit=findViewById(R.id.personalEdit);
        personalImage=findViewById(R.id.personalImage);
        personalName=findViewById(R.id.personalName);
        personalSex=findViewById(R.id.personalSex);
        personalYear=findViewById(R.id.personalYear);
        personalPhone=findViewById(R.id.personalPhone);
        personalMail=findViewById(R.id.personalEmail);
        personalIntroduce=findViewById(R.id.personalIntroduce);
        writePersonalData();
        personalEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainFormActivity.this,PersonalDataActivity.class);
                startActivity(intent);
            }
        });
        //重新登录按钮设置
        reloginButton=findViewById(R.id.ReloginButton);
        reloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空内存
                clear();
                Intent intent=new Intent(MainFormActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onTabSelected(int i) {
        if(fragments!=null){
            Log.d("选中", "onTabSelected: "+i);
            if(i<fragments.size()){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Fragment fragment=fragments.get(i);
                Log.d("fragment", "onTabSelected: "+fragment);
                ft.replace(R.id.content,fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int i) {
        if(fragments!=null){
            if(i<fragments.size()){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Fragment fragment=fragments.get(i);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int i) {

    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.replace(R.id.content,MessageFragment.newInstance("Message"));
        transaction.commit();
    }
    private ArrayList<Fragment> getFragments(){
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(MessageFragment.newInstance("Message"));
        fragments.add(ContactFragment.newInstance("Contact"));
        fragments.add(CircleFragment.newInstance("Circle"));
        return fragments;
    }
    //初始化，将状态栏和标题栏设为透明
    private void initWindow()
    {
        //取消透明状态栏
        Window window= getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.parseColor("#2E3238"));
    }
    /**
     * 将个人资料进行填写
     */
    private void writePersonalData()
    {
        prefs=getSharedPreferences("PersonalData",MODE_PRIVATE);
        Log.d("加载主页面", "writePersonalData: ");
        if(prefs.contains("name")){
            Log.d("Prefs", "writePersonalData: ");
            settings();
        }else{
            Log.d("从数据库中查询个人信息", "writePersonalData: ");
            personalData= Utility.dataExecute();
            if(personalData!=null){
                save();//保存于手机缓存中
                settings();
            }
        }

    }
    //从缓存中填写个人资料
    public void settings(){
        String image=prefs.getString("image",null);
        if(!image.equals("")){
            Bitmap bitmap= ImageUtils.convertToBitmap(image);
            if(bitmap!=null){
                Config.headPhoto=bitmap;
                personalImage.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
            }
        }
        String name=prefs.getString("name",null);
        if(!name.equals("null")) {
            Config.Name=name;
            personalName.setText(name);
        }
        String sex=prefs.getString("sex",null);
        if(!sex.equals("null"))
            personalSex.setText(sex);
        String year=prefs.getString("year",null);
        if(!year.equals("null"))
            personalYear.setText(year);
        String phone=prefs.getString("phone",null);
        if(!phone.equals("null"))
            personalPhone.setText(phone);
        String mail=prefs.getString("mail",null);
        if(!mail.equals("null"))
            personalMail.setText(mail);
        String introduce=prefs.getString("introduce",null);
        if(!introduce.equals("null"))
            personalIntroduce.setText(introduce);
    }
    //保存于手机自身内存里
    public void save(){
        prefs=getSharedPreferences("PersonalData",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        if(personalData.getName()!=null) {
            editor.putString("name", personalData.getName());
            editor.putString("sex", personalData.getSex());
            editor.putString("year", personalData.getYear());
            editor.putString("phone", personalData.getPhone());
            editor.putString("mail", personalData.getMail());
            editor.putString("introduce", personalData.getIntroduce());
            editor.putString("image", personalData.getImage());
            editor.apply();
        }
    }
    //清空手机内存
    public void clear(){
        SharedPreferences prefs=getSharedPreferences("PersonalData",MODE_PRIVATE);
        SharedPreferences prefs1=getSharedPreferences("Account_data",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        SharedPreferences.Editor editor1=prefs1.edit();
        editor.clear();
        editor.apply();
        editor1.clear();
        editor1.apply();
        //删除数据库
        LitePal.deleteDatabase("community");
    }
}
