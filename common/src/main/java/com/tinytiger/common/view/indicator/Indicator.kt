package com.tinytiger.common.view.indicator

import android.content.Context
import android.graphics.Typeface
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.tinytiger.common.R
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 *
 * @author zhw_luke
 * @date 2020/3/19 0019 下午 2:56
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 导航菜单
 */
object Indicator {

    /**
     * 获取导航条
     * titles 文字
     * 通用
     * min 大小变化
     */
    fun mTitleView(context: Context,titles:String,min:Float): ScaleTransitionPagerTitleView {

        val titleView = ScaleTransitionPagerTitleView(context)
        titleView.text = (titles)
        titleView.textSize = 16f
        titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        titleView.normalColor = ContextCompat.getColor(context, R.color.grayAA)
        titleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)
        titleView.minScale = min
        return titleView
    }

    fun mTitleView(context: Context,titles:String,min:Float, style:Int): ScaleTransitionPagerTitleView {

        val titleView = ScaleTransitionPagerTitleView(context)
        titleView.text = (titles)
        titleView.textSize = 16f
        titleView.typeface = Typeface.defaultFromStyle(style)
        titleView.normalColor = ContextCompat.getColor(context, R.color.grayAA)
        titleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)
        titleView.minScale = min
        return titleView
    }


    /**
     * 获取导航条
     * titles 文字
     * 通用
     * min 大小变化
     */
    fun mTitleViewScale(context: Context,titles:String,min:Float): ScaleTransitionPagerTitleView {
        val titleView = ScaleTransitionPagerTitleView(context)
        titleView.text = (titles)
        titleView.textSize = 16f
        titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        titleView.normalColor = ContextCompat.getColor(context, R.color.gray66)
        titleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)
        titleView.minScale = min
        return titleView
    }



    /**
     * 获取导航条
     * 圆角 3 宽10 高3
     * 通用
     */
    fun mLinePagerIndicator(context: Context,color:Int,width:Int): LinePagerIndicator {
        val indicator = LinePagerIndicator(context)
        indicator.mode = LinePagerIndicator.MODE_EXACTLY
        indicator.lineHeight = Dp2PxUtils.dip2px(context, 3).toFloat()
        indicator.lineWidth = Dp2PxUtils.dip2px(context, width).toFloat()

        indicator.roundRadius = 6.toFloat()
        indicator.startInterpolator = AccelerateInterpolator()
        indicator.endInterpolator = DecelerateInterpolator(2.0f)
        indicator.setColors(ContextCompat.getColor(context, color))
        indicator.yOffset = 0.toFloat()
        return indicator
    }
}