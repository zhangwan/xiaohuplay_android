package com.tinytiger.titi.ui.mine.me.circle

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.basis.BasicNullPresenterActivity
import com.tinytiger.common.basis.BasisActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData

import com.tinytiger.titi.ui.mine.me.circle.vp.AddCirclePresenter
import com.tinytiger.titi.ui.mine.me.release.MineReleaseActivity
import kotlinx.android.synthetic.main.activity_add_circle.*
import kotlinx.android.synthetic.main.activity_add_circle.mViewPager
import kotlinx.android.synthetic.main.activity_add_circle.magic_indicator
import kotlinx.android.synthetic.main.activity_add_circle.tvTitle
import kotlinx.android.synthetic.main.mine_activity_base_viewpager.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus

class AddCircleActivity : BasicNullPresenterActivity(){
    private val mFragmentList = ArrayList<Fragment>()
    private var mAddCircleFragment: MyAddCircleFragment? = null
    private var mBrowseFragment: MyBrowseFragment? = null
    private val titles = ArrayList<String>()
    private var mAdapter: CommonNavigatorAdapter? = null

    companion object {
        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, AddCircleActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun layoutId(): Int= R.layout.activity_add_circle

    override fun initData() {

    }

    override fun initView() {
        tvTitle.centerTxt = getString(R.string.mine_circle)
        initMagicIndicator()
        initListener()
    }
    private fun initMagicIndicator() {
        mAddCircleFragment = MyAddCircleFragment.getInstance()
        mBrowseFragment = MyBrowseFragment.getInstance()
        mFragmentList.add(mAddCircleFragment!!)
        mFragmentList.add(mBrowseFragment!!)

        titles.add(getString(R.string.circle_add_title))
        titles.add(getString(R.string.circle_browse_title))

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
                titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                titleView.normalColor = ContextCompat.getColor(context, R.color.gray99)
                titleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)
                titleView.minScale = 0.8.toFloat()

                val title =titles[index]

                titleView.text = title

                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                    mAdapter?.notifyDataSetChanged()
                }

                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
            }
        }

        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)
        ViewPagerHelper.bind(magic_indicator, mViewPager)
    }
    fun initListener(){
        tvTitle.setBackOnClick {
            finish()
        }
    }
}