package com.tinytiger.titi.ui.mine.me.release

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import kotlinx.android.synthetic.main.mine_activity_collect.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus

/*
 * @author zwy
 * create at 2020/5/19 0019
 * description: 我的发布页面
 */
class MineReleaseActivity : BaseActivity() {
    private var mReleaseCommentFragment: ReleaseCommentFragment? = null
    private var mReleaseAnswersFragment: ReleaseAnswersFragment? = null
    private var mReleaseDynamicFragment: ReleaseDynamicFragment? = null
    private val mFragmentList = ArrayList<Fragment>()
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
            val intent = Intent(context, MineReleaseActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.mine_activity_release

    override fun initData() {

    }

    override fun initView() {
        tvTitle.centerTxt = getString(R.string.mine_works)
//        tvTitle.rightTxt = "编辑"
        tvTitle.setBackOnClick {
            finish()
        }
        tvTitle.setRightOnClick {

            when (tvTitle.rightTxt) {
                "编辑" -> {
                    tvTitle.rightTxt = "完成"
                    tvTitle.setRightTxtColor(Color.parseColor("#0FB50A"))
                    setPageSelected(true)
                }
                "完成" -> {
                    tvTitle.rightTxt = "编辑"
                    tvTitle.setRightTxtColor(Color.parseColor("#323232"))
                    setPageSelected(false)

                }
                "删除" -> {
                    delComment()
                }
            }
        }
        tvTitle.setRightV(View.GONE)
        initMagicIndicator()
        start()

    }

    override fun start() {

    }

    private fun initMagicIndicator() {
        mReleaseCommentFragment =
            ReleaseCommentFragment.getInstance()
        mReleaseDynamicFragment =
            ReleaseDynamicFragment.getInstance()
        mReleaseAnswersFragment =
            ReleaseAnswersFragment.getInstance()
        mFragmentList.add(mReleaseCommentFragment!!)
        mFragmentList.add(mReleaseDynamicFragment!!)
        mFragmentList.add(mReleaseAnswersFragment!!)
        titles.add(getString(R.string.mine_release_comment))
        titles.add(getString(R.string.mine_release_dynamic))
        titles.add(getString(R.string.mine_release_answers))

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
                titleView.textSize = 16f
                titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                titleView.normalColor = ContextCompat.getColor(context, R.color.gray99)
                titleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)
                titleView.minScale = 0.8.toFloat()
                titleView.setOnClickListener {
                    mViewPager.currentItem = index
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
        mViewPager.offscreenPageLimit=0
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setPageSelected(false)
            }
        })
    }
    private fun delComment(){
        when (mViewPager.currentItem) {
            1 -> {
//                mReleaseDynamicFragment?.delComment()
            }
            2 -> {
//                mReleaseAnswersFragment?.delComment()
            }
            else -> {
//                mReleaseCommentFragment?.delComment()
            }
        }
    }
    fun showEditButton(total: Int) {
        if (total > 0) {
            tvTitle?.rightTxt = "编辑"
            tvTitle?.setRightTxtColor(Color.parseColor("#323232"))
//            tvTitle?.setRightV(View.VISIBLE)
            tvTitle?.setRightV(View.GONE)
        } else {
            tvTitle?.setRightV(View.GONE)
        }

    }
    private fun setPageSelected(isShow: Boolean) {
        when (mViewPager.currentItem) {
            1 -> {
                mReleaseDynamicFragment?.showEditStatus(isShow)
            }
            2 -> {
                mReleaseAnswersFragment?.showEditStatus(isShow)
            }
            else -> {
                mReleaseCommentFragment?.showEditStatus(isShow)
            }
        }
    }



}