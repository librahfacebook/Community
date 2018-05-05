package com.example.community.frame.basic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{
    //类标签
    protected static String TAG="";
    //上下文
    protected Context mContext=null;
    //依附的Activity
    protected Activity mActivity=null;
    private AlertDialog alertDialog;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG=this.getClass().getSimpleName();
        mContext=activity;
        mActivity=activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getContentViewId()!=0)
            return inflater.inflate(getContentViewId(),null);
        else
            return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    protected void initToolbar(Toolbar toolbar){
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);
    }
    protected void initToolbar(Toolbar toolbar,String title){
        toolbar.setTitle(title);
        initToolbar(toolbar);
    }


    /**
     * 获取当前Fragment的名称
     * @return
     */
    public String getFragmentName(){
        return TAG;
    }

    /** 初始化方法 */
    public void init() {}
    /** 设置布局 */
    protected abstract int getContentViewId();
    /** 初始化View的抽象方法 */
    public abstract void initViews(View view);
    /** 初始化数据 */
    protected void initData() {}
    /** 初始化事件的抽象方法 */
    public abstract void initEvents();
}
