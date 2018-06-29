package com.example.community.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.example.community.R;
import com.example.community.custom.CustomViewPager;
import com.example.community.custom.RadarViewGroup;
import com.example.community.domain.Location;
import com.example.community.domain.PersonInfo;
import com.example.community.domain.PersonalData;
import com.example.community.service.LocationService;
import com.example.community.utils.ImageUtils;
import com.example.community.utils.Utility;
import com.example.community.utils.radar.FixedSpeedScroller;
import com.example.community.utils.radar.LogUtil;
import com.example.community.utils.radar.ZoomOutPageTransformer;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class NearPeopleActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,RadarViewGroup.IRadarClickListener{

    private CustomViewPager viewPager;
    private RelativeLayout ryContainer;
    private RadarViewGroup radarViewGroup;
    private int mPosition;
    private FixedSpeedScroller scroller;
    private SparseArray<PersonInfo> mDatas = new SparseArray<>();
    private LocationClient locationClient;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new LocationListener());
        setContentView(R.layout.activity_near_people);

        initView();
        //获取请求
        getRequestPermisssions();
        initData();

        /**
         * 将Viewpager所在容器的事件分发交给ViewPager
         */
       ryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        ViewpagerAdapter mAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(mAdapter);
        //设置缓存数为展示的数目
        Cursor cursor=DataSupport.findBySQL("select count(*) from PersonalData");
        cursor.moveToFirst();
        int count=cursor.getInt(0);
        Log.d("数量", "onCreate: "+mDatas.size());
        viewPager.setOffscreenPageLimit(mDatas.size());
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        //设置切换动画
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(this);
        setViewPagerSpeed(250);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                radarViewGroup.setDatas(mDatas);
            }
        }, 1500);
        radarViewGroup.setiRadarClickListener(this);
    }

    private void initData() {
        //获取使用者的账户信息
        SharedPreferences prefs=getSharedPreferences("Account_data",MODE_PRIVATE);
        String account=prefs.getString("account",null);
        //获取其余用户个人资料以及位置信息
        List<PersonalData> dataList=DataSupport.where("account!=?",account).find(PersonalData.class);
        List<Location> locationList=DataSupport.where("account!=?",account).find(Location.class);
        //查找使用者位置信息
        Location myLocation=DataSupport.where("account=?",account).findFirst(Location.class);
        LatLng myLatLng=new LatLng(Double.parseDouble(myLocation.getLatitude()),
                Double.parseDouble(myLocation.getLongitude()));

        for (int i = 0; i < dataList.size(); i++) {
            PersonInfo info = new PersonInfo();
            PersonalData personalData=dataList.get(i);
            //设置账户
            String otherAccount=personalData.getAccount();
            info.setAccount(otherAccount);
            Bitmap image= ImageUtils.convertToBitmap(personalData.getImage());
            info.setImage(ImageUtils.toRoundBitmap(image));
            //设置年龄
            String age=personalData.getYear();
            info.setAge(age);
            //设置姓名
            info.setName(personalData.getName());
            //设置性别
            String sex=personalData.getSex();
            if(sex.equals("男"))
                info.setSex(false);
            else
                info.setSex(true);
            double otherLongitude=0.0;
            double otherLatitude=0.0;
            for(Location locations:locationList){
                if(locations.getAccount().equals(personalData.getAccount())){
                    otherLongitude=Double.parseDouble(locations.getLongitude());
                    otherLatitude=Double.parseDouble(locations.getLatitude());
                    break;
                }
            }
            LatLng otherLatLng=new LatLng(otherLatitude,otherLongitude);
            double distance=getDistance(myLatLng,otherLatLng);
            info.setDistance((float) distance);
            mDatas.put(i, info);
        }
    }

    private void initView() {
        View view=getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        viewPager = (CustomViewPager) findViewById(R.id.vp);
        radarViewGroup = (RadarViewGroup) findViewById(R.id.radar);
        ryContainer = (RelativeLayout) findViewById(R.id.ry_container);
    }

    /**
     * 设置ViewPager切换速度
     *
     * @param duration
     */
    private void setViewPagerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedScroller(NearPeopleActivity.this, new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position;
    }

    @Override
    public void onPageSelected(int position) {
        radarViewGroup.setCurrentShowItem(position);
        LogUtil.m("当前位置 " + mPosition);
        LogUtil.m("速度 " + viewPager.getmSpeed());
        //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
        if (viewPager.getmSpeed() < -1800) {

            viewPager.setCurrentItem(mPosition + 2);
            LogUtil.m("位置 " + mPosition);
            viewPager.setmSpeed(0);
        } else if (viewPager.getmSpeed() > 1800 && mPosition > 0) {
            //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
            viewPager.setCurrentItem(mPosition - 1);
            LogUtil.m("位置 " + mPosition);
            viewPager.setmSpeed(0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRadarItemClick(int position) {
        viewPager.setCurrentItem(position);
    }




    class ViewpagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Log.d("位置", "instantiateItem: "+position);
            final PersonInfo info = mDatas.get(position);
            //设置一大堆演示用的数据
            View view = LayoutInflater.from(NearPeopleActivity.this).inflate(R.layout.viewpager_layout, null);
            ImageView ivPortrait = (ImageView) view.findViewById(R.id.iv);
            ImageView ivSex = (ImageView) view.findViewById(R.id.iv_sex);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvDistance = (TextView) view.findViewById(R.id.tv_distance);
            Log.d("姓名", "instantiateItem: "+info.getName());
            tvName.setText(info.getName()+"");
            tvDistance.setText(info.getDistance() + "km");
            ivPortrait.setImageBitmap(info.getImage());
            if (info.getSex()) {
                ivSex.setImageResource(R.drawable.girl);
            } else {
                ivSex.setImageResource(R.drawable.boy);
            }
            ivPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NearPeopleActivity.this, "这是 " + info.getName() + " >.<", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(NearPeopleActivity.this,UserDataActivity.class);
                    intent.putExtra("account",info.getAccount());
                    startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }

    private void requestLocation(){
        initLocation();
        locationClient.start();
    }
    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.setLocOption(option);
    }
    //查看请求是否被允许
    public void getRequestPermisssions(){
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(NearPeopleActivity.this, Manifest
                .permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(NearPeopleActivity.this, Manifest
                .permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(NearPeopleActivity.this, Manifest
                .permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(NearPeopleActivity.this, Manifest
                .permission.ACCESS_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(NearPeopleActivity.this,permissions,1);
        }else{
            requestLocation();
        }
    }
    //获取GPS定位请求
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本应用",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            location=new Location();
            //保存登录的用户账户
            SharedPreferences prefs=getSharedPreferences("Account_data",MODE_PRIVATE);
            String account=prefs.getString("account","");
            location.setAccount(account);
            //设置个人位置
            location.setLongitude(bdLocation.getLongitude()+"");
            location.setLatitude(bdLocation.getLatitude()+"");
            location.setAddress(bdLocation.getAddrStr()+bdLocation.getLocationDescribe());
            Log.d("账户", "onReceiveLocation: "+location.getAccount());
            Log.d("位置信息", "onReceiveLocation: "+location.getAddress());
            //更新用户位置信息
            location.updateAll("account=?",account);
            //上传个人位置信息
            LocationService.queryFromServer(location);
        }
    }
    //
    //计算地图上两点之间的距离
    public double getDistance(LatLng myLocation,LatLng otherLocation){
        //维度
        double myLat=(Math.PI/180)*myLocation.latitude;
        double otherLat=(Math.PI/180)*otherLocation.latitude;
        //经度
        double myLon=(Math.PI/180)*myLocation.longitude;
        double otherLon=(Math.PI/180)*otherLocation.longitude;
        //地球半径
        double R=6371.004;
        //计算两点之间的距离
        double distance=Math.acos(Math.sin(myLat)*Math.sin(otherLat)+
                Math.cos(myLat)*Math.cos(otherLat)*Math.cos(otherLon-myLon))*R;
        //保留两位小数
        BigDecimal bg=new BigDecimal(distance).setScale(2, RoundingMode.UP);
        //数字格式化对象
        /*NumberFormat format=NumberFormat.getNumberInstance();
        if(distance<1){
            //小于1千米，以米做单位
            format.setMaximumFractionDigits(1);
            distance*=1000;
            return format.format(distance)+"m";
        }else{
            format.setMaximumFractionDigits(2);
            return format.format(distance)+"km";
        }*/
        return bg.doubleValue();
    }
}
