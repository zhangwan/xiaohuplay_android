package com.tinytiger.titi.app;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.haima.hmcp.HmcpManager;
import com.haima.hmcp.listeners.OnInitCallBackListener;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tinytiger.common.NetConstants_B;
import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.utils.ChannelUtil;
import com.tinytiger.titi.R;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;


/**
 * @author zhw_luke
 * @date 2019/12/31 0031 上午 10:26
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.tinytiger.titi.service.action.INIT";

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    public InitializeService() {
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (ACTION_INIT_WHEN_APP_CREATE.equals(intent.getAction())) {
                performInit();
                startYunGame();
            }
        }
    }

    private void performInit() {
        //context = getApplicationContext();

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId(R.color.transparent, R.color.gray66).setReboundDuration(350);
                return new ClassicsHeader(context).setEnableLastTime(false);
            }
        });

        //微信
        com.umeng.socialize.PlatformConfig.setWeixin("wxe9cd6333c243aba9", "928824d1f26b667b213208aa1d455961");
        com.umeng.socialize.PlatformConfig.setQQZone("1110196346", "DzK4FzZa0JZe6MwD");

        //新浪微博(第三个参数为回调地址)
        // com.umeng.socialize.PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com/sina2/callback");


        if (NetConstants_B.LOG_TYPE) {
            //测试埋点
            UMConfigure.init(this, "5f35faa9d30932215478705f", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        } else {
            //正式环境埋点
            UMConfigure.init(this, "5edf3b53895cca84cb000033", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        }

        UMConfigure.setLogEnabled(false);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    private void startYunGame() {
        HmcpManager manager = HmcpManager.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString(HmcpManager.ACCESS_KEY_ID, "e87ec3dac90");
        bundle.putString(HmcpManager.CHANNEL_ID, ChannelUtil.getChannel(BaseApp.getContext()));
        manager.init(bundle, this, new OnInitCallBackListener() {
            @Override
            public void success() {

            }

            @Override
            public void fail(String msg) {
                Logger.d("startYunGame-----------fail--------" + msg);
                // Log.e(TAG, "init fail--------" + msg);
                //  Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}



