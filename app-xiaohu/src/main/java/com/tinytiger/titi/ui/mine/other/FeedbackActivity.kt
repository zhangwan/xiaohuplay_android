package com.tinytiger.titi.ui.mine.other

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.CatListBean
import com.tinytiger.common.net.data.mine.FeedbackListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.mine.FeedbackContract
import com.tinytiger.titi.mvp.presenter.mine.FeedbackPresenter
import kotlinx.android.synthetic.main.activity_feedback.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList


/*
* @author Tamas
* create at 2020/1/7 0007
* Email: ljw_163mail@163.com
* description: 
*/
class FeedbackActivity : BaseBackActivity(), FeedbackContract.View {

    private val mFragmentList = ArrayList<Fragment>()

    private val titles = ArrayList<String>()


    private val mPresenter by lazy { FeedbackPresenter() }

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
            val intent = Intent(context, FeedbackActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.activity_feedback

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        title_view.centerTxt = "产品建议墙"
        title_view.setBackOnClick {
            finish()
        }

        fab_add.setOnClickListener {
            if (isLoginStart()) {
                //填写意见
                FeedbackSendActivity.actionStart(this, 1000)
            }
        }
        start()
    }

    override fun start() {
        mPresenter.getCatList()
    }


    private fun initMagicIndicator(bean: List<CatListBean.CateBean>) {
        mFragmentList.clear()
        titles.clear()

        for (item in bean) {
            titles.add(item.type_name)
            mFragmentList.add(FeedbackFragment.getInstance(item.id))
        }

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true

        val mAdapter = object : CommonNavigatorAdapter() {
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
                titleView.selectedColor = ContextCompat.getColor(context, R.color.gray28)
                titleView.minScale = 0.9.toFloat()
                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                }

                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return if (titles.size > 1) {
                    Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
                } else {
                    null
                }
            }
        }


        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                //  onTitleShow(false)
                setSwipeBackEnable(position==0)
            }

        })

        ViewPagerHelper.bind(magic_indicator, mViewPager)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    /**
     * ================================================= Contract 接口 =================================================
     */

    override fun showCateList(bean: List<CatListBean.CateBean>) {
        if (bean.isNotEmpty()) {
            initMagicIndicator(bean)
        }

    }

    override fun showProposalList(bean: FeedbackListBean.DataBean?) {
    }


    override fun showResult() {
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        if (mErrorView != null) {
            fl_content.removeView(mErrorView)
            mErrorView = null
        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null) {
                mErrorView = getErrorLayout(true)
                fl_content.addView(mErrorView)
            }
        } else {
            dismissLoading()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            val content = data!!.getStringExtra("feedback")
            val opinion_cat_id = data.getIntExtra("opinion_cat_id",0)

            if (content.isNotEmpty()) {
                if(opinion_cat_id!=0){
                    val fm = mFragmentList[opinion_cat_id] as FeedbackFragment
                    fm.addFeedback(content)
                }
                val fm0 = mFragmentList[0] as FeedbackFragment
                fm0.addFeedback(content)
            }
        }
    }

}