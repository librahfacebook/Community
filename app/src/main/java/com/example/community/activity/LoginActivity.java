package com.example.community.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.domain.Config;
import com.example.community.domain.Location;
import com.example.community.domain.PersonalData;
import com.example.community.domain.User;
import com.example.community.service.UserService;
import com.example.community.utils.Utility;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;


/**
 * 用户登录个人账户及密码
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences.Editor editor;//将账号以及密码保存起来
    private EditText loginAccount;
    private EditText loginPassword;
    private Button loginButton;
    private User loginUser;
    public static Config config;
    final String action="login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWindow();
        loginAccount=findViewById(R.id.login_Account);
        loginPassword=findViewById(R.id.login_Password);
        //接受从注册层返回来的数据
        Intent intent1=getIntent();
        loginUser=(User)intent1.getSerializableExtra("user");
        if(loginUser!=null) {
            //直接登录
            loginAccount.setText(loginUser.getAccount());
            loginPassword.setText(loginUser.getPassword());
        }else{
            loginUser=new User();
        }
        loginButton=findViewById(R.id.login_Button);
        loginButton.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        loginUser.setAccount(loginAccount.getText().toString());
        loginUser.setPassword(loginPassword.getText().toString());
        UserService.queryFromServer(action,loginUser,config);
        //睡眠1S后再判断(这里是为了防止产生第一次登录成功无法响应的事故)
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("登录情况", "onClick: "+config.Success);
       if(config.Success){
            //用户验证成功，则切换到主界面，同时将账号以及密码保存起来
            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            Save(loginUser);
            //创建litepal数据库
            Connector.getDatabase();
            queryDataWithInsert();
            saveLocationData();
            Intent intent=new Intent(LoginActivity.this, MainFormActivity.class);
            startActivity(intent);
            finish();
        }else{
            //用户验证失败，重新登录
            Toast.makeText(LoginActivity.this,"登录失败，请检查用户名或密码",
                    Toast.LENGTH_SHORT).show();
        }
    }
    //保存用户登录数据
    private void Save(User user){
        Config.Account=user.getAccount();
        editor=getSharedPreferences("Account_data",MODE_PRIVATE).edit();
        editor.putString("account",user.getAccount());
        editor.putString("password",user.getPassword());
        editor.apply();
    }
    //将服务器中已注册用户发出请求信息并保存用户资料
    private void queryDataWithInsert(){
        List<PersonalData> dataList= Utility.savePersonalData();
        for(PersonalData pd:dataList){
            pd.save();
        }
    }
    //保存用户位置信息至数据库
    private void saveLocationData(){
        List<Location> locationList=Utility.otherLocationsExcute();
        for(Location location:locationList){
            Log.d("用户位置信息", "saveLocationData: "+location.getAddress());
            location.save();
        }
    }
}
