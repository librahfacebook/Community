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
import com.example.community.service.ImageService;
import com.example.community.service.PersonalDataService;
import com.example.community.utils.ImageUtils;
import com.example.community.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendCircleActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView icon_return;
    private ImageView icon_publish;
    private TextView circleText;
    private GridView circleGirdView;
    private ImageView personalImage;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);
        initWindow();

        circleText=findViewById(R.id.circleText);
        circleGirdView=findViewById(R.id.circleGridView);
        personalImage=findViewById(R.id.personalCircleImage);
        personalName=findViewById(R.id.personalCircleName);

        circleList=(List<FriendCircle>) Utility.circleExcute("librah");
        circleList.addAll((List<FriendCircle>) Utility.circleExcute("cx"));
        recyclerView=findViewById(R.id.circleRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        if(circleList!=null)
            show();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //friendCircle=(FriendCircle) intent.getSerializableExtra("circle");
        circleList=(List<FriendCircle>) Utility.circleExcute("librah");

        //展示动态
//        if(friendCircle!=null)
//            show();
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
}
