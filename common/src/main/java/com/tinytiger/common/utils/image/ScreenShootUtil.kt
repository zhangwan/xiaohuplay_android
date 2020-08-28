package com.tinytiger.common.utils.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.tencent.smtt.sdk.WebView
import com.tinytiger.common.view.web.X5TxtWebView

/**
 *
 * @author zhw_luke
 * @date 2020/7/23 0023 下午 4:23
 * @Copyright 小虎互联科技
 * @since 3.4.0
 * @doc view 转  Bitmap
 *
 */
object ScreenShootUtil {

    /**
     * webview转bitmap
     */
    fun screenshot(webView: X5TxtWebView): Bitmap?{
        webView.measure(View.MeasureSpec.makeMeasureSpec(
            View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)

        try {
            val bitmap = Bitmap.createBitmap(webView.width, webView.measuredHeight,
                Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(-0x1)
            webView.draw(canvas)
            return bitmap
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun getwebview(webView: WebView){
        val bitmap: Bitmap = webView.getDrawingCache()

    }

    /**
     * 获取scrollview的截屏
     */
    fun shotScrollView(scrollView: ScrollView): Bitmap? {
        var h = 0
        var bitmap: Bitmap? = null
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
        bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }
}