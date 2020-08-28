package com.tinytiger.common.basis

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tinytiger.common.R
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.LoadingUtils
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * BaseActivity
 *
 * @author:zwy
 * @date:2020/6/9
 */
abstract class BasisFragment<T : BasisPresenter<*>> : Fragment(), BasisView {

    var basePresenter: T? = null
    protected var context: FragmentActivity? = null
    var TAG = ""

    var page=1

    override fun onDestroy() {
        super.onDestroy()

        if (basePresenter != null) {
            basePresenter?.detachView()
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun getEntry(): Class<T>? {
        val type = javaClass.genericSuperclass
        var result: Class<T>? = null
        if (type is ParameterizedType) {
            result = type.actualTypeArguments[0] as Class<T>
        }
        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = activity
        initView()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
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

    override fun onResume() {
        super.onResume()
        refresh()
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
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            refresh()
        }
    }

    fun initPresenter() {
        var presenterClass = getEntry()
        basePresenter = presenterClass?.newInstance()
        basePresenter?.attachView(this)
    }


    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showToast(msg: String) {
        ToastUtils.toshort(activity, msg)
    }


    override fun getMyContext(): Context {
        return context!!
    }

    override fun refresh() {

    }

    override fun showToast(msg: Int) {
        showToast(getString(msg))
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

}