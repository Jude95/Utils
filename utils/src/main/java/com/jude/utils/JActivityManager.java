package com.jude.utils;

/**
 * Created by Mr.Jude on 2015/2/12.
 * 管理Activity的类
 */

import android.app.Activity;
import android.text.TextUtils;

import java.util.LinkedList;

public class JActivityManager {
    private static LinkedList<Activity> activityStack;
    private static JActivityManager instance;

    private JActivityManager() {
    }

    public static JActivityManager getInstance() {
        if (instance == null) {
            instance = new JActivityManager();
        }
        return instance;
    }

    /**
     * 获得当前栈顶Activity
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.isEmpty())
            activity = activityStack.get(activityStack.size() - 1);
        return activity;
    }

    /**
     * 将当前Activity推入栈中
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new LinkedList<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 将Activity退出栈
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 主动退出Activity
     *
     * @param activity
     */
    public void closeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }




    public void closeAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (null == activity) {
                break;
            }
            closeActivity(activity);
        }
    }

    /**
     * 退出指定名字的activity
     */
    public void closeActivityByName(String name) {
        while (true) {
            Activity activity = currentActivity();
            if (null == activity) {
                break;
            }

            String activityName = activity.getComponentName().getClassName().toString();
            if (TextUtils.equals(name, activityName)) {
                continue;
            }
            popActivity(activity);
        }
    }

    /**
     * 获得当前ACTIVITY 名字
     */
    public String getCurrentActivityName() {
        Activity activity = currentActivity();
        String name = "";
        if (activity != null) {
            name = activity.getComponentName().getClassName().toString();
        }
        return name;
    }

}

