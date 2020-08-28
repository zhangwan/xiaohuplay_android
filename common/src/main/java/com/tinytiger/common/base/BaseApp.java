package com.tinytiger.common.base;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 *
 * @author zhw_luke
 * @date 2019/5/8 0008 下午 4:21
 * @Copyright 小虎互联科技
 * @since 4.1.0
 * @doc
 */
public class BaseApp extends Application {

    public static BaseApp _instance;
    public static Boolean Type4G=false;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);

        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
    }

    public static Context getContext() {
        return _instance.getApplicationContext();
    }

}
