package  com.tinytiger.common.widget.base

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.R
import com.tinytiger.common.utils.Dp2PxUtils


/**
 *
 */
abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        startUpAnimation(view!!)

        return view
    }

    var views: View? = null

     var isShowNavigationBarHeight = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views = view
        initUi(view)
        initData()
        initView()

    }

    private fun initView() {
        //判断是否有导航栏，有导航栏的话获取高度
        if (isShowNavigationBarHeight)
            activity?.window?.decorView?.setPadding(0, 0, 0, Dp2PxUtils.getNavigationBarHeight(context))
    }



    protected fun itDismiss() {
        startDownAnimation(views!!)
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     *
     * @return
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 执行数据的加载
     */
    protected abstract fun initData()

    protected abstract fun initUi(v: View)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setStyle(STYLE_NORMAL, R.style.dialogStyle)
    }


    override fun onStart() {
        super.onStart()

        val window = dialog!!.window
        val params = window!!.attributes
        params.gravity = Gravity.BOTTOM // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度填充满屏
        window.attributes = params
        window.setBackgroundDrawableResource(R.color.flashlight_bg)
        // 这里用透明颜色替换掉系统自带背景
        /* val color = ContextCompat.getColor(activity, R.color.flashlight_bg)
         window.setBackgroundDrawable(ColorDrawable(color))*/
        // window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }


    protected fun startUpAnimation(view: View) {
        val slide = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f)

        slide.duration = 150
        slide.fillAfter = true
        slide.isFillEnabled = true
        view.startAnimation(slide)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity!!.window.statusBarColor = android.graphics.Color.TRANSPARENT
        } else {
            activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    protected fun startDownAnimation(view: View) {
        val slide = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f)

        slide.duration = 250
        slide.fillAfter = true
        slide.isFillEnabled = true
        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                dismiss()
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
        view.startAnimation(slide)
    }


    /**
     * 设置沉浸式
     *
     * @param activity 当前展示的activity
     * @return CoordinatorTabLayout
     */
    fun setTranslucentNavigationBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity!!.window.statusBarColor = android.graphics.Color.TRANSPARENT
        } else {
            activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

    }


    private fun changeStatusBarTextColor(activity: Activity, isBlack: Boolean) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//设置状态栏黑色字体
            } else {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE//恢复状态栏白色字体
            }
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mOnDismissListener?.onDismiss(dialog)
    }

    private var mOnDismissListener: OnDismissListener? = null
    fun setOnDismissListener(listener: OnDismissListener) {
        mOnDismissListener = listener
    }

    interface OnDismissListener {
        fun onDismiss(dialog: DialogInterface?)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, getFragmentTag())
    }

    private val TAG = "base_bottom_dialog"
    fun getFragmentTag(): String {
        return TAG
    }
}
