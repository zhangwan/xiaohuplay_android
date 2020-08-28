package com.tinytiger.titi.ui.mine.me.collect

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.center.UserCollectWikiList
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.presenter.center.MineCollectsPresenter
import kotlinx.android.synthetic.main.mine_activity_base_viewpager.*
import kotlinx.android.synthetic.main.mine_activity_collect.*
import kotlinx.android.synthetic.main.mine_activity_collect.mViewPager
import kotlinx.android.synthetic.main.mine_activity_collect.magic_indicator
import kotlinx.android.synthetic.main.mine_activity_collect.tvTitle
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 个人中心 - 我的收藏 Activity
*/
class MineCollectsActivity : BaseActivity(), MineCollectContract.View {

    override fun batchCancelCollect() {

    }

    override fun getUserFollow(uid: String, is_mutual: Int) {

    }

    override fun showAmwayCollectList(bean: UserGameAmwayList.Data) {

    }

    override fun showNewsCollectList(bean: MyContentListBean.DataBean) {

    }

    override fun showUserWikiCollectList(bean: UserCollectWikiList.Data) {

    }

    override fun showPostCollectList(bean: PostInfoBean) {
    }


    val mPresenter by lazy { MineCollectsPresenter() }


    private val mFragmentList = ArrayList<Fragment>()

    private val titles = ArrayList<String>()
    private val number = intArrayOf(0, 0, 0, 0, 0)

    companion object {

        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, MineCollectsActivity::class.java)
            context.startActivity(intent)
        }

    }


    override fun layoutId(): Int = R.layout.mine_activity_collect

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        tvTitle.centerTxt = "我的收藏"
        tvTitle.setBackOnClick {
            finish()
        }
        tvTitle.rightTxt = "编辑"
        tvTitle.setRightOnClick {
            var isShow = true

            if (tvTitle.rightTxt.toString() == "编辑") {
                tvTitle.rightTxt = "取消"
            } else {
                tvTitle.rightTxt = "编辑"
                isShow = false
            }
            setPageSelected(isShow)
        }
        tvTitle.setRightV(View.GONE)


        initMagicIndicator()

        start()
    }

    private var mCollectGameFragment: CollectGameFragment? = null
    private var mCollectAmwayFragment: CollectAmwayFragment? = null
    private var mCollectWikiFragment: CollectWikiFragment? = null
    private var mCollectNewsFragment: CollectNewsFragment? = null
    private var mCollectNodeFragment: CollectNodeFragment? = null

    private var mAdapter: CommonNavigatorAdapter? = null

    private fun initMagicIndicator() {
        mCollectGameFragment = CollectGameFragment.getInstance()
        mCollectAmwayFragment = CollectAmwayFragment.getInstance()
        mCollectNewsFragment = CollectNewsFragment.getInstance()
        mCollectWikiFragment = CollectWikiFragment.getInstance()
        mCollectNodeFragment = CollectNodeFragment.getInstance()
        mFragmentList.add(mCollectGameFragment!!)
        mFragmentList.add(mCollectNodeFragment!!)
        mFragmentList.add(mCollectWikiFragment!!)
        mFragmentList.add(mCollectAmwayFragment!!)
        mFragmentList.add(mCollectNewsFragment!!)

        titles.add("游戏")
        titles.add("帖子")
        titles.add("百科")
        titles.add("安利")
        titles.add("资讯")

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
                return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
            }
        }

        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)
        ViewPagerHelper.bind(magic_indicator, mViewPager)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setPageSelected(false)
            }
        })
    }


    fun showEditButton(total: Int, isType: Boolean) {
        if (total > 0) {
            tvTitle.rightTxt = "编辑"
            if (isType) {
                tvTitle.rightTxt = "取消"
            }
            tvTitle.setRightV(View.VISIBLE)
        } else {
            tvTitle.setRightV(View.GONE)
        }

    }

    fun showNumber(total: Int, position: Int) {
        if (position < number.size && number[position] != total) {
            number[position] = total
            mAdapter?.notifyDataSetChanged()
        }
    }


    private fun setPageSelected(isShow: Boolean) {
        when (mViewPager.currentItem) {
            0 -> {
                mCollectGameFragment?.showEditStatus(isShow)
            }
            1 -> {
                mCollectNodeFragment?.showEditStatus(isShow)
            }
            2 -> {
                mCollectWikiFragment?.showEditStatus(isShow)

            }
            3 -> {
                mCollectAmwayFragment?.showEditStatus(isShow)
            }
            4 -> {
                mCollectNewsFragment?.showEditStatus(isShow)
            }
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