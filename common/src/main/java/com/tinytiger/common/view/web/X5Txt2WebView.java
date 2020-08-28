package com.tinytiger.common.view.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhw_luke
 * @date 2019/10/24 0024 上午 10:10
 * @Copyright 小虎互联科技
 * @doc 富文本加载,需要图片适应100%
 * @since 5.0.0
 */
public class X5Txt2WebView extends WebView {


    public X5Txt2WebView(Context arg0) {
        super(arg0);
        setBackgroundColor(85621);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5Txt2WebView(final Context context, AttributeSet attr) {
        super(context, attr);
        initX5WebGoodSetting(context);
    }

    //一般用以下配置即可让View获得相对完整的功能
    public void initX5WebGoodSetting(Context context) {
        com.tencent.smtt.sdk.WebSettings webSetting = getSettings();
        webSetting.setSavePassword(true);
        // 保存表单数据
        webSetting.setSaveFormData(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setAllowFileAccess(true);
        // 缩放至屏幕的大小
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
      //  webSetting.setSupportZoom(true);
       // webSetting.setBuiltInZoomControls(true);


        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);

        webSetting.setCacheMode(com.tencent.smtt.sdk.WebSettings.LOAD_NO_CACHE);
        //webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setAppCachePath(context.getDir("appwebcache", 0).getPath());
        //webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().sync();


       /* mEditor.getSettings().setUseWideViewPort(true);
        mEditor.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mEditor.getSettings().setSupportZoom(true); //支持缩放
        mEditor.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。
        mEditor.getSettings().setDisplayZoomControls(false); //隐藏原生的缩放控件`*/
    }

    public void loadDataWithBaseURL(String data) {
      //  this.loadDataWithBaseURL("about:blank", getHtmlData(data), "text/html", "utf-8", null);
        this.loadDataWithBaseURL("about:blank", data, "text/html", "utf-8", null);

        // listImgSrc.addAll(getImgSrc(data));
    }





}
