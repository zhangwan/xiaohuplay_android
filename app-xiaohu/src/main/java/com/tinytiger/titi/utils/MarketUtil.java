package com.tinytiger.titi.utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhw_luke
 * @date 2019/11/25 0025 下午 3:58
 * @Copyright 小虎互联科技
 * @doc 应用市场跳转
 * @since 5.0.0
 */
public class MarketUtil {

    /**
     * 启动到应用商店app详情界面
     *
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void launchAppDetail(Context context, String marketPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getApplicationContext().getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goToYingYongBaoWeb(context);
        }
    }


   /*
    com.tencent.android.qqdownloader	应用宝
    com.qihoo.appstore	360手机助手
    com.baidu.appsearch	百度手机助
    com.xiaomi.market	小米应用商店
    com.wandoujia.phoenix2	豌豆荚
    com.huawei.appmarket	华为应用市场
    com.taobao.appcenter	淘宝手机助手
    com.meizu.mstore        魅族


        private final static String MARKET_PKG_NAME_VIVO = "com.bbk.appstore";
    private final static String MARKET_PKG_NAME_OPPO = "com.oppo.market";
    */


    /**
     * 跳转到应用宝网页版
     */
    public void goToYingYongBaoWeb(Context context) {
        try {
            Uri uri = Uri.parse("https://a.app.qq.com/o/simple.jsp?pkgname=com.tinytiger.titi");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 遍历手机内应用包名
     *
     * @param context
     */
    public static ArrayList<ResolveInfo> loadApps(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(intent, 0);

        ArrayList appList = new ArrayList<ResolveInfo>();

        //for循环遍历ResolveInfo对象获取包名和类名
        for (int i = 0; i < apps.size(); i++) {
            String packageName = apps.get(i).activityInfo.packageName;
            Boolean being = true;
            switch (packageName) {
                case "com.xiaomi.market":
                case "com.qihoo.appstore":
                case "com.bbk.appstore":
                case "com.oppo.market":
                case "com.tencent.android.qqdownloader":
                case "com.huawei.appmarket":
                case "com.baidu.appsearch":
                case "com.meizu.mstore":
                case "com.pp.assistant":
                case "com.wandoujia.phoenix2":
                    break;
                default:
                    being = false;
                    break;
            }
            if (being) {
                appList.add(apps.get(i));
            }
        }
        return appList;
    }


    /**
     * @param context
     * @return
     */
    public static List<ResolveInfo> loadAppsAll(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(intent, 0);
        ArrayList appList = new ArrayList<ResolveInfo>();
        for (int i = 0; i < apps.size(); i++) {
            String name = apps.get(i).activityInfo.packageName;
            if (!name.contains("huawei") && !name.contains("android") && !name.contains("xiaomi")) {
                appList.add(apps.get(i));
            }
        }
        return appList;
    }


    /**
     * 获取apk安装信息
     *
     * @param context
     * @param pkgName apk包名
     * @return apk包信息
     */
    public static PackageInfo getAppInstalled(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return null;
        }

        try {
            return context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent getAppOpenIntentByPackageName(Context context, String packageName) {
        //Activity完整名
        String mainAct = null;
        //根据包名寻找
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);


        @SuppressLint("WrongConstant") List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }

    public static Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    public static boolean openPackage(Context context, String packageName) {
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (pkgContext != null && intent != null) {
            intent.putExtra("openMoudle", "serviceHall");
            pkgContext.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 跳转应用信息页
     * @param context
     * @param packageName
     */
    public static void showInstalledAppDetails(Context context, String packageName) {
   /*     Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME, packageName, null);
        intent.setData(uri);
        context.startActivity(intent);*/

        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:"+packageName));
        context.startActivity(intent);
    }

}