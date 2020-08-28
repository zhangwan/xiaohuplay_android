package com.tinytiger.titi.im.config;

import com.tinytiger.titi.im.DemoCache;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.umeng.analytics.MobclickAgent;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        DemoCache.clear();
        DropManager.getInstance().destroy();
        NIMClient.getService(AuthService.class).logout();
        MobclickAgent.onProfileSignOff();
    }

}
