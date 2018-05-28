package com.example.community.activity;
/**
 * 查看用户个人主页资料的界面
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.domain.PersonalData;
import com.example.community.utils.ImageUtils;

import org.litepal.crud.DataSupport;

public class UserDataActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView blurImageView;
    private ImageView headImageView;
    private TextView headNameText;
    private TextView userNameText;
    private TextView userSexText;
    private TextView userAgeText;
    private TextView userPhoneText;
    private TextView userMailText;
    private TextView userIntroduceText;
    private Button addFriend;
    private Button seeCircle;

    private String account;
    int[] imageId={R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p3,R.drawable.p4,R.drawable.p5,
            R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10,R.drawable.p11,R.drawable.p12,
            R.drawable.p13,R.drawable.p14,R.drawable.p15,R.drawable.p16};
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        initWindow();

        Intent intent=getIntent();
        account=intent.getStringExtra("account");

        blurImageView=findViewById(R.id.blurImageView);
        headImageView=findViewById(R.id.headImageView);
        headNameText=findViewById(R.id.headNameText);
        userNameText=findViewById(R.id.userNameText);
        userSexText=findViewById(R.id.userSexText);
        userAgeText=findViewById(R.id.userAgeText);
        userPhoneText=findViewById(R.id.userPhoneText);
        userMailText=findViewById(R.id.userMailText);
        userIntroduceText=findViewById(R.id.userIntroduceText);
        //显示数据
        showData();
        //按键控制
        addFriend=findViewById(R.id.addFriendButton);
        addFriend.setOnClickListener(this);
        seeCircle=findViewById(R.id.seeCircleButton);
        seeCircle.setOnClickListener(this);

    }
    //初始化，将状态栏和标题栏设为透明
    private void initWindow()
    {
        View view=getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    //显示个人资料
    private void showData(){
        PersonalData personalData= DataSupport.where("account=?",account).findFirst(PersonalData.class);
        //随机显示背景图片
        int n=new Long(Math.round(Math.random()*16)).intValue();
        Bitmap circleBitmap=BitmapFactory.decodeResource(getResources(),imageId[n]);
        Bitmap blurBitmap=ImageUtils.toBlur(circleBitmap);
        blurImageView.setImageBitmap(blurBitmap);
        //显示头像
        Bitmap headImage=ImageUtils.toRoundBitmap(ImageUtils.convertToBitmap(personalData.getImage()));
        headImageView.setImageBitmap(headImage);
        //个人姓名
        String name=personalData.getName();
        headNameText.setText(name);
        userNameText.setText(name);
        //个人性别
        String sex=personalData.getSex();
        userSexText.setText(sex);
        //个人年龄
        String age=personalData.getYear();
        userAgeText.setText(age);
        //个人电话
        String phone=personalData.getPhone();
        userPhoneText.setText(phone);
        //个人邮箱
        String mail=personalData.getMail();
        userMailText.setText(mail);
        //个人简介
        String introduce=personalData.getIntroduce();
        userIntroduceText.setText(introduce);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFriendButton:
                Toast.makeText(UserDataActivity.this,"添加好友",Toast.LENGTH_SHORT).show();
                break;
            case R.id.seeCircleButton:
                Toast.makeText(UserDataActivity.this,"查看动态",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UserDataActivity.this,FriendCircleActivity.class);
                intent.putExtra("account",account);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
