package com.tinytiger.titi.ui.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.view.title.TitleHomeView
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.titi.event.HomeEvent
import com.tinytiger.titi.ui.home2.HomeFindFragment
import com.tinytiger.titi.ui.home2.HomePushFragment
import com.tinytiger.titi.ui.search.SearchActivity
import kotlinx.android.synthetic.main.main_fagment_play.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2019/10/30 0030 下午 1:51
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页
 */
class HomeFragment : BaseFragment() {

    companion object {
        fun getInstance(): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.main_fagment_play

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        initMagicIndicator()

    }

    override fun start() {

    }

    private val mFragmentList = ArrayList<Fragment>()
    private fun initMagicIndicator() {
        thvTitle.onHomePageListener = object : TitleHomeView.OnHomePageListener {
            override fun onPage(index: Int) {
                mViewPager.currentItem = index
            }
        }

        ivSearch.setOnClickListener {
           SearchActivity().actionStart(activity!!)
        }

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                thvTitle.setSwitch(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        mFragmentList.add(HomePushFragment.getInstance())
        mFragmentList.add(HomeFindFragment.getInstance())


        mViewPager.adapter =TabAdapter(childFragmentManager, mFragmentList)
    }


    /**
     * 滚动到顶部
     */
    fun topScorll(){
        when(mViewPager.currentItem){
            0->{
                (mFragmentList[0] as HomePushFragment).startTop()
            }
            1->{
                (mFragmentList[1] as HomeFindFragment).startTop()
            }
        }
    }

    fun setDoubleRefresh() {
        when(mViewPager.currentItem){
            0->{

            }
            1->{
                (mFragmentList[1] as HomeFindFragment).setDoubleRefresh()
            }
        }
    }
}
