package com.tinytiger.common.base


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tinytiger.common.R
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.LoadingUtils
import org.greenrobot.eventbus.EventBus


/**
 * @author xuhao
 * created: 2017/10/25
 * desc:BaseActivity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 分页页码
     */
    protected var page = 1
    protected var mHandler: Handler = Handler()
    protected var sysTime = 0.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        setContentView(layoutId())
        setStatusBar()
        AppManager.getAppManager().addActivity(this)
        initView()
    }


    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
        AppManager.getAppManager().finishActivity(this)
    }


    override fun onPause() {
        super.onPause()
        LoadingUtils.getInstance().dismiss()
    }

    /**
     *  加载布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()


    protected fun showToast(content: String) {
        ToastUtils.toshort(this.applicationContext, content)
    }

    protected fun showToast(strId: Int) {
        showToast(getString(strId))
    }


    protected fun showProgress() {
        LoadingUtils.getInstance().show(this)
    }

    protected fun hideProgress() {
        LoadingUtils.getInstance().dismiss()
    }

    /**
     * android6.0以上可以设置
     * true  状态栏文字和图标为暗色
     * false 状态栏采用了白色系
     */
    protected var useStatusBarColor = true

    protected fun setStatusBar() {
        //android6.0以后可以对状态栏文字颜色和图标进行修改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (useStatusBarColor) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        )
            }
        }
    }

    /**
     * 设置状态栏透明
     * !!!!记得在setContentView之前
     */
    protected fun setWindowFeature() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.BLACK
    }


    /**
     * 获取登录状态
     *
     * @return true 已登录 false 未登录
     */
    protected fun isLogin(): Boolean {
        return !SpUtils.getString(R.string.token, "").isEmpty()
    }

    /**
     * 获取登录状态,未登录跳登录页面
     *
     * @return true 已登录 false 未登录
     */
    protected fun isLoginStart(): Boolean {
        return if (!isLogin()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            false
        } else {
            true
        }
    }

    protected var mErrorView: View? = null

    /**
     * 无网络显示View
     */
    fun getErrorLayout(): View {
        val view = View.inflate(this, R.layout.view_no_network, null)
        view.findViewById<View>(R.id.no_network_view_tv).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            start()
        }
        return view
    }


    /**
     * 无网络显示View
     */
    fun getErrorLayout(is_cancel: Boolean): View {
        val view = View.inflate(this, R.layout.view_no_network, null)
        view.findViewById<View>(R.id.no_network_view_tv).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            start()
        }

        val ivBack = view.findViewById<View>(R.id.ivBack)
        ivBack.visibility = if (is_cancel) View.VISIBLE else View.GONE
        ivBack.setOnClickListener {
            finish()
        }
        return view
    }
}


