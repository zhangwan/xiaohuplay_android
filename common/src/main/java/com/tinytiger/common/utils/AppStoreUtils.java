package com.tinytiger.common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.tinytiger.common.R;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.common.utils.toast.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Desction:
 */
public class AppStoreUtils {

    //  检查版本更新，跳转到腾讯应用宝进行下载
    public static void intit_getClick(Context mContext) {
       /* if (isAvilible(mContext, "com.tencent.android.qqdownloader")) {
            // 市场存在
            launchAppDetail(mContext.getApplicationContext(), "com.jumeng.lsj", "com.tencent.android.qqdownloader");
        } else {*/
        try {
            Uri uri = Uri.parse(SpUtils.getString(R.string.android_web_url, "0").trim());
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}


    }


    /**
     * 启动到app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) {
                return;
            }
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 判断市场是否存在的方法
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
// 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }


    /**
     * @author zhw_luke
     * @date 2018/9/21 0021 上午 10:08
     * @doc 获取手机内所有app信息
     * pkg 包名
     * cls 主Activity
     */
    public static void startApp(Context context) {
        // 王者荣耀 com.tencent.tmgp.sgame
        // 和平精英 com.tencent.tmgp.pubgmhd

        //pkg---com.tencent.tmgp.pubgmhd  cls---com.epicgames.ue4.SplashActivity
        //pkg---com.tencent.tmgp.sgame  cls---com.tencent.tmgp.sgame.SGameActivity

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPackageManager = context.getPackageManager();
        List<ResolveInfo> mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        //按报名排序
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

        for (ResolveInfo res : mAllApps) {
            //该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;

            Logger.d("pkg---" + pkg + "  cls---" + cls);
            // Log.d("pkg_cls","pkg---" +pkg +"  cls---" + cls );

        }
    }


    /**
     * @author zhw_luke
     * @date 2018/9/21 0021 上午 10:21
     * @doc 打开王者荣耀
     */
    public static void startSgame(Context mContext) {
        // 王者荣耀 com.tencent.tmgp.sgame
        //pkg---com.tencent.tmgp.sgame  cls---com.tencent.tmgp.sgame.SGameActivity

        if (isAvilible(mContext, "com.tencent.tmgp.sgame")) {
            // 市场存在
            ComponentName componet = new ComponentName("com.tencent.tmgp.sgame", "com.tencent.tmgp.sgame.SGameActivity");
            //pkg 就是第三方应用的包名
            //cls 就是第三方应用的进入的第一个Activity
            Intent intent = new Intent();
            intent.setComponent(componet);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            ToastUtils.toshort(mContext, "您还未安装王者荣耀");
        }

    }


    /**
     * @author zhw_luke
     * @date 2018/9/21 0021 上午 10:21
     * @doc 打开和平精英
     */
    public static void startPubgmhd(Context mContext) {

        // 和平精英
        //pkg---com.tencent.tmgp.pubgmhd
        // cls---com.epicgames.ue4.SplashActivity


        if (isAvilible(mContext, "com.tencent.tmgp.pubgmhd")) {
            // 市场存在
            ComponentName componet = new ComponentName("com.tencent.tmgp.pubgmhd", "com.epicgames.ue4.SplashActivity");
            //pkg 就是第三方应用的包名
            //cls 就是第三方应用的进入的第一个Activity
            Intent intent = new Intent();
            intent.setComponent(componet);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            ToastUtils.toshort(mContext, "您还未安装和平精英");
        }

    }

    /**
     * @author zhw_luke
     * @date 2018/9/21 0021 上午 10:21
     * @doc 是否存在微信
     */
    public static boolean startWX(Context mContext) {
        boolean type = false;
        if (isAvilible(mContext, "com.tencent.mm")) {
            type = true;
        } else {
            ToastUtils.toshort(mContext, "您还未安装微信");
        }
        return type;
    }
    /**
     * @author zhw_luke
     * @date 2018/9/21 0021 上午 10:21
     * @doc 是否存在QQ
     */
    public static boolean startQQ(Context mContext) {
        boolean type = false;
        if (isAvilible(mContext, "com.tencent.mobileqq")) {
            type = true;
        } else {
            ToastUtils.toshort(mContext, "您还未安装QQ");
        }
        return type;
    }

}