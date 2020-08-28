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
 * @doc 富文本加载, 需要图片适应100%
 * @since 5.0.0
 */
public class X5TxtWebView extends WebView implements View.OnTouchListener {


    public X5TxtWebView(Context arg0) {
        super(arg0);
        setBackgroundColor(85621);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5TxtWebView(final Context context, AttributeSet attr) {
        super(context, attr);
        initX5WebGoodSetting(context);

        this.addJavascriptInterface(new InJavaScriptLocalObj(), "imageListener");
        setWebViewClient(client);
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
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().sync();
    }


    private WebViewClient client = new WebViewClient() {

        @Override
        public void onPageFinished(final WebView webView, String s) {
            setImageClickListner();
            super.onPageFinished(webView, s);
            if (onWebPageChangeListener != null) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        //重设view高度,防止显示不全
                        webView.measure(0, 0);
                        int measuredHeight = webView.getMeasuredHeight();

                        if (measuredHeight > 300) {
                            ViewGroup.LayoutParams lp = webView.getLayoutParams();
                            lp.height = measuredHeight;
                            webView.setLayoutParams(lp);
                        }

                        if (onWebPageChangeListener != null) {
                            onWebPageChangeListener.onPageFinished(measuredHeight);
                        }
                    }
                });
            }
        }
    };

    public void getHeightSize() {
        this.measure(0, 0);
        int measuredHeight = this.getMeasuredHeight();
        if (measuredHeight > 300) {
            ViewGroup.LayoutParams lp = this.getLayoutParams();
            lp.height = measuredHeight;
            this.setLayoutParams(lp);

            if (onWebPageChangeListener != null) {
                onWebPageChangeListener.onPageFinished(measuredHeight);
            }
        }
    }


    public void loadDataWithBaseURL(String data) {
        this.loadDataWithBaseURL("about:blank", getHtmlData(data), "text/html", "utf-8", null);
        getImgSrc(data);
    }


    public void loadDataURL(String data) {
        this.loadUrl(data);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{width:100%; height:auto;}*{margin:0px;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    /**
     * 图片点击回调
     *
     * @param imageClickListener 回调
     * @return RichTextConfigBuild
     */
    public X5TxtWebView imageClick(OnImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
        return this;
    }

    private OnImageClickListener imageClickListener;


    public interface OnImageClickListener {
        /**
         * 图片被点击后的回调方法
         *
         * @param imageUrls 本篇富文本内容里的全部图片
         * @param position  点击处图片在imageUrls中的位置
         */
        void imageClicked(List<String> imageUrls, int position);
    }

    private List<String> listImgSrc = new ArrayList<>();

    private class InJavaScriptLocalObj {

        @JavascriptInterface
        public void startShowImageActivity(String url, String[] imgs) {
            int position = 0;
            listImgSrc.clear();
            for (int i = 0; i < imgs.length; i++) {
                if (imgs[i].equals(url)) {
                    position = i;
                }
                listImgSrc.add(imgs[i]);
            }
            imageClickListener.imageClicked(listImgSrc, position);
        }
    }

    private void getImgSrc(String htmlStr) {
        if (htmlStr == null) {
            return;
        }
        if (htmlStr.indexOf("<") < 0) {
            return;
        }
        Document document = Jsoup.parse(htmlStr);
        Elements images = document.getElementsByTag("img");
        for (Element image : images) {
            listImgSrc.add(image.attr("src"));
        }
    }

    /**
     * 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
     */
    private void setImageClickListner() {
        //延迟300ms加载js，防止两次调用loadUrl时第二次无效
        postDelayed(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        " var array=new Array(); " +
                        " for(var j=0;j<objs.length;j++){ array[j]=objs[j].src; }" +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.imageListener.startShowImageActivity(this.src,array);  " +
                        "    }  " +
                        "}" +
                        "})()");
            }
        }, 300);
    }


    public OnWebPageChangeListener onWebPageChangeListener;

    public interface OnWebPageChangeListener {

        /**
         * 网页加载完成
         */
        void onPageFinished(int measuredHeight);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ((WebView) v).requestDisallowInterceptTouchEvent(true);
        return false;
    }

}
