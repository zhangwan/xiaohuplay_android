package com.tinytiger.titi.wxapi;

import android.app.Activity;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;

/**
 * 微信分享管理器
 */
public class WeiXinManager {
    private static WeiXinManager instance;
    private ArrayList<WeiXinShareListener> mListeners = new ArrayList<>();
    private IWXAPI mWxApi;
    public static String appid = "wx6b19d2de3a714d12";
    public static String secret = "2f9d69f604f3568e18c0c610c9d51f23";

    private WeiXinManager(Activity context){
        registToWX(context);
    }


    private void registToWX(Activity context) {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(context, appid, true);
        // 将该app注册到微信
        mWxApi.registerApp(appid);
    }

    public IWXAPI getApi(){
        return mWxApi;
    }

    /**
     * 初始化
     */
    public static WeiXinManager init(Activity context){
        if (instance == null){
            instance = new WeiXinManager(context);
        }
        return instance;
    }

    /**
     *  处理分享意图
     * @param intent WXEntryActivity的意图
     */
    public void handleShareIntent(Intent intent){
        for (WeiXinShareListener weiXinShareListener:
                mListeners) {
            weiXinShareListener.onEntry(intent);
        }
    }

    /**
     * 微信分享
     */
    public interface WeiXinShareListener{
        void onEntry(Intent intent);
    }

    public void addWeiXinShareListener(WeiXinShareListener weiXinShareListener){
        mListeners.add(weiXinShareListener);
    }
    public void removeWeiXinShareListener(WeiXinShareListener weiXinShareListener){
        mListeners.remove(weiXinShareListener);
    }
}
