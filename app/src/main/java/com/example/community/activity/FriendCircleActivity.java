package com.example.community.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.community.R;
import com.example.community.adapter.CircleAdapter;
import com.example.community.domain.Config;
import com.example.community.domain.FriendCircle;
import com.example.community.domain.PersonalData;
import com.example.community.service.ImageService;
import com.example.community.service.PersonalDataService;
import com.example.community.utils.ImageUtils;
import com.example.community.utils.Utility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendCircleActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView icon_return;
    private ImageView icon_publish;
    private TextView circleText;
    private GridView circleGirdView;
    private ImageView personalImage;
    private ImageView circleBackGround;
    private TextView personalName;
    private List<String> imageFilePath;
    private List<HashMap<String,Object>> imageItem;
    private List<FriendCircle> circleList;
    private String circleContext;
    private SimpleAdapter adapter;
    private FriendCircle friendCircle;
    private Config config;
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    int[] imageId={R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p3,R.drawable.p4,R.drawable.p5,
            R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10,R.drawable.p11,R.drawable.p12,
            R.drawable.p13,R.drawable.p14,R.drawable.p15,R.drawable.p16};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);

        circleText=findViewById(R.id.circleText);
        circleGirdView=findViewById(R.id.circleGridView);
        personalImage=findViewById(R.id.personalCircleImage);
        personalName=findViewById(R.id.personalCircleName);
        circleBackGround=findViewById(R.id.circleBackGround);

        icon_return=findViewById(R.id.icon_return);
        icon_return.setOnClickListener(this);
        icon_publish=findViewById(R.id.icon_publish);
        icon_publish.setOnClickListener(this);
        initWindow();

        Intent intent=getIntent();
        //判断查看朋友圈的动作行为（查看特定用户的朋友圈还是附近用户所有的朋友圈）
        String account=intent.getStringExtra("account");
        if(!account.equals("")){
            //查看特定用户的朋友圈
            icon_return.setVisibility(View.INVISIBLE);
            icon_publish.setVisibility(View.INVISIBLE);
            circleList=(List<FriendCircle>) Utility.circleExcute(account);
        }else{
            //查看附近所有用户的朋友圈
            //找到所有已注册用户的账户并显示其朋友圈
            SharedPreferences prefs=getSharedPreferences("Account_data",MODE_PRIVATE);
            String myAccount=prefs.getString("account",null);
            if(myAccount!=null)
                circleList=(List<FriendCircle>) Utility.circleExcute(myAccount);
            List<PersonalData> dataList= DataSupport.select("account").where("account!=?",myAccount).find(PersonalData.class);
            for(PersonalData data:dataList){
                circleList.addAll((List<FriendCircle>)Utility.circleExcute(data.getAccount()));
            }
        }
        recyclerView=findViewById(R.id.circleRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        if(circleList!=null)
            show();
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
        //显示背景图片
        showBackground();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //friendCircle=(FriendCircle) intent.getSerializableExtra("circle");
        circleList=(List<FriendCircle>) Utility.circleExcute("librah");

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
                //发布动态的界面
                Intent intent1=new Intent(FriendCircleActivity.this,WriteCircleActivity.class);
                startActivity(intent1);
                break;
        }
    }
    //展示朋友圈内容
    private void show(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CircleAdapter adapter=new CircleAdapter(circleList);
        recyclerView.setAdapter(adapter);
    }
    //随机展示朋友圈上方图片
    private void showBackground(){
        int n=new Long(Math.round(Math.random()*16)).intValue();
        Log.d("字符", "showBackground: "+n);
        circleBackGround.setImageResource(imageId[n]);
    }
}
