package com.tinytiger.titi.ui.main


import android.os.Bundle

import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.utils.preference.SpUtils

import com.tinytiger.titi.R
import com.tinytiger.titi.ui.web.WebInterface
import kotlinx.android.synthetic.main.activity_fr_web.*


/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 3:56
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 工具页
 */
class ToolFragment : BaseFragment() {

    companion object {
        fun getInstance(): ToolFragment {
            val fragment = ToolFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_fr_web


    /**
     * 初始化 View
     */
    override fun initView() {
        x5webView.setWebInterface(WebInterface(activity))
        val url = SpUtils.getString(R.string.tool_config_url, "")
        x5webView.loadUrl(url)
    }

    override fun start() {

    }


}
