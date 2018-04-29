package com.example.community;
/**
 * 基于地理位置的校园交友社区
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button loginButton;
    private Button signUpButton;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                intent=new Intent(MainActivity.this,MainFormActivity.class);
                startActivity(intent);
                break;
            case R.id.signUpButton:
                intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}
