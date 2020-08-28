package com.tinytiger.titi.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.tinytiger.common.utils.AppManager;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.titi.R;
import com.tinytiger.titi.ui.login.LoginActivity;


/**
 * @author luke
 * @date 2018/9/12 17:23
 * @doc webview H5回调
 */
public class WebDialogInterface {
    private DialogFragment mDialogFragment;
    private Activity mActivity;

    public WebDialogInterface(DialogFragment fragment) {
        mDialogFragment = fragment;
        mActivity = mDialogFragment.getActivity();
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
     *
     * @return 无
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
     * @param url
     */
    @JavascriptInterface
    public void callNewWeb(String url) {
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        mActivity.startActivity(intent);
    }

    /**
     * 关闭当前页
     */
    @JavascriptInterface
    public void callFinish() {
         AppManager.getAppManager().finishActivity();
    }

    /**
     * 点击返回
     */
    @JavascriptInterface
    public void callBack() {
        mDialogFragment.dismiss();
    }

    /**
     * 点击返回完成
     * */
    @JavascriptInterface
    public void callComplete() {
    }


    /**
     * 评分埋点
     * */
    @JavascriptInterface
    public void callGameScore(String data) {

        JSONObject obj = JSON.parseObject(data);
        String game_id = obj.getString("game_id");
        String game_score_fraction = obj.getString("game_score_fraction");
        if (listener!=null){
            listener.onGameScore(game_id,game_score_fraction);
            listener.onComplete();
        }
    }


    /**
     * 打开系统相册
     */
    @JavascriptInterface
    public void callAlbum() {
        if (listener!=null){
            listener.onStartAlbum();
        }
    }

    public onWebDialogListener listener;

    public interface onWebDialogListener {

        void onStartAlbum();
        void onComplete();
        void onGameScore(String game_id,String score);
    }


}