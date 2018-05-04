package com.example.community;
/**
 * 用户第一次登陆或注册的开始界面
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private Button signUpButton;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //登录按钮响应功能
        loginButton=findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        //注册按钮响应功能
        signUpButton=findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.signUpButton:
                intent=new Intent(this,SignUpActivity.class);
                startActivity(intent);
                break;
            default:
        }
        //关闭主界面
        finish();
    }
}
