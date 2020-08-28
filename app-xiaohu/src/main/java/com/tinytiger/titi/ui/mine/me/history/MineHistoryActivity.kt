package com.tinytiger.titi.ui.mine.me.history

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.mine.HistoryContract
import com.tinytiger.titi.mvp.presenter.mine.HistoryPresenter
import kotlinx.android.synthetic.main.mine_activity_base_viewpager.*
import kotlinx.android.synthetic.main.mine_activity_base_viewpager.mViewPager
import kotlinx.android.synthetic.main.mine_activity_base_viewpager.magic_indicator
import kotlinx.android.synthetic.main.mine_activity_base_viewpager.tvTitle
import kotlinx.android.synthetic.main.mine_activity_collect.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 个人中心 - 我的历史 Activity
*/
class MineHistoryActivity : BaseActivity(), HistoryContract.View {
    override fun getUserFollow(uid: String, is_mutual: Int) {

    }

    override fun showAmwayHistory(bean: UserGameAmwayList.Data) {

    }

    override fun showViewHistory(bean: MyContentListBean.DataBean) {

    }

    override fun showResult() {
        when (mViewPager.currentItem) {
            0 -> {
                //清除类型 1资讯 2安利文
                mHistoryAmwayFragment?.clearAll()
                updateNumber(0,0)
            }
            1 -> {
                mHistoryNewsFragment?.clearAll()
                updateNumber(0,1)
            }
            else -> {
//                mHistoryNodeFragment?.clearAll()
                updateNumber(0,2)
            }
        }
        showEditButton(0)
    }


    private val mPresenter by lazy { HistoryPresenter() }


    private val mFragmentList = ArrayList<Fragment>()

    private val titles = ArrayList<String>()


    companion object {

        fun actionStart(context: Context) {
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
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
            val intent = Intent(context, MineHistoryActivity::class.java)
            context.startActivity(intent)
        }

    }


    override fun layoutId(): Int = R.layout.mine_activity_collect

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        tvTitle.centerTxt = "历史记录"
        tvTitle.setBackOnClick {
            finish()
        }
        tvTitle.rightTxt = "清空"
        tvTitle.setRightOnClick {
            TextDialog.create(supportFragmentManager)
                .setMessage("确定清空记录?")
                .setViewListener(object : TextDialog.ViewListener {
                    override fun click() {
                        when (mViewPager.currentItem) {
                            0 -> {
                                //清除类型 1资讯 2安利文
                                mPresenter.clearViewHistory(2)
                            }
                            1 -> {
                                mPresenter.clearViewHistory(1)
                            }
                            else -> {
                                mPresenter.clearViewHistory(3)
                            }
                        }

                    }

                })
                .show()
        }
        tvTitle.setRightV(View.GONE)

        initMagicIndicator()

        start()
    }

    private var mHistoryAmwayFragment: HistoryAmwayFragment? = null

    private var mHistoryNewsFragment: HistoryNewsFragment? = null

//    private var mHistoryNodeFragment: HistoryNodeFragment? = null

    private var mAdapter: CommonNavigatorAdapter? = null
    private val number = intArrayOf(0, 0, 0)
    private fun initMagicIndicator() {
        mHistoryAmwayFragment = HistoryAmwayFragment.getInstance()
        mHistoryNewsFragment = HistoryNewsFragment.getInstance()
//        mHistoryNodeFragment=HistoryNodeFragment.getInstance()

        mFragmentList.add(mHistoryAmwayFragment!!)
        mFragmentList.add(mHistoryNewsFragment!!)
//        mFragmentList.add(mHistoryNodeFragment!!)
        titles.add("安利")
        titles.add("资讯")
//        titles.add("帖子")

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

                val title = if (index == mViewPager.currentItem) {
                    "${titles[index]}${number[index]}"
                } else {
                    "${titles[index]}"
                }


                val sb = SpannableString(title)
                sb.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_ffcc03)), 2,
                    title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                titleView.text = sb

                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                    mAdapter?.notifyDataSetChanged()
                }

                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = Dp2PxUtils.dip2px(context, 4).toFloat()
                indicator.lineWidth = Dp2PxUtils.dip2px(context, 10).toFloat()

                indicator.roundRadius = 2.toFloat()
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
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tvTitle.setRightV(View.GONE)
                if (position == 0) {
                    mHistoryAmwayFragment?.setPageSelected()
                } else {
                    mHistoryNewsFragment?.setPageSelected()
                }

            }
        })
        ViewPagerHelper.bind(magic_indicator, mViewPager)


    }


    fun showEditButton(total: Int) {
        tvTitle.rightTxt = "清空"
        if (total > 0) {
            tvTitle.setRightV(View.VISIBLE)
        } else {
            tvTitle.setRightV(View.GONE)
        }
    }

    fun updateNumber(total: Int,position:Int) {
        if (position < number.size && number[position] != total) {
            number[position] = total
            mAdapter?.notifyDataSetChanged()
        }
    }


    override fun start() {

    }


    override fun showLoading() {

    }

    override fun dismissLoading() {
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
    }
}