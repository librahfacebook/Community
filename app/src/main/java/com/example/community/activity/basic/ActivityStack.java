package com.example.community.activity.basic;

import android.app.Activity;

import java.util.LinkedList;

/**
 * 管理Activity的栈
 */
public class ActivityStack {
    public LinkedList<Activity> activityList=null;

    public ActivityStack(){
        activityList=new LinkedList<Activity>();
    }
    //添加activity到activityList中
    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    //获取栈顶activity
    public Activity getLastActivity(){
        return activityList.getLast();
    }
    //移除activity
    public void removeActivity(Activity activity){
        if(activity!=null&&activityList.contains(activity)){
            activityList.remove(activity);
        }
    }
    //判断某一Activity是否运行
    public boolean isActivityRunning(String classname){
        if(classname!=null){
            for(Activity activity:activityList){
                if(activity.getClass().getName().equals(classname))
                    return true;
            }
        }
        return false;
    }
    //退出所有的Activity
    public void finishAllActivity(){
        for(Activity activity:activityList){
            if(activity!=null)
                activity.finish();
        }
    }
    //退出应用程序
    public void appExit(){
        try{
            finishAllActivity();
            //杀死应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
