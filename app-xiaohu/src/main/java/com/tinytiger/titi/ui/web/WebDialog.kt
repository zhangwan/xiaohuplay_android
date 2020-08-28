package com.tinytiger.titi.ui.web


import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.dialog.TextDialog

import com.tinytiger.common.widget.dialog.base.BaseDialog
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.dialog_web.*


/**
 *
 * @author zhw_luke
 * @date 2020/1/6 0006 上午 10:58
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc dialogH5展示页
 */
class WebDialog : BaseDialog(){


    private var url = "https://h5.tinytiger.cn/web_app/new_appAgreement/index.html"

    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): WebDialog {
            val dialog = WebDialog()
            dialog.setFragmentManager(manager)
            dialog.isCancelable = false
            return dialog
        }
    }

    override fun getLayoutRes(): Int = R.layout.dialog_web

    override fun bindView(v: View?) {
        tv_cancel.setOnClickListener {
            dismiss()
            mDismissListener?.click()
        }
        tv_sure.setOnClickListener {
            SpUtils.saveSP(R.string.start_app, true)
           // SpUtils.saveSP(R.string.start_app1, true)
            dismiss()
        }
        x5webView.webViewClient=object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(url)
               return true
            }
        }
        x5webView.loadUrl(url)
    }


    fun setFragmentManager(manager: FragmentManager): WebDialog {
        mFragmentManager = manager
        return this
    }

    fun show(): WebDialog {
        show(mFragmentManager)
        return this
    }
    fun setDismissListener(listener: ViewListener): WebDialog {
        this.mDismissListener = listener
        return this
    }

    private var mDismissListener: ViewListener? = null

    interface ViewListener {
        fun click()
    }
}
