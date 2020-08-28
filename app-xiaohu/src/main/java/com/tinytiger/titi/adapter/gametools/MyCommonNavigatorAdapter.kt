package com.tinytiger.titi.adapter.gametools

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import java.util.ArrayList

/**
 * @author zwy
 * create at 2020/6/28 0028
 * description:
 */
class MyCommonNavigatorAdapter(var context: Context?,var titles: ArrayList<String>): CommonNavigatorAdapter(){
    var titleListener: ((index: Int) -> Unit)? = null
    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
        val titleView = ScaleTransitionPagerTitleView(context)
        titleView.text = (titles[index])
        titleView.textSize = 16f
        titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        titleView.normalColor = Color.WHITE
        titleView.selectedColor = Color.WHITE
        titleView.minScale = 0.8.toFloat()
        titleView.setOnClickListener {
            titleListener?.invoke(index)
        }

        return titleView
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getIndicator(context: Context?): IPagerIndicator? {
        return if (titles.size > 1) {
            Indicator.mLinePagerIndicator(context!!, R.color.color_ffcc03, 10)
        } else {
            null
        }
    }

}