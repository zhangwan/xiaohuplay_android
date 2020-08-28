package com.tinytiger.titi.ui.props



import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.alibaba.fastjson.JSON
import com.tinytiger.common.adapter.TabAdapter

import com.tinytiger.common.widget.LoadingUtils

import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.BaseBean

import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView


import com.tinytiger.titi.R

import com.tinytiger.titi.mvp.contract.props.PropsContract
import com.tinytiger.titi.mvp.presenter.props.PropsPresenter

import kotlinx.android.synthetic.main.props_activity.mViewPager
import kotlinx.android.synthetic.main.props_activity.magic_indicator
import kotlinx.android.synthetic.main.props_fragment_me_vp.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import kotlin.collections.ArrayList


/**
 *
 * @Author luke
 * @Date 2020-02-03 16:33
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 我的道具分类
 *
 */
class PropsMeVpFragment : BaseFragment(), PropsContract.View{
    private val mFragmentList = ArrayList<Fragment>()
    private var titles = ArrayList<String>()

    companion object {
        fun getInstance(): PropsMeVpFragment {
            val fragment = PropsMeVpFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.props_fragment_me_vp

    private val mPresenter by lazy { PropsPresenter() }


    override fun initView() {
        mPresenter.attachView(this)

        start()
    }

    override fun onResume() {
        super.onResume()
        if (!load){
            start()
        }
    }

    private var load=true

    override fun start() {
        mPresenter.loadindexGoodsCate(1)
    }

    override fun indexGoods(bean: PropsListBean) {
  }

    override fun wearProps(bean: BaseBean) {

    }


    override fun showLoading() {
        LoadingUtils.getInstance().show(context)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        setLoadTitile()
    }
    private var mAdapter: CommonNavigatorAdapter? = null

    private fun initMagicIndicator() {

        val commonNavigator = CommonNavigator(activity)
        commonNavigator.isAdjustMode = false

        mAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //文字
                val titleView = ScaleTransitionPagerTitleView(context)
                titleView.text = (titles[index])
                titleView.textSize = 15f
                titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                titleView.normalColor = ContextCompat.getColor(context, R.color.gray99)
                titleView.selectedColor = ContextCompat.getColor(context, R.color.color_ffcc03)
                titleView.minScale = 0.9.toFloat()
                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                }

                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = Dp2PxUtils.dip2px(context, 2).toFloat()
                indicator.lineWidth = Dp2PxUtils.dip2px(context, 24).toFloat()

                indicator.roundRadius=3.toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(ContextCompat.getColor(context, R.color.color_ffcc03))
                indicator.yOffset = 0.toFloat()
                return indicator
            }
        }

        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(childFragmentManager, mFragmentList)

        ViewPagerHelper.bind(magic_indicator, mViewPager)
    }

    override fun indexGoodsCate(bean: PropsTabListBean) {
        if (bean.data!=null&&bean.data.size>0){
            load=true
            if (mErrorView != null) {
                rl_content.removeView(mErrorView)
            }
            for ( i in bean.data){
                titles.add(i.name)
                mFragmentList.add(PropsMeFragment.getInstance(i.id))
            }
            initMagicIndicator()
        }else{
            load=false
            if (mErrorView == null) {
                mErrorView = RefreshView.getEmptyView(
                    activity,
                    "背包空空如也~",
                    rl_content
                )
            }else{
                rl_content.removeView(mErrorView)
            }
            rl_content.addView(
                mErrorView,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels
            )
        }

    }

    private fun setLoadTitile(){
        val game_title= SpUtils.getString(R.string.props_tab_me,"")
        if (!game_title.isEmpty()){
            val bean = JSON.parseObject(game_title, PropsTabListBean::class.java)
            indexGoodsCate(bean)
        }
    }

}
