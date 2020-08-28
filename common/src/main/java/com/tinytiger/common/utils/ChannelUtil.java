package com.tinytiger.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.tinytiger.common.R;
import com.tinytiger.common.utils.preference.SpUtils;

/**
 *
 * @author zhw_luke
 * @date 2019/5/29 0029 下午 4:23
 * @Copyright 小虎互联科技
 * @since 4.1.0
 * @doc 渠道工具
 */
public class ChannelUtil {

    private static String mChannel;
    //默认渠道版本
    private static final String NORMAL_CHANNEL_VALUE = "999";

    /**
     * 返回市场。  如果获取失败返回""
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        return getChannel(context, NORMAL_CHANNEL_VALUE);
    }

    /**
     * 返回市场。  如果获取失败返回defaultChannel
     *
     * @param context
     * @param defaultChannel
     * @return
     */
    public static String getChannel(Context context, String defaultChannel) {

        //内存中获取
        if (!TextUtils.isEmpty(mChannel)) {
            return mChannel;
        }

        mChannel=SpUtils.getString(R.string.channel,"");
        if (!TextUtils.isEmpty(mChannel)) {
            return mChannel;
        }

        //从apk中获取
        mChannel = getChannelApp(context);
        if (!TextUtils.isEmpty(mChannel)) {
            SpUtils.saveSP(R.string.channel, mChannel);
            return mChannel;
        }
        //全部获取失败
        return defaultChannel;
    }


    public static String getChannelApp(Context context) {
        String key = "999";
        String meta_data = getAppMetaData(context);
        if (meta_data.equals("titi")) {
            key = "1";
        } else if (meta_data.equals("baidu")) {
            key = "2";
        } else if (meta_data.equals("yingyongbao")) {
            key = "3";
        } else if (meta_data.equals("xiaomi")) {
            key = "4";
        } else if (meta_data.equals("vivo")) {
            key = "5";
        } else if (meta_data.equals("oppo")) {
            key = "6";
        } else if (meta_data.equals("meizu")) {
            key = "7";
        } else if (meta_data.equals("huawei")) {
            key = "8";
        } else if (meta_data.equals("ttt")) {
            key = "9";
        } else if (meta_data.equals("ali")) {
            key = "10";
        } else if (meta_data.equals("c360")) {
            key = "11";
        } else if (meta_data.equals("yingyongbao2")) {
            key = "12";
        }
        return key;
    }



	/*
		360-1
		百度-2
		应用宝-3
		小米-4
		VIVO-5
		oppo-6
		魅族-7
		华为-8
		锤子-9
		阿里-10
		未知-999
		*/

    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context) {
        if (context == null) {
            return "titi";
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber == null ? "titi" : channelNumber;
    }


    /**
     * 获取可提供下载功能的渠道
     * 区别版本,公版,私版(提供所有功能)
     */
    public static void getChannelApk(Context context){
       String mChannel= getChannel(context);
        boolean key = false;
        if (mChannel.equals("1")) {
            key = true;
        } else if (mChannel.equals("2")) {
            key =true;
        } else if (mChannel.equals("3")) {
            key = true;
        } else if (mChannel.equals("4")) {

        } else if (mChannel.equals("5")) {

        } else if (mChannel.equals("6")) {

        } else if (mChannel.equals("7")) {

        } else if (mChannel.equals("8")) {

        } else if (mChannel.equals("9")) {

        } else if (mChannel.equals("10")) {
            key = true;
        } else if (mChannel.equals("11")) {
            key = true;
        } else if (mChannel.equals("12")) {
            key = true;
        } else {

        }

        SpUtils.saveSP(R.string.download_apk, key);
    }
}
