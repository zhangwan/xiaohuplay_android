package com.tinytiger.titi.ui.web

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import com.alibaba.fastjson.JSONObject
import com.tencent.smtt.sdk.WebView
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.QiNiuUtils
import com.tinytiger.common.utils.image.ImageUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.web.X5WebView
import com.tinytiger.titi.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_web.*

/**
 *
 * @author zhw_luke
 * @date 2019/10/23 0023 上午 10:50
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc web处理页
 */
class WebActivity : BaseActivity() {

    companion object {
        fun actionStart(context: Context, url: String) {
            actionStart(context, url, true)
        }

        fun actionStart(context: Context, url: String, titleType: Boolean) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("titleType", titleType)
            context.startActivity(intent)
        }

    }

    override fun layoutId(): Int = R.layout.activity_web

    private var url = ""
    private var mWebInterface: WebInterface? = null

    override fun initData() {
        url = intent.getStringExtra("url")
        if (url.contains("?")) {
            url = url + "&appversion=" + SpUtils.getString(R.string.version, "")
        } else {
            url = url + "?appversion=" + SpUtils.getString(R.string.version, "")
        }

        if (!SpUtils.getString(R.string.token, "").isEmpty()) {
            url = url + "&token=" + SpUtils.getString(R.string.token,
                "") + "&user_id=" + SpUtils.getString(R.string.user_id, "")
        }
    }


    override fun initView() {
        if (intent.getBooleanExtra("titleType", true)) {
            tvTitle.setBackOnClick { finish() }
        } else {
            tvTitle.visibility = View.GONE
        }

        mWebInterface = WebInterface(this)

        mWebInterface?.mWebListener = object : WebInterface.WebListener {
            override fun showTitle(titleType: Boolean) {
                if (titleType) {
                    tvTitle.visibility = View.VISIBLE
                } else {
                    tvTitle.visibility = View.GONE
                }
            }

            override fun onStartAlbum() {
                startAlbum()
            }
        }

        x5webView.setWebInterface(mWebInterface)

        x5webView.loadUrl(url)

        x5webView.onWebPageChangeListener = object : X5WebView.OnWebPageChangeListener {
            override fun onReceivedTitle(webView: WebView?, s: String) {
                tvTitle.centerTxt = s
            }

            override fun onPageStart(webView: WebView?, s: String?) {

            }

            override fun onPageFinished(webView: WebView?, s: String?) {

            }
        }
    }

    override fun start() {

    }


    override fun onResume() {
        super.onResume()
        if (x5webView != null) {
            x5webView.onResume()
            x5webView?.loadUrl("javascript:onResume()")
        }
    }

    override fun onPause() {
        super.onPause()
        if (x5webView != null) {
            x5webView.onPause()
            x5webView?.loadUrl("javascript:onPause()")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (x5webView != null) {
            x5webView.destroy()
        }
    }

    override fun onBackPressed() {
        /**
         *  部分机型执行此方法会跳转到非主线程执行,导致操作失败
         *  强制方法执行在Ui主线程
         */
        Handler(Looper.getMainLooper()).post {
            if (x5webView.canGoBack()) {
                x5webView.goBack()
            } else {
                finish()
            }
        }
    }

    fun startAlbum() {
        AndPermission.with(this).runtime().permission(Permission.Group.STORAGE)
            .rationale(DefaultRationale()).onGranted {
                startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 9162)
            }.onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                    PermissionSetting().showSettingDialog(this, permissions)
                }
            }.start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 9162 && data != null) {
            val imgPath = ImageUtil.getImageAbsolutePath(this, data.data)
            uploadQiniu(imgPath)
        }
    }


    private fun uploadQiniu(imgPath: String) {
        QiNiuUtils.appQiniu?.uploadManager("web", imgPath, object : QiNiuUtils.OnQiniuListener {
            override fun onResult(result: String, type: Boolean) {
                if (type) {
                    val jb = JSONObject()
                    jb["img"] = result
                    x5webView?.loadUrl("javascript:getImgUrl(${jb.toString()})")
                } else {
                    showToast("上传失败")
                }
                hideProgress()
            }
        })
    }
}
