package com.example.community.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.domain.Config;
import com.example.community.domain.User;
import com.example.community.service.UserService;

/**
 * 用户注册个人账户及密码
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences.Editor editor;
    private EditText signUpAccount;
    private EditText signUpPassword;
    private Button signUpButton;
    private User signUp_user;
    final String action="register";
    public static Config config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initWindow();
        //获取用户注册账号以及密码
        signUpAccount=findViewById(R.id.signUp_Account);
        signUpPassword=findViewById(R.id.signUp_Password);
        signUp_user=new User();

        signUpButton=findViewById(R.id.signUp_Button);
        signUpButton.setOnClickListener(this);
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
        signUp_user.setAccount(signUpAccount.getText().toString());
        signUp_user.setPassword(signUpPassword.getText().toString());
        //Log.d("SignUp", "onCreate: "+signUp_user.getAccount()+" "+signUp_user.getPassword());
        UserService.queryFromServer(action,signUp_user,config);
        //睡眠1S后再判断(这里是为了防止产生第一次登录成功无法响应的事故)
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(config.Success){
            //注册成功
            Toast.makeText(SignUpActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
            clear();
            //返回登录界面
            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
            intent.putExtra("user",signUp_user);
            startActivity(intent);
            finish();
        }else{
            //注册失败
            Toast.makeText(SignUpActivity.this,"注册失败或者已有此账户",Toast.LENGTH_SHORT).show();
        }

    }
    //当注册另一个账号时清空内存
    private void clear(){
        editor=getSharedPreferences("Account_data",MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
