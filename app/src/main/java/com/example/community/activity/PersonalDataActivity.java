package com.example.community.activity;
/**
 * 个人资料设置并上传到数据库
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.domain.Config;
import com.example.community.domain.PersonalData;
import com.example.community.service.PersonalDataService;
import com.example.community.utils.ImageUtils;
import com.example.community.view.SelectPhotoPopWindow;

import java.io.File;

public class PersonalDataActivity extends AppCompatActivity {

    private ImageView personalImage_edit;
    private EditText personalName_edit;
    private EditText personalSex_edit;
    private EditText personalYear_edit;
    private EditText personalPhone_edit;
    private EditText personalEmail_edit;
    private EditText personalIntroduce_edit;
    private Button imageTake;
    private Button personalSave;
    private String imageStr="";

    protected static final int CHOOSE_PICTURE=0;
    protected static final int TAKE_PICTURE=1;
    private static final int CROP_SMALL_PICTURE=2;
    protected static Uri tempUri;
    //将信息保存于SharePreferences中
    SharedPreferences.Editor editor;

    public static Config config;
    //自定义的弹出框类
    private SelectPhotoPopWindow menuWindow;
    public  static PersonalData personalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initWindow();
        personalImage_edit=findViewById(R.id.personalImage_edit);
        personalName_edit=findViewById(R.id.personalName_edit);
        personalSex_edit=findViewById(R.id.personalSex_edit);
        personalYear_edit=findViewById(R.id.personalYear_edit);
        personalPhone_edit=findViewById(R.id.personalPhone_edit);
        personalEmail_edit=findViewById(R.id.personalEmail_edit);
        personalIntroduce_edit=findViewById(R.id.personalIntroduce_edit);
        settings();
        imageTake=findViewById(R.id.imageTake);
        //个人信息保存
        personalSave=findViewById(R.id.personalSave);
        personalSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingData();
                PersonalDataService.queryFromServer(personalData,config);
                //睡眠1S后再判断(这里是为了防止产生第一次登录成功无法响应的事故)
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("Config", "onClick: "+config.Success);
                if(config.Success){
                    Toast.makeText(PersonalDataActivity.this,"用户信息修改成功",Toast.LENGTH_SHORT).show();
                    save();//保存用户信息
                    MainFormActivity.MainForm.finish();
                    Intent intent=new Intent(PersonalDataActivity.this,MainFormActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(PersonalDataActivity.this,"信息保存失败，请重新保存",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //上传照片
        //解决系统相机问题
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        imageTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow=new SelectPhotoPopWindow(PersonalDataActivity.this,itemOnClick);
                //显示窗口
                menuWindow.showAtLocation(PersonalDataActivity.this.findViewById(R.id.personalDataMain), Gravity.BOTTOM|
                Gravity.CENTER_HORIZONTAL,0,0);
            }
        });
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
    //将上述信息进行保存
    private void SettingData(){
        //将上述信息保存于PersonalData类中
        personalData=new PersonalData();
        //登录账户
        SharedPreferences prefs=getSharedPreferences("Account_data",MODE_PRIVATE);
        String account=prefs.getString("account","null");
        personalData.setAccount(account);
        //用户照片
        personalData.setImage(imageStr);
        //用户姓名
        String name=personalName_edit.getText().toString();
        personalData.setName(name);
        //用户性别
        String sex=personalSex_edit.getText().toString();
        personalData.setSex(sex);
        //用户年龄
        String year=personalYear_edit.getText().toString();
        personalData.setYear(year);
        //用户电话
        String phone=personalPhone_edit.getText().toString();
        personalData.setPhone(phone);
        //用户电子邮箱
        String email=personalEmail_edit.getText().toString();
        personalData.setMail(email);
        //用户简介
        String introduce=personalIntroduce_edit.getText().toString();
        personalData.setIntroduce(introduce);
    }
    //保存于手机自身内存里
    public void save(){
        editor=getSharedPreferences("PersonalData",MODE_PRIVATE).edit();
        editor.putString("name",personalData.getName());
        editor.putString("sex",personalData.getSex());
        editor.putString("year",personalData.getYear());
        editor.putString("phone",personalData.getPhone());
        editor.putString("mail",personalData.getMail());
        editor.putString("introduce",personalData.getIntroduce());
        editor.putString("image",personalData.getImage());
        editor.apply();
    }
    //从手机相册里选择照片
    private void choosePicture(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,CHOOSE_PICTURE);
    }
    //照相
    private void takePicture(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(),"image.jpg"
        )));
        startActivityForResult(intent,TAKE_PICTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case TAKE_PICTURE:
                    File temp=new File(Environment.getExternalStorageDirectory()+"/image.jpg");
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if(data!=null) {
                        setImageToView(data);
                    }
                    break;
            }
        }
    }
    //裁剪图片方法
    protected void startPhotoZoom(Uri uri){
        if(uri==null){
            Log.i("tag", "startPhotoZoom: The Uri is not exist.");
        }
        tempUri=uri;
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置裁剪
        intent.putExtra("crop","true");
        //图片宽高比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //裁剪图片宽高
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CROP_SMALL_PICTURE);
    }
    //保存裁剪之后的图片
    protected void setImageToView(Intent data){
        Bundle bundle=data.getExtras();
        if (bundle!=null){
            Bitmap photo=bundle.getParcelable("data");
            photo= ImageUtils.toRoundBitmap(photo);
            imageStr=ImageUtils.convertToString(photo);
            personalImage_edit.setImageBitmap(photo);
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()){
                case R.id.takePhotoButton:
                    takePicture();
                    break;
                case R.id.pickPhotoButton:
                    choosePicture();
                    break;
                default:
                    break;
            }
        }
    };
    //从缓存中填写个人资料
    public void settings(){
        SharedPreferences prefs=getSharedPreferences("PersonalData",MODE_PRIVATE);
        String image=prefs.getString("image",null);
        imageStr=image;
        if(!image.equals("")){
            Bitmap bitmap= ImageUtils.convertToBitmap(image);
            if(bitmap!=null)
                personalImage_edit.setImageBitmap(bitmap);
        }
        personalName_edit.setText(prefs.getString("name",null));
        personalSex_edit.setText(prefs.getString("sex",null));
        personalYear_edit.setText(prefs.getString("year",null));
        personalPhone_edit.setText(prefs.getString("phone",null));
        personalEmail_edit.setText(prefs.getString("mail",null));
        personalIntroduce_edit.setText(prefs.getString("introduce",null));
    }
}
