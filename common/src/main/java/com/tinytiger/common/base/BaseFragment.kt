package com.tinytiger.common.base;

import android.os.Bundle
import android.os.Handler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tinytiger.common.R
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils

import com.tinytiger.common.widget.LoadingUtils
import com.umeng.analytics.MobclickAgent

import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/10/28 0028 下午 1:50
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc Fragment基类
 */
abstract class BaseFragment : Fragment() {

    var TAG = ""
    protected var context: FragmentActivity? = null

    var page = 1
    protected var mHandler: Handler = Handler()

    protected var sysTime = 0.toLong()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = activity
        initView()

    }

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化 View
     */
    abstract fun initView()


    /**
     * 加载数据,需要页面做处理
     */
    abstract fun start()


    protected fun showToast(content: String) {
        ToastUtils.toshort(activity, content)
    }

    override fun onResume() {
        super.onResume()
        if (TAG.isNotEmpty()){
            MobclickAgent.onPageStart(TAG)
        }
    }

    override fun onPause() {
        super.onPause()
        LoadingUtils.getInstance().dismiss()
        if (TAG.isNotEmpty()){
            MobclickAgent.onPageEnd(TAG);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        mHandler.removeCallbacksAndMessages(null)
        mErrorView = null
    }

    protected fun showProgress() {
        LoadingUtils.getInstance().show(activity)
    }

    protected fun hideProgress() {
        LoadingUtils.getInstance().dismiss()
    }

    /**
     * 获取登录状态
     *
     * @return true 已登录 false 未登录
     */
    protected fun isLogin(): Boolean {
        return SpUtils.getString(R.string.token, "").isNotEmpty()
    }

    /**
     * 获取登录状态,未登录跳登录页面
     *
     * @return true 已登录 false 未登录
     */
    protected fun isLoginStart(): Boolean {
        if (!isLogin()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return false
        } else {
            return true
        }
    }

    protected var mErrorView: View? = null
    /**
     * 无网络显示View
     *
     */
    fun getErrorLayout(): View {
        val view = View.inflate(activity, R.layout.view_no_network, null)
        view.findViewById<View>(R.id.no_network_view_tv).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }

            start()
        }
        return view
    }

}
