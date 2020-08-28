package com.tinytiger.titi.ui.props

import android.content.Context
import android.content.Intent

import android.view.View


import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tinytiger.common.adapter.BaseFragmentAdapter
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean

import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils

import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView


import com.tinytiger.titi.mvp.contract.props.PropsContract
import com.tinytiger.titi.mvp.presenter.props.PropsPresenter
import com.tinytiger.titi.ui.props.exchage.PropsExchangeActivity

import kotlinx.android.synthetic.main.home_fagment_game.magic_indicator
import kotlinx.android.synthetic.main.props_activity_me.*

import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import kotlin.collections.ArrayList

/**
 *
 * @Author luke
 * @Date 2020-02-03 16:06
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 我的道具
 *
 */
class PropsMeActivity : BaseActivity(), PropsContract.View {


    private val mFragmentList = ArrayList<Fragment>()
    private var titles = arrayListOf("我的道具")

    private var mAdapter: CommonNavigatorAdapter? = null

    companion object {

        fun actionStart(context: Context) {
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, PropsMeActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun layoutId(): Int = R.layout.props_activity_me

    private val mPresenter by lazy { PropsPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
    }

    private var proType=0
    override fun initView() {
        proType=SpUtils.getInt(R.string.props_user,0)

        ivBack.setOnClickListener {
            finish()
        }
        mFragmentList.add(PropsMeVpFragment.getInstance())
        if (proType==1){
            titles.add("我的兑换码")
            mFragmentList.add(PropsMeCodeFragment.getInstance())

            ivAddProps.visibility=View.VISIBLE
            ivAddProps.setOnClickListener {
                PropsExchangeActivity.actionStart(this)
            }
        }

        initMagicIndicator()
    }


    override fun start() {
        //mPresenter.loadContentInfo("")
    }


    private fun initMagicIndicator() {

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true

        mAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //文字
                val titleView = ScaleTransitionPagerTitleView(context)
                titleView.text = (titles[index])
                titleView.textSize = 15f
                // titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                titleView.normalColor = ContextCompat.getColor(context, R.color.color_808080)
                titleView.selectedColor = ContextCompat.getColor(context, R.color.gray28)
                titleView.minScale = 0.9.toFloat()
                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                }

                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {

                return null
            }
        }

        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = BaseFragmentAdapter(supportFragmentManager, mFragmentList)

        ViewPagerHelper.bind(magic_indicator, mViewPager)

    }

    var mContentInfoBean: ContentInfoBean.DataBean? = null


    override fun indexGoodsCate(bean: PropsTabListBean) {
          }

    override fun indexGoods(bean: PropsListBean) {
          }

    override fun wearProps(bean: BaseBean) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        if (mErrorView != null) {
            //   fl_content.removeView(mErrorView)
            mErrorView = null
        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {

            if (mErrorView == null && mContentInfoBean == null) {
                //mErrorView = getErrorLayout(true)
                //fl_content.addView(mErrorView)
            }
            showToast(errorMsg)
        } else {
            showToast(errorMsg)
        }
    }


}
