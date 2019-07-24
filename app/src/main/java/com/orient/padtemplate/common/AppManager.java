package com.orient.padtemplate.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 活动的管理
 *
 * Author WangJie
 * Created on 2018/9/6.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AppManager {

    private static AppManager instance;
    private Stack<Activity> activityStack = new Stack<>();

    public static AppManager getInstance(){
        if(instance == null)
            instance = new AppManager();
        return instance;
    }

    static {
        instance = new AppManager();
    }

    private AppManager() {
    }

    /**
     *  入栈
     */
    public void addActivity(Activity activity){
        activityStack.add(activity);
    }

    /**
     *  出栈
     */
    public void finishActivity(Activity activity){
        activity.finish();
        activityStack.push(activity);
    }

    /**
     *  栈顶的活动
     */
    public Activity currentActivity(){
        return activityStack.lastElement();
    }

    /**
     * 清理栈
     */
    public void finishAllActivity(){
        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     *  退出程序
     */
    public void exit(Context context){
        finishAllActivity();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        manager.killBackgroundProcesses(context.getPackageName());
        System.exit(0);
    }

}
