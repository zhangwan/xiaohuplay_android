package com.tinytiger.common.base


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.tinytiger.common.R
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.LoadingUtils
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/5/21 0021 下午 2:46
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 左滑返回
 */
abstract class BaseBackActivity : AppCompatActivity(), BGASwipeBackHelper.Delegate {

    /**
     * 分页页码
     */
    protected var page = 1
    protected var mHandler: Handler = Handler()
    protected var sysTime = 0.toLong()
    /**
     * 是否启用友盟埋点
     */
    protected var umengType=false

    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBackFinish()
        super.onCreate(savedInstanceState)
        initData()
        setContentView(layoutId())
        setStatusBar()
        AppManager.getAppManager().addActivity(this)
        initView()
    }
    protected var mSwipeBackHelper: BGASwipeBackHelper? = null
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
// 下面几项可以不配置，这里只是为了讲述接口用法。
// 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper?.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper?.setIsOnlyTrackingLeftEdge(false)
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper?.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
      //  mSwipeBackHelper?.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper?.setIsNeedShowShadow(false)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper?.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper?.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper?.setIsNavigationBarOverlap(false)
    }

    override fun onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper!!.isSliding) {
            return
        }
        mSwipeBackHelper!!.backward()
    }
    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper!!.swipeBackward()
    }

    override fun onResume() {
        super.onResume()
        if (umengType){
            MobclickAgent.onResume(this)
        }
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
        if (umengType){
            MobclickAgent.onPause(this)
        }
    }

    /**
     * 设置是否可滑动返回
     */
    fun setSwipeBackEnable(swipeBackEnable:Boolean){
        mSwipeBackHelper?.setSwipeBackEnable(swipeBackEnable)
    }

    /**
     * 是否侧边栏滑动返回
     * 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
     */
    fun setIsOnlyTrackingLeftEdge( isOnlyTrackingLeftEdge:Boolean){
        mSwipeBackHelper?.setIsOnlyTrackingLeftEdge(isOnlyTrackingLeftEdge)
    }


    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    override fun onSwipeBackLayoutSlide(slideOffset: Float) {}

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    override fun onSwipeBackLayoutCancel() {}

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
        ToastUtils.toshort(this, content)
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


