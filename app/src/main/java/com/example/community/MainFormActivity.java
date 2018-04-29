package com.example.community;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.community.frame.CircleFragment;
import com.example.community.frame.ContactFragment;
import com.example.community.frame.MessageFragment;

import java.util.ArrayList;

public class MainFormActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;
    private ArrayList<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        bottomNavigationBar=findViewById(R.id.bottomBar);
        //设置BottomNavigateBar的属性
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);//模式
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);//浮动形式
        bottomNavigationBar.setActiveColor("#00FF00");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.logo_message,"Message"))
                .addItem(new BottomNavigationItem(R.drawable.logo_contact,"Contact"))
                .addItem(new BottomNavigationItem(R.drawable.logo_circle,"Circle"))
                .setFirstSelectedPosition(0).initialise();
        fragments=getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);
    }
    @Override
    public void onTabSelected(int i) {
        if(fragments!=null){
            Log.d("选中", "onTabSelected: "+i);
            if(i<fragments.size()){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Fragment fragment=fragments.get(i);
                Log.d("fragment", "onTabSelected: "+fragment);
                ft.replace(R.id.content,fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int i) {
        if(fragments!=null){
            if(i<fragments.size()){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Fragment fragment=fragments.get(i);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int i) {

    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.replace(R.id.content,MessageFragment.newInstance("Message"));
        transaction.commit();
    }
    private ArrayList<Fragment> getFragments(){
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(MessageFragment.newInstance("Message"));
        fragments.add(ContactFragment.newInstance("Contact"));
        fragments.add(CircleFragment.newInstance("Circle"));
        return fragments;
    }
}
