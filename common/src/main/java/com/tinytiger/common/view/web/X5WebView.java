package com.tinytiger.common.view.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tinytiger.common.view.title.TitleView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 *
 * @author zhw_luke
 * @date 2019/10/24 0024 上午 10:10
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc tbs内核
 */
public class X5WebView extends WebView {

    private Context context;
    public OnWebPageChangeListener onWebPageChangeListener;
    public String[] adLinks;
    public String homeLinkUrl;
    OnShouldInterCeptUrlListener interCeptUrlListener;


    public interface OnShouldInterCeptUrlListener {
        WebResourceResponse onShouldInterCeptUrl(WebView webView, String url);
    }

    public X5WebView(Context arg0) {
        super(arg0);

        setBackgroundColor(85621);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(final Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initDefaultSetting();
    }


    public void initDefaultSetting() {
        initX5WebGoodSetting();
        setWebViewClient(client);
        setWebChromeClient(chromeClient);
        getView().setClickable(true);
    }

    public void setLayerType(){
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            if (urlCanLoad(url.toLowerCase())) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            if (onWebPageChangeListener != null) {
                onWebPageChangeListener.onPageStart(webView, s);
            }
            super.onPageStarted(webView, s, bitmap);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            if (onWebPageChangeListener != null) {
                onWebPageChangeListener.onPageFinished(webView, s);
            }
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError sslError) {
            handler.proceed();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return shouldInterceptRequestInX5(view, url, super.shouldInterceptRequest(view, url));
        }
    };


    public WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView webView, String s) {
            super.onReceivedTitle(webView, s);
            if (onWebPageChangeListener != null) {
                onWebPageChangeListener.onReceivedTitle(webView, s);
            }
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if ( i < 100 && i > 10) {

            } else {

            }
        }
    };


    /**
     * 处理拦截广告
     *
     * @param view
     * @param url
     * @param webResourceResponse
     * @return
     */
    public WebResourceResponse shouldInterceptRequestInX5(WebView view, String url, WebResourceResponse webResourceResponse) {
        url = url.toLowerCase();
        if (!TextUtils.isEmpty(homeLinkUrl) && !url.contains(homeLinkUrl)) {
            if (!ADFilterTool.hasAd(url, adLinks)) {
                if (interCeptUrlListener != null) {
                    return interCeptUrlListener.onShouldInterCeptUrl(view, url);
                } else {
                    return webResourceResponse;
                }
            } else {
                return new WebResourceResponse(null, null, null);
            }
        } else {
            if (interCeptUrlListener != null) {
                return interCeptUrlListener.onShouldInterCeptUrl(view, url);
            } else {
                return webResourceResponse;
            }
        }
    }


    public interface OnWebPageChangeListener {
        /**
         * 网页标题
         * @param webView
         * @param s
         */
        void onReceivedTitle(WebView webView, String s);

        /**
         * 开始加载网页
         * @param webView
         * @param s
         */
        void onPageStart(WebView webView, String s);

        /**
         * 网页加载完成
         * @param webView
         * @param s
         */
        void onPageFinished(WebView webView, String s);
    }

    /**
     * 列举正常情况下能正常加载的网页url
     *
     * @param url
     * @return
     */
    public boolean urlCanLoad(String url) {
        return url.startsWith("http://") || url.startsWith("https://") ||
                url.startsWith("ftp://") || url.startsWith("file://");
    }


    //一般用以下配置即可让View获得相对完整的功能
    public void initX5WebGoodSetting() {
        WebSettings webSetting = getSettings();
        webSetting.setSavePassword(true);
        // 保存表单数据
        webSetting.setSaveFormData(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setAllowFileAccess(true);
        // 缩放至屏幕的大小
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);

        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
       // webSetting.setAppCachePath(context.getDir("appwebcache", 0).getPath());
        //webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().sync();

    }



    /**
     * 广告拦截过滤
     */
    public static class ADFilterTool {
        public static boolean hasAd(String url, String[] adUrls) {
            if (adUrls == null || adUrls.length == 0) {
                return false;
            }
            for (String adUrl : adUrls) {
                if (url.contains(adUrl)) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * 添加js调用安卓方法声明
     * @param mWebInterface
     */
    public void setWebInterface(Object mWebInterface){
        addJavascriptInterface(mWebInterface, "titi_js");
    }


    @Override
    public void loadUrl(String s) {
        super.loadUrl(s);
    }

    @Override
    public void goBack() {
        super.goBack();
    }

    @Override
    public boolean canGoBack() {
        return super.canGoBack();
    }


    private TitleView title_view;

    public void setTitleView(TitleView title_view) {
        this.title_view = title_view;
    }
}
