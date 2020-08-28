package com.tinytiger.titi.ui.mine.me.fans


import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tinytiger.common.adapter.BaseFragmentAdapter

import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.titi.data.MyUserData


import kotlinx.android.synthetic.main.mine_activity_me_friend.*
import kotlinx.android.synthetic.main.mine_activity_me_friend.mViewPager
import kotlinx.android.synthetic.main.mine_activity_me_friend.magic_indicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 查看自己的粉丝与关注
 */
class FriendActivity : BaseActivity() {

    companion object {
        fun actionStart(context: Context, index: Int) {
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

            val intent = Intent(context, FriendActivity::class.java)
            intent.putExtra("index", index)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.mine_activity_me_friend

    override fun initData() {

            index = intent.getIntExtra("index", 0)

    }

    override fun initView() {
        mTabTitleList.add("关注")
        mTabTitleList.add("粉丝")
        mFragmentList.add(AttentionFragment.getInstance())
        mFragmentList.add(FansFragment.getInstance())

        initMagicIndicator1()
        ivBack.setOnClickListener {
            finish()
        }

        if (index == 1) {
            mViewPager.currentItem = 1
        }
    }

    private var index = 0

    override fun start() {

    }

    private val mTabTitleList = ArrayList<String>()
    private val mFragmentList = ArrayList<Fragment>()

    private fun initMagicIndicator1() {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTabTitleList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //文字
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = (mTabTitleList[index])
                simplePagerTitleView.textSize = 16f
                simplePagerTitleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                simplePagerTitleView.normalColor = ContextCompat.getColor(context, R.color.gray28)
                simplePagerTitleView.selectedColor = ContextCompat.getColor(context, R.color.gray28)

                simplePagerTitleView.minScale = 0.9.toFloat()
                simplePagerTitleView.setOnClickListener {
                    if (mViewPager.currentItem != index) {
                        mViewPager.currentItem = index
                    }
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                //底部下划线指示器
                val indicator = LinePagerIndicator(context)
                indicator.setColors(ContextCompat.getColor(context, R.color.color_ffcc03))
                indicator.yOffset = 10.toFloat()
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT)
                return indicator
            }
        }
        magic_indicator.navigator = commonNavigator

    /*    mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                commonNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                commonNavigator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                commonNavigator.onPageScrollStateChanged(state)
            }
        })*/
        mViewPager.adapter =
            BaseFragmentAdapter(supportFragmentManager, mFragmentList)
        ViewPagerHelper.bind(magic_indicator, mViewPager)
    }

}
