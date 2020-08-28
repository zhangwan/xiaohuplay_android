package com.tinytiger.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.Stack;

/**
 * @author luke
 * @date 2018/6/30 13:54
 * @doc 应用程序Activity管理类：
 * 用于Activity管理和应用程序退出
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }


    /**
     * 获取栈数量
     * @return
     */
    public int getStackSize(){
        return activityStack.size();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {

        for (int i = 0; i < activityStack.size() - 1; i++) {
            if (null != activityStack.get(i) && (activityStack.get(i).getClass().equals(cls))) {
                finishActivity(activityStack.get(i));
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity
     * 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishNotSpecifiedActivity(Class<?> cls) {
        for (int i = 0; i < activityStack.size() - 1; i++) {
            if (null != activityStack.get(i) && !(activityStack.get(i).getClass().equals(cls))) {
                finishActivity(activityStack.get(i));
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity_1() {
        for (int i = 1, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }


    /**
     * 指定类名的Activity是否存在
     */
    public Boolean existsActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定类名的Activity是否存在
     */
    public Boolean existsActivity(Activity cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除已经finish的activity
     *
     * @param sourceActivity
     */
    public void removeTemporaryActivities(final Class<Activity> targetclazz,
                                          final Activity sourceActivity) {
        if (targetclazz == null || sourceActivity == null) {
            return;
        }

        int begin = -1;
        int end = -1;
        Activity activity;

        for (int i = activityStack.size() - 1; i >= 0; i--) {
            activity = activityStack.get(i);
            if (activity.getClass() == targetclazz && end == -1) {
                end = i;
            }
            if (sourceActivity == activity && begin == -1) {
                begin = i;
            }
            if (begin != -1 && end != -1) {
                break;
            }
        }

        if (end != -1 && begin > end) {
            for (int i = begin; i > end; i--) {
                activity = activityStack.get(i);
                finishActivity(activity);
            }
        }
    }


}
