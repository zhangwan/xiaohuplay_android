package com.tinytiger.titi.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;


import com.tinytiger.titi.R;
import com.tinytiger.titi.im.DemoCache;
import com.tinytiger.titi.im.DemoPrivatizationConfig;
import com.tinytiger.titi.im.config.preference.UserPreferences;
import com.tinytiger.titi.ui.main.MainActivity;
import com.netease.nim.uikit.api.wrapper.MessageRevokeTip;
import com.netease.nim.uikit.api.wrapper.NimUserInfoProvider;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.ServerAddresses;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.mixpush.MixPushConfig;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.IOException;

/**
 * Created by hzchenkang on 2017/9/26.
 * <p>
 * 云信sdk 自定义的SDK选项设置
 */

class NimSDKOptionConfig {

    static SDKOptions getSDKOptions(Context context) {
        SDKOptions options = new SDKOptions();
        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        initStatusBarNotificationConfig(options);
        // 配置 APP 保存图片/语音/文件/log等数据的目录
        options.sdkStorageRootPath = getAppCacheDir(context) + "/nim";
        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;
        // 配置附件缩略图的尺寸大小
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();
        // 通知栏显示用户昵称和头像
        options.userInfoProvider = new NimUserInfoProvider(DemoCache.getContext());
        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;
        // 在线多端同步未读数
        options.sessionReadAck = true;
        // 动图的缩略图直接下载原图
        options.animatedImageThumbnailEnabled = true;
        // 采用异步加载SDK
        options.asyncInitSDK = true;
        // 是否是弱IM场景
        options.reducedIM = false;
        // 是否检查manifest 配置，调试阶段打开，调试通过之后请关掉
        options.checkManifestConfig = false;
        // 是否启用群消息已读功能，默认关闭
      //  options.enableTeamMsgAck = true;
        // 打开消息撤回未读数-1的开关
        options.shouldConsiderRevokedMessageUnreadCount = true;
        // 云信私有化配置项
        configServerAddress(options, context);
        options.mixPushConfig = buildMixPushConfig();
        //        options.mNosTokenSceneConfig = createNosTokenScene();
       // options.loginCustomTag = "登录自定义字段";
        return options;
    }


    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + DemoCache.getContext().getPackageName();
        }
        return storageRootPath;
    }

    private static void configServerAddress(final SDKOptions options, Context context) {
        ServerAddresses serverConfig = DemoPrivatizationConfig.getServerAddresses(context);
        if (serverConfig != null) {
            options.serverConfig = serverConfig;
        }
        String appKey = DemoPrivatizationConfig.getAppKey(context);
        if (!TextUtils.isEmpty(appKey)) {
            options.appKey = appKey;
        }
    }

    private static void initStatusBarNotificationConfig(SDKOptions options) {
        // load 应用的状态栏配置
        StatusBarNotificationConfig config = loadStatusBarNotificationConfig();
        // load 用户的 StatusBarNotificationConfig 设置项
        StatusBarNotificationConfig userConfig = UserPreferences.getStatusConfig();
        if (userConfig == null) {
            userConfig = config;
        } else {
            // 新增的 UserPreferences 存储项更新，兼容 3.4 及以前版本
            // 新增 notificationColor 存储，兼容3.6以前版本
            // APP默认 StatusBarNotificationConfig 配置修改后，使其生效
            userConfig.notificationEntrance = config.notificationEntrance;
            userConfig.notificationFolded = config.notificationFolded;
            userConfig.notificationColor = config.notificationColor;
        }
        // 持久化生效
        UserPreferences.setStatusConfig(userConfig);
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = userConfig;
    }

    // 这里开发者可以自定义该应用初始的 StatusBarNotificationConfig
    private static StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        config.notificationEntrance = MainActivity.class;
        config.notificationSmallIconId = R.mipmap.ic_launcher;
      //  config.notificationColor = DemoCache.getContext().getResources().getColor(R.color.color_blue_3a9efb);
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.tinytiger.titi/raw/msg";
        config.notificationFolded = true;
        //        config.notificationFolded = false;
        config.downTimeEnableNotification = true;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 是否APP ICON显示未读数红点(Android O有效)
        config.showBadge = true;
        // save cache，留做切换账号备用
        DemoCache.setNotificationConfig(config);
        return config;
    }

    private static MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {

        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeRevokeMsgTip(String revokeAccount, IMMessage item) {
            return MessageRevokeTip.getRevokeTipContent(item, revokeAccount);
        }
    };

    private static MixPushConfig buildMixPushConfig() {
        // 第三方推送配置
        MixPushConfig config = new MixPushConfig();
        // 小米推送
  /*      config.xmAppId = "2882303761517731435";
        config.xmAppKey = "5651773125435";
        config.xmCertificateName = "TiTi电竞";*/

        return config;
    }
}
