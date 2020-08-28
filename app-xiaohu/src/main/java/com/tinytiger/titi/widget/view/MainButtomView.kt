package com.tinytiger.titi.widget.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.orhanobut.logger.Logger
import com.tinytiger.common.utils.AnimationUtil
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.view_main_buttom.view.*

/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 4:29
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 发帖首页发帖按钮
 */
class MainButtomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        initView(context)
    }

    /**
     * 当前打开页面
     */
    private var currTabIndex = ""

    var xPixels = 0
    var dp55 = 0
    private var sysTime = 0.toLong()
    private var clickTime = 0.toLong()

    /**
     * 显示数量
     */
    private var indexSzie = 3


    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_main_buttom, this)

        card_rb_home.setOnClickListener {
            switchFragment("home")
        }

        card_rb_msg.setOnClickListener {
            switchFragment("circle")
        }
        card_rb_me.setOnClickListener {
            switchFragment("mine")
        }

        card_rb_tool.setOnClickListener {
            switchFragment("tool")
        }

        llBack.setOnClickListener {
            clickTime = System.currentTimeMillis()
            mListener?.onClickTop(currTabIndex)
            llBack.animation = AnimationUtil.moveToViewBottom()
            llBack.postDelayed({ llBack.visibility = View.GONE }, 200)
        }
        dp55 = Dp2PxUtils.dip2px(context, 55) / 2

    }

    /**
     * index 显示底部栏状态
     */
    fun setPixels() {
       val index=SpUtils.getInt(R.string.tool_config,0)
        if (index==1){
            indexSzie = 4
            card_rb_tool.visibility=View.VISIBLE
            xPixels =  resources.displayMetrics.widthPixels / 8

            ivNewMsg.x = (xPixels * 7 + 20).toFloat()
        }else{
            card_rb_tool.visibility=View.GONE
            xPixels =  resources.displayMetrics.widthPixels / 6
            ivNewMsg.x = (xPixels * 5 + 20).toFloat()
        }

        val lp = llBack.layoutParams
        lp.width = xPixels * 2
        llBack.layoutParams = lp

    }

    /**
     * 设置显示页码
     * home 首页
     * circle 圈子
     * mine 我
     * tool 工具
     */
    fun switchFragment(title: String) {
        if (currTabIndex == title) {
            setDouble(title)
            return
        }
        //防止快速来回切换
        if (System.currentTimeMillis() - sysTime < 500) {
            setTab(title)
            return
        }
        sysTime = System.currentTimeMillis()
        when (title) {
            "home" -> {
                ivOver.x = xPixels.toFloat() - dp55
                llBack.x =0.toFloat()
            }
            "circle" -> {
                if (indexSzie == 4) {
                    ivOver.x = xPixels * 5.toFloat() - dp55
                    llBack.x = xPixels.toFloat() * 4
                } else {
                    ivOver.x = xPixels * 3.toFloat() - dp55
                    llBack.x = xPixels.toFloat() * 2
                }
            }
            "mine" -> {
                if (indexSzie == 4) {
                    ivOver.x = xPixels * 7.toFloat() - dp55
                } else {
                    ivOver.x = xPixels * 5.toFloat() - dp55
                }
            }
            "tool" -> {
                ivOver.x = xPixels * 3.toFloat() - dp55
            }

        }
        setTab(title)
        currTabIndex = title
        mListener?.onPostionView(title)
        llBack.visibility = View.GONE
    }

    /**
     * 双击事件
     */
    fun setDouble(title: String) {
        if (System.currentTimeMillis() - sysTime < 1200) {
            when (title) {
                "home" -> {
                    mListener?.onClickDouble(title)
                }
                "circle" -> {
                    mListener?.onClickDouble(title)
                }
            }

        }
        sysTime = System.currentTimeMillis()
    }

    /**
     * 设置选中tab
     */
    private fun setTab(title: String) {
        when (title) {
            "home" -> {
                card_rb_home.isChecked = true
            }
            "circle" -> {
                card_rb_msg.isChecked = true
            }
            "mine" -> {
                card_rb_me.isChecked = true
            }
        }
    }

    /**
     * 设置红点显示
     */
    fun setUnreadNum(unreadNum: Int) {
        if (unreadNum > 0 && !TextUtils.isEmpty(SpUtils.getString(R.string.token, ""))) {
            ivNewMsg.visibility = View.VISIBLE
        } else {
            ivNewMsg.visibility = View.GONE
        }
    }


    var mListener: OnMainButtomViewListener? = null

    interface OnMainButtomViewListener {
        fun onPostionView(title: String)
        fun onClickDouble(title: String)
        fun onClickTop(title: String)
    }


    /**
     * 显示返回顶部
     */
    fun setTopShow(visibility: Int) {
        if (System.currentTimeMillis() - clickTime < 1000) {
            return
        }

        if (visibility == llBack.visibility) {
            return
        }

        when (currTabIndex) {
            "home" -> {

            }
            "circle" -> {

            }
            else -> {
                return
            }
        }

        if (visibility == 0) {
            //显示,
            llBack.visibility = View.VISIBLE
            llBack.animation = AnimationUtil.moveToViewLocation()
        } else {
            //影藏
            llBack.animation = AnimationUtil.moveToViewBottom()
            llBack.postDelayed({ llBack.visibility = View.GONE }, 200)
        }
    }


}