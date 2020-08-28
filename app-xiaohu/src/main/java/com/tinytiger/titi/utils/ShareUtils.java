package com.tinytiger.titi.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.utils.AppManager;
import com.tinytiger.common.utils.AppStoreUtils;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.common.utils.toast.ToastUtils;
import com.tinytiger.titi.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

/**
 * Author     luke
 * DATE       2017/07/16
 * Des	      友盟分享工具类
 */
public class ShareUtils {

    /**
     * 分享链接
     */
    public static void shareWeb(final String className, String WebUrl, String title, String description, String imageUrl, int imageID, SHARE_MEDIA platform) {
        Activity activity= AppManager.getAppManager().currentActivity();
        if (platform== SHARE_MEDIA.QQ){
            if (!AppStoreUtils.startQQ(activity)){
                return;
            }
        }else if (platform== SHARE_MEDIA.QZONE){
            if (!AppStoreUtils.startQQ(activity)){
                return;
            }
        }else {
            if (!AppStoreUtils.startWX(activity)){
                return;
            }
        }

        if (WebUrl.contains("?")) {
            WebUrl = WebUrl + "&appversion=" +  SpUtils.getString(R.string.version,"");
        } else {
            WebUrl = WebUrl + "?appversion=" + SpUtils.getString(R.string.version,"");
        }

        UMWeb web = new UMWeb(WebUrl);
        //连接地址
        web.setTitle(title);
        //标题
        web.setDescription(description);
        //描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, imageID));
            //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));
            //网络缩略图
        }
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        int type = 1;
                        if (SHARE_MEDIA.WEIXIN.equals(share_media)) {
                            type = 1;
                        } else if (SHARE_MEDIA.WEIXIN_CIRCLE.equals(share_media)) {
                            type = 3;
                        } else {
                            type = 4;
                        }
                       // EventBus.getDefault().post(new ShareEvent(className, type, "Callback"));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtils.show(BaseApp.getContext(),"分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                       // ToastUtils.show(BaseApp.getContext(),"分享失败");
                    }
                })
                .share();

    }


    /**
     * 小程序分享
     */
    public static void shareWxapp(final Activity activity, String WebUrl, String title, String description, String imageUrl, String path, SHARE_MEDIA platform) {
        com.umeng.socialize.media.UMMin umMin = new com.umeng.socialize.media.UMMin(WebUrl);
        umMin.setThumb(new UMImage(activity, imageUrl));
        umMin.setTitle(title);
        umMin.setDescription(description);
        umMin.setPath(path);
        umMin.setUserName("gh_de04bb7b670f");
        new ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(platform)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                       // EventBus.getDefault().post(new MeInfoEvent(21, 2));
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                    }
                })
                .share();
    }


    /**
     * 1.微信网页
     * 2.微信小程序
     * 3.朋友圈
     * 4.QQ
     */

    /**
     * app代开小程序
     *
     * @param path pages/index/index
     */
    public static void shareWxapp(String path) {
        // 填应用AppId，APP在开放平台注册的id
        String appId = "wx6b19d2de3a714d12";
        IWXAPI api = WXAPIFactory.createWXAPI(BaseApp.getContext(), appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        // 填小程序原始id
        req.userName = "gh_6205bd32bb0b";
        //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.path = path;
        // 可选打开 开发版，体验版和正式版
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);
    }

    /**
     * app代开小程序
     *@param gh 小程序原始id
     * @param path pages/index/index
     */
    public static void shareWxapp(String gh,String path) {
        // 填应用AppId，APP在开放平台注册的id
        String appId = "wx6b19d2de3a714d12";
        IWXAPI api = WXAPIFactory.createWXAPI(BaseApp.getContext(), appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        // 填小程序原始id
        req.userName = gh;
        //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.path = path;
        // 可选打开 开发版，体验版和正式版
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);
    }

    /**
     * 获取所有未读消息
     */
    public static void getIm() {
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

            @Override
            public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                if (code != ResponseCode.RES_SUCCESS || recents == null) {
                    return;
                }
                int unreadNum = 0;
                for (RecentContact i : recents) {
                    unreadNum += i.getUnreadCount();
                }
                SpUtils.saveSP(R.string.im_size,unreadNum);
            }
        });
    }

    /**
     * 分享图片
     */
    public static void shareImage(Bitmap bitmap,SHARE_MEDIA platform) {
        Activity activity= AppManager.getAppManager().currentActivity();
        if (platform== SHARE_MEDIA.QQ){
            if (!AppStoreUtils.startQQ(activity)){
                return;
            }
        }else if (platform== SHARE_MEDIA.QZONE){
            if (!AppStoreUtils.startQQ(activity)){
                return;
            }
        }else {
            if (!AppStoreUtils.startWX(activity)){
                return;
            }
        }

        UMImage image = new UMImage(activity, bitmap);//bitmap文件
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(image)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        int type = 1;
                        if (SHARE_MEDIA.WEIXIN.equals(share_media)) {
                            type = 1;
                        } else if (SHARE_MEDIA.WEIXIN_CIRCLE.equals(share_media)) {
                            type = 3;
                        } else {
                            type = 4;
                        }
                        // EventBus.getDefault().post(new ShareEvent(className, type, "Callback"));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtils.show(BaseApp.getContext(),"分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        // ToastUtils.show(BaseApp.getContext(),"分享失败");
                    }
                })
                .share();

    }


}
