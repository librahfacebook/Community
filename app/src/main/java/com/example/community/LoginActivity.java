package com.example.community;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.community.domain.Config;
import com.example.community.domain.User;
import com.example.community.service.UserService;


/**
 * 用户登录个人账户及密码
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

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
            //用户验证成功，则切换到主界面
            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(LoginActivity.this, MainFormActivity.class);
            startActivity(intent);
        }else{
            //用户验证失败，重新登录
            Toast.makeText(LoginActivity.this,"登录失败，请检查用户名或密码",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
