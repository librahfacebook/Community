package com.example.community.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.community.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 1.通过SimpleAdapter适配器实现加载图片，在gridview点击函数中响应不同操作
 * 2.当点击图片（+）时，调用本地相册获取图片路径存于字符串pathImage
 * 3.获取图片路径后再onResume中刷新图片，通过GridView的setAdapter刷新加载图片
 * 4.点击图片时提示是否删除
 */
public class WriteCircleActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView icon_returnCircle;//返回上一层按钮
    private Button publish;//发布按钮
    private final int IMAGE_OPEN=1;//打开图片标记
    private GridView gridView;//网格显示缩略图
    public static Bitmap bitmap;//临时图片
    private String pathImage;//图片路径
    private ArrayList<HashMap<String,Object>> imageItem;
    private SimpleAdapter adapter;//适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_circle);
        initWindow();
        //获取控件对象
        gridView=findViewById(R.id.gridView);
        //获取资源图片加号
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.addimage);
        imageItem=new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map=new HashMap<>();
        map.put("itemImage",bitmap);
        imageItem.add(map);
        adapter=new SimpleAdapter(this,imageItem,R.layout.gridview_add,
                new String[]{"itemImage"},new int[]{R.id.imageView1});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i=(ImageView) view;
                    i.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(adapter);
        //监听GridView点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(imageItem.size()==10){
                    //图片已满
                    Toast.makeText(WriteCircleActivity.this,"不能再添加图片",Toast.LENGTH_SHORT).show();
                }else if(position==0){
                    //添加图片
                    Toast.makeText(WriteCircleActivity.this,"添加图片",Toast.LENGTH_SHORT).show();
                    //选择图片
                    Intent intent=new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,IMAGE_OPEN);
                }else{
                    dialog(position);
                }
            }
        });
        icon_returnCircle=findViewById(R.id.icon_returnCircle);
        icon_returnCircle.setOnClickListener(this);
        publish=findViewById(R.id.publish);
        publish.setOnClickListener(this);
    }
    //初始化，将状态栏和标题栏合二为一
    private void initWindow()
    {
        //取消透明状态栏
        Window window= getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.parseColor("#2E3238"));
        //防止键盘挡住输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_write_circle);
        if(ActivityCompat.checkSelfPermission(WriteCircleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_returnCircle:
                //返回朋友圈列表
                Intent intent=new Intent(WriteCircleActivity.this,FriendCircleActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.publish:
                //发表动态
                break;
        }
    }
    /**
     * 获取图片路径
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK&&requestCode==IMAGE_OPEN){
            Uri uri=data.getData();
            if(!TextUtils.isEmpty(uri.getAuthority())){
                //查询选择图片
                Cursor cursor=getContentResolver().query(uri,new String[]{MediaStore.Images.Media.DATA},
                        null,null,null);
                //返回，没找到选择图片
                if(cursor==null){
                    return;
                }
                //光标移动至开头，选择图片路径
                cursor.moveToFirst();
                pathImage=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
    }
    /**
     * 刷新图片
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){
            Log.d("图片路径", "onResume: "+pathImage);
            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            HashMap<String,Object> map=new HashMap<>();
            map.put("itemImage",addbmp);
            imageItem.add(map);
            adapter=new SimpleAdapter(this,imageItem,R.layout.gridview_add,
                    new String[]{"itemImage"},new int[]{R.id.imageView1});
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i=(ImageView) view;
                        i.setImageBitmap((Bitmap)data);
                        return true;
                    }
                    return false;
                }
            });
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage=null;
        }
    }

    /**
     * 提示用户删除操作
     */
    protected void dialog(final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(WriteCircleActivity.this);
        builder.setMessage("确认删除图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}