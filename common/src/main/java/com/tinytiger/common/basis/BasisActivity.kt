package com.tinytiger.common.basis

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.toast.ToastUtils
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * BaseActivity
 *
 * @author:zwy
 * @date:2020/6/5
 */
abstract class BasisActivity<T : BasisPresenter<*>> : AppCompatActivity(), BasisView, BGASwipeBackHelper.Delegate {

    var basePresenter: T? = null
    private var mSwipeBackHelper: BGASwipeBackHelper? = null
    protected var mHandler: Handler = Handler()
    /**
     * android6.0以上可以设置
     * true  状态栏文字和图标为暗色
     * false 状态栏采用了白色系
     */
    protected var useStatusBarColor = true

    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBackFinish()
        super.onCreate(savedInstanceState)
        initPresenter()
        initData()
        setStatusBar()
        setContentView(layoutId())
        AppManager.getAppManager().addActivity(this)
        initView()

    }

    private fun initPresenter() {
        val presenterClass = getEntry()
        basePresenter = presenterClass?.newInstance()
        basePresenter?.attachView(this)
    }

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

    /**
     * 设置是否可滑动返回
     */
    fun setSwipeBackEnable( swipeBackEnable:Boolean){
        mSwipeBackHelper?.setSwipeBackEnable(swipeBackEnable)
    }

    /**
     * 是否侧边栏滑动返回
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
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper!!.swipeBackward()
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


    override fun refresh() {

    }


    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showToast(msg: String) {
        ToastUtils.toshort(this, msg)
    }

    override fun showToast(msg: Int) {
        showToast(getString(msg))
    }


    override fun getMyContext(): Context {
        return this
    }

    override fun onDestroy() {
        if (basePresenter != null) {
            basePresenter?.detachView()
        }
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        AppManager.getAppManager().finishActivity(this)
    }

    private fun getEntry(): Class<T>? {
        val type = javaClass.genericSuperclass
        var result: Class<T>? = null
        if (type is ParameterizedType) {
            result = type.actualTypeArguments[0] as Class<T>
        }
        return result
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
}