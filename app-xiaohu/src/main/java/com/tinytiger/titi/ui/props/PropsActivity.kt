package com.tinytiger.titi.ui.props

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.KeyEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils

import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView


import com.tinytiger.titi.mvp.contract.props.PropsContract
import com.tinytiger.titi.mvp.presenter.props.PropsPresenter

import kotlinx.android.synthetic.main.home_fagment_game.magic_indicator
import kotlinx.android.synthetic.main.props_activity.*

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
 * @Date 2020-02-03 16:06
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具商城首页
 *
 */
class PropsActivity : BaseActivity(), PropsContract.View {


    private val mFragmentList = ArrayList<Fragment>()
    private var titles = ArrayList<String>()

    private var mAdapter: CommonNavigatorAdapter? = null

    companion object {

        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, PropsActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun layoutId(): Int = R.layout.props_activity

    private val mPresenter by lazy { PropsPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

    }

    override fun initView() {

        title_view.centerTxt="道具商城"
        title_view.rightTxt="我的道具"

        title_view.setBackOnClick {
            finish()
        }
        title_view.setRightTxtColor(ContextCompat.getColor(this,R.color.color_ffcc03))
        title_view.setRightOnClick {
            PropsMeActivity.actionStart(this)
        }

        //输入监听
        et_send.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (FastClickUtil.isFastClickTiming()) {
                        return false
                    }
                    val keyWord = et_send.text.toString().trim()

                    if (keyWord.isNullOrEmpty()) {
                        showToast("请输入你感兴趣的关键词")
                        return false
                    }
                    et_send.setText("")
                    PropsSearchActivity.actionStart(this@PropsActivity,keyWord)
                    closeKeyBord(et_send)
                }
                return false
            }
        })

        start()
    }


    fun closeKeyBord(mEditText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun start() {
        mPresenter.loadindexGoodsCate(0)
    }



    private fun initMagicIndicator() {

        val commonNavigator = CommonNavigator(this)
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
                titleView.selectedColor = ContextCompat.getColor(context, R.color.black)
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

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)

        ViewPagerHelper.bind(magic_indicator, mViewPager)
    }

    override fun indexGoodsCate(bean: PropsTabListBean) {
        if (bean.data!=null&&bean.data.size>0){
            for ( i in bean.data){
                titles.add(i.name)
                mFragmentList.add(PropsFragment.getInstance(i.id))
            }
            initMagicIndicator()
            SpUtils.saveSP(R.string.props_tab, JSON.toJSON(bean).toString())
        }else{
            setLoadTitile()
        }

    }

    private fun setLoadTitile(){
        val game_title= SpUtils.getString(R.string.props_tab,"")
        if (!game_title.isEmpty()){
            val bean = JSON.parseObject(game_title, PropsTabListBean::class.java)
            indexGoodsCate(bean)
        }
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
        if(mErrorView!=null){
            fl_content.removeView(mErrorView)
            mErrorView =null
        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
                mErrorView = getErrorLayout()
                fl_content.addView(
                    mErrorView,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels
                )
        } else {
            setLoadTitile()
        }
    }

}
