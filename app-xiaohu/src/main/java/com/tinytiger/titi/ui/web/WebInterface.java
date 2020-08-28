package com.tinytiger.titi.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.tinytiger.common.event.ShareEvent;
import com.tinytiger.common.utils.AppManager;
import com.tinytiger.common.utils.ConstantsUtils;
import com.tinytiger.common.utils.image.DonwloadSaveImg;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.titi.R;
import com.tinytiger.titi.ui.circle.PostActivity;
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity;
import com.tinytiger.titi.ui.circle.post.SendPostActivity;
import com.tinytiger.titi.ui.game.GameLibraryActivity;
import com.tinytiger.titi.ui.game.GameReviewsActivity;
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity;
import com.tinytiger.titi.ui.game.info.GameDetailActivity;
import com.tinytiger.titi.ui.game.wiki.GameWikiDetailActivity;
import com.tinytiger.titi.ui.home2.AmwayActivity;
import com.tinytiger.titi.ui.login.LoginActivity;
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity;
import com.tinytiger.titi.ui.mine.other.FeedbackSendActivity;
import com.tinytiger.titi.ui.news.NewsDetailActivity;
import com.tinytiger.titi.ui.video.VideoDetailActivity;
import com.tinytiger.titi.utils.MarketUtil;
import com.xwdz.download.core.DownloadEntry;
import com.xwdz.download.core.QuietDownloader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * @author luke
 * @date 2018/9/12 17:23
 * @doc webview H5回调
 */
public class WebInterface {

    private Activity mActivity;

    public WebInterface(Activity webActivity) {
        mActivity = webActivity;
    }

    public WebListener mWebListener = null;

    interface WebListener {
        /**
         * Title
         *
         * @param titleType true显示 false隐藏
         */
        void showTitle(Boolean titleType);

        void onStartAlbum();
    }


    /**
     * 获取用户数据
     *
     * @return 用户id user_id
     * 用户凭证 token
     * APP版本 appversion
     */
    @JavascriptInterface
    public String callUserInfo() {
        JSONObject jb = new JSONObject();
        try {
            jb.put("user_id", SpUtils.getString(R.string.user_id, ""));
            jb.put("token", SpUtils.getString(R.string.token, ""));
            jb.put("appversion", SpUtils.getString(R.string.version, ""));
        } catch (Exception E) {

        }
        return jb.toString();
    }


    /**
     * app跳转登录
     */
    @JavascriptInterface
    public void callLogin() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }


    /**
     * 打开新的web页面
     *
     * @param url 页面地址
     */
    @JavascriptInterface
    public void callNewWeb(String url) {
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        mActivity.startActivity(intent);
    }


    /**
     * 页面title栏
     *
     * @param data 状态 {"titleType":true显示 false隐藏}
     */
    @JavascriptInterface
    public void callShowTitle(String data) {
        JSONObject obj = JSON.parseObject(data);
        boolean titleType = obj.getBoolean("titleType");
        if (mWebListener != null) {
            mWebListener.showTitle(titleType);
        }
    }

    /**
     * 关闭当前页
     */
    @JavascriptInterface
    public void callFinish() {
        AppManager.getAppManager().finishActivity();
    }

    /**
     * 保存图片到相册
     */
    @JavascriptInterface
    public void callSaveImg(String url) {
        if (mActivity != null) {
            DonwloadSaveImg.donwloadImg(mActivity, url);
        }
    }

    /**
     * 打开系统相册
     */
    @JavascriptInterface
    public void callAlbum() {
        if (mWebListener != null) {
            mWebListener.onStartAlbum();
        }
    }


    /**
     * 打开微信扫一扫
     */
    @JavascriptInterface
    public void callWXScan() {
        if (mActivity != null) {
            Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            mActivity.startActivity(intent);
        }
    }


    /**
     * 打开意见反馈
     */
    @JavascriptInterface
    public void callFeedback() {
        Intent intent = new Intent(mActivity, FeedbackSendActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }

    /**
     * 获取渠道安装包信息
     *
     * @param data 包标记 {"path_url":下载url,"package_name":apk包名}
     * @return true 返回PackageInfo apk安装信息 false 返回空对象
     */
    @JavascriptInterface
    public String callAPKPackger(String data) {
        JSONObject obj = JSON.parseObject(data);
        String pathUrl = obj.getString("path_url");

        List<DownloadEntry> list = QuietDownloader.queryAll();
        boolean typeDownload = false;
        //判断是否下载安装包
        for (DownloadEntry i : list) {
            if (i.id.equals(pathUrl) && i.status == DownloadEntry.Status.COMPLETED) {
                typeDownload = true;
                break;
            }
        }
        if (typeDownload) {
            String packageName = obj.getString("package_name");
            PackageInfo apk = MarketUtil.getAppInstalled(mActivity, packageName);
            if (apk != null) {
                return JSON.toJSONString(apk);
            }
        }
        return new JSONObject().toString();
    }


    /**
     * 跳转功能页面
     *
     * @param data {"startType":打开页面类型
     *             1:资讯文章,2资讯视频,3个人主页,4游戏详情,5:安利文,6:百科详情,7圈子,8帖子详情
     *             "key":参数}
     */
    @JavascriptInterface
    public void callActivityPage(String data) {
        Logger.d(data);
        JSONObject obj = JSON.parseObject(data);
        String startType = obj.getString("startType");
        String key = obj.getString("key");
        Intent intent = null;
        if (startType.equals("1")) {
            intent = new Intent(mActivity, NewsDetailActivity.class);
            intent.putExtra("content_id", key);

        } else if (startType.equals("2")) {
            intent = new Intent(mActivity, VideoDetailActivity.class);
            intent.putExtra("content_id", key);
            intent.putExtra("content_url", "");
        } else if (startType.equals("3")) {
            intent = new Intent(mActivity, HomepageActivity.class);
            intent.putExtra("user_id", key);
        } else if (startType.equals("4")) {
            intent = new Intent(mActivity, GameDetailActivity.class);
            intent.putExtra("extra_game_id", key);

        } else if (startType.equals("5")) {
            intent = new Intent(mActivity, GameReviewsActivity.class);
            intent.putExtra("game_id", key);
            intent.putExtra("assess_id", obj.getString("key2"));

        } else if (startType.equals("6")) {
            intent = new Intent(mActivity, GameWikiDetailActivity.class);
            intent.putExtra("extra_wiki_id", key);
        } else if (startType.equals("7")) {
            intent = new Intent(mActivity, CirclesDetailsActivity.class);
            intent.putExtra("circleId", key);
            intent.putExtra("modularId", "");
            intent.putExtra("pageIndex", ConstantsUtils.Page.PAGE_CIRCLE);
            intent.putExtra("gameId", "");
        } else if (startType.equals("8")) {
            //发布帖子页面
            intent = new Intent(mActivity, SendPostActivity.class);
            if (key!=null){
                intent.putExtra("circleId", key);
                intent.putExtra("circleName", obj.getString("key2"));
            }
        } else if (startType.equals("9")) {
            intent = new Intent(mActivity, PostActivity.class);
            intent.putExtra("postId", key);
        } else if (startType.equals("10")) {
            //圈子百科
            intent = new Intent(mActivity, CirclesDetailsActivity.class);
            if (key!=null){
                intent.putExtra("gameId", key);

            }
            intent.putExtra("modularId", "");
            intent.putExtra("pageIndex", ConstantsUtils.Page.PAGE_WIKI);
            String key2 = obj.getString("key2");
            if (key2!=null){
                intent.putExtra("circleId", key2);
            }
        } else if (startType.equals("11")) {
            //11-游戏分类二级分类详情
            intent = new Intent(mActivity, GameCategoryDetailActivity.class);
            intent.putExtra("extra_cate_id", obj.getInteger("key"));
            intent.putExtra("extra_cate_name", obj.getString("key2"));

        } else if (startType.equals("12")) {
            intent = new Intent(mActivity, FeedbackSendActivity.class);
        } else if (startType.equals("13")) {
            intent = new Intent(mActivity, GameLibraryActivity.class);
        } else if (startType.equals("14")) {
            intent = new Intent(mActivity, AmwayActivity.class);
        } else if (startType.equals("16")) {
            //圈子资讯
            intent = new Intent(mActivity, CirclesDetailsActivity.class);
            if (key!=null){
                intent.putExtra("gameId", key);
            }
            intent.putExtra("modularId", "");
            intent.putExtra("pageIndex", ConstantsUtils.Page.PAGE_NEWS);
            String key2 = obj.getString("key2");
            if (key2!=null){
                intent.putExtra("circleId", key2);
            }
        }

       if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        }
    }


    /**
     * 分享數據
     *
     * @param share{"type":分享类型 0:微信 1:朋友圈 2:QQ 3:QQ空间,"share_url":分享链接,"share_title":分享标题,"share_desc":分享说明,"share_image":分享图片}
     */
    @JavascriptInterface
    public void callShare(String share) {
        // {"type":分享类型 0:微信 1:朋友圈 2:QQ 3:QQ空间,"share_url":分享链接,"share_title":分享标题,"share_desc":分享说明,"share_image":分享图片}

        JSONObject obj = JSON.parseObject(share);
        int type = obj.getInteger("type");
        String share_url = obj.getString("share_url");
        String share_title = obj.getString("share_title");
        String share_desc = obj.getString("share_desc");
        String share_image = obj.getString("share_image");

        String share_type = "WEIXIN";
        switch (type) {
            case 1:
                share_type = "WEIXIN_CIRCLE";
                break;
            case 2:
                share_type = "QQ";
                break;
            case 3:
                share_type = "QQZONE";
                break;
            case 4:
                share_type = "Copy";
                break;
            default:
                break;
        }

        EventBus.getDefault().post(new ShareEvent("share", share_url, share_title, share_desc, share_image, share_type));
    }

}