package com.example.community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        //获取用户注册账号以及密码
        signUpAccount=findViewById(R.id.signUp_Account);
        signUpPassword=findViewById(R.id.signUp_Password);
        signUp_user=new User();

        signUpButton=findViewById(R.id.signUp_Button);
        signUpButton.setOnClickListener(this);
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
