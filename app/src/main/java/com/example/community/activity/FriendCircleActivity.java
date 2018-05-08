package com.example.community.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.community.R;

public class FriendCircleActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView icon_return;
    private ImageView icon_publish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);
        initWindow();

        icon_return=findViewById(R.id.icon_return);
        icon_return.setOnClickListener(this);
        icon_publish=findViewById(R.id.icon_publish);
        icon_publish.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.icon_return:
                //返回主界面
                Intent intent=new Intent(FriendCircleActivity.this,MainFormActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.icon_publish:
                //发布动态
                Intent intent1=new Intent(FriendCircleActivity.this,WriteCircleActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
