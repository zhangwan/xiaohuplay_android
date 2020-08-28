package com.tinytiger.titi.ui.main


import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.title.TitleCiecleView
import com.tinytiger.titi.ui.circle.CircleAttentionFragment
import com.tinytiger.titi.ui.circle.CirclePushFragment
import com.tinytiger.titi.ui.circle.post.SendPostActivity
import com.tinytiger.titi.ui.msg.ImActivity

import com.tinytiger.titi.ui.search.SearchActivity
import com.tinytiger.titi.utils.AutoPlayUtils
import kotlinx.android.synthetic.main.main_fagment_circle.*
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2019/10/30 0030 下午 1:51
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 圈子首页
 */
class CircleFragment : BaseFragment() {

    companion object {
        fun getInstance(): CircleFragment {
            val fragment = CircleFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.main_fagment_circle

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        TAG = "CircleFragment"
        initMagicIndicator()
        ivSearch.setOnClickListener {
            SearchActivity().actionStart(activity!!)
        }
        ivMsg.setOnClickListener {
            ImActivity.actionStart(activity!!)
        }
        fl_drag.setOnClickChildListener {
            SendPostActivity.actionStart(activity!!)
        }

    }

    override fun start() {
        if (mViewPager?.currentItem==1){
            mCirclePushFragment?.setDoubleRefresh()
        }else{
            mCircleAttentionFragment?.setDoubleRefresh()
        }

    }
    /**
     * 滚动到顶部
     */
    fun topScorll(){
        if (mViewPager?.currentItem==1){
            mCirclePushFragment?.startTop()
        }else{
            mCircleAttentionFragment?.startTop()
        }
    }


    private val mFragmentList = ArrayList<Fragment>()
    private fun initMagicIndicator() {

        thvTitle.onCieclePageListener = TitleCiecleView.OnCieclePageListener { index -> mViewPager.currentItem = index }
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                AutoPlayUtils.index = position + 1
                AutoPlayUtils.pageType = position + 1
                thvTitle.setSwitch(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mCirclePushFragment = CirclePushFragment.getInstance()
        mCircleAttentionFragment=CircleAttentionFragment.getInstance()
        mFragmentList.add(mCircleAttentionFragment!!)
        mFragmentList.add(mCirclePushFragment!!)

        AutoPlayUtils.homePlayerMap[2] = -1
        AutoPlayUtils.homePlayerMap[1] = -1
        mViewPager.adapter = TabAdapter(childFragmentManager, mFragmentList)
        mViewPager.setCurrentItem(1, false)

        mViewPager.offscreenPageLimit
        tocken = SpUtils.getString(R.string.token, "")
    }

    var mCirclePushFragment: CirclePushFragment? = null
    var mCircleAttentionFragment: CircleAttentionFragment? = null
    override fun onResume() {
        super.onResume()
        setMsg()

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden){
            if(mViewPager.currentItem==0){
                mCircleAttentionFragment?.pauseVideo()
            }else if(mViewPager.currentItem==1){
                mCirclePushFragment?.pauseVideo()
            }
        }else{
            if(mViewPager.currentItem==0){
                mCircleAttentionFragment?.resumeVideo()
            }else if(mViewPager.currentItem==1){
                mCirclePushFragment?.resumeVideo()
            }
        }
    }

    var tocken = ""

    fun setMsg() {
        val unreadNum = (activity as MainActivity).readNumMap[SpUtils.getString(R.string.user_id, "")] ?: 0
        if (unreadNum == 0) {
            ivMsg1.visibility = View.GONE
        } else {
            ivMsg1.visibility = View.VISIBLE
        }
    }


}
