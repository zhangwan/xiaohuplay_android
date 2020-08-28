package com.tinytiger.titi.ui.circle.detail


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.netease.nim.uikit.common.framework.infra.TaskExecutor
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseApp

import com.tinytiger.common.basis.BasisActivity
import com.tinytiger.common.net.MyNetworkUtil

import com.tinytiger.common.net.data.circle.CircleNavigationBean

import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.image.GlideUtil

import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.MyCommonNavigatorAdapter

import com.tinytiger.titi.ui.circle.detail.vp.CirclesDetailsPresenter
import com.tinytiger.titi.ui.game.info.GameAddWikiFragment
import com.tinytiger.titi.ui.game.info.GameContentFragment
import com.tinytiger.titi.ui.game.info.GameWikiBrightFragment
import com.tinytiger.titi.ui.game.info.GameWikiFragment
import com.tinytiger.titi.ui.game.listener.OnGameStatusListener
import com.tinytiger.titi.ui.search.GameWikiSearchActivity
import com.tinytiger.titi.ui.search.SearchPostActivity
import kotlinx.android.synthetic.main.activity_circles_details.*

import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * @author zwy
 * create at 2020/6/28 0028
 * description:新的圈子详情页
 */
class CirclesDetailsActivity : BasisActivity<CirclesDetailsPresenter>(), OnGameStatusListener {
    private var mCircleFragment: CircleFragment? = null
    private var mMyGameWikiFragment: GameWikiFragment? = null
    private var mGameAddWikiFragment: GameAddWikiFragment? = null
    private var mGameWikiBrightFragment: GameWikiBrightFragment? = null
    private var mMyGameContentFragment: GameContentFragment? = null
    private val mFragmentList = ArrayList<Fragment>()
    private val titles = ArrayList<String>()
    var bgBitmap: Bitmap? = null
    private val executor = TaskExecutor("image", TaskExecutor.defaultConfig, true)
    var modularId = ""
    var circleId = ""
    var game_id = ""
    //0-详情页面 1-百科页面 2-咨询页面
    var index = 0
    //分享链接
    var shareUrl = ""
    //logo
    var logo = ""
    //名称
    var name = ""

    //"circle","" 圈子 "wiki" 百科 "news" 咨询
    var pageIndex: String? = null


    /**
     *  管理员wifi权限
     */
    var wiki_type = 0
    private val mTitleAdapter by lazy { MyCommonNavigatorAdapter(this, ArrayList()) }
    private var mCircleNavigationBean: CircleNavigationBean? = null


    companion object {
        //跳转页面码
        fun actionStart(context: Context, circleId: String, modularId: String, index: String) {
            actionStart(context, circleId, "", modularId, index)
        }

        fun actionStart(context: Context, circleId: String, gameId: String, modularId: String,
            index: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, CirclesDetailsActivity::class.java)
            intent.putExtra("circleId", circleId)
            intent.putExtra("modularId", modularId)
            intent.putExtra("pageIndex", index)
            intent.putExtra("gameId", gameId)
            context.startActivity(intent)
        }

        fun actionStart(context: Context, gameId: String, index: String) {
            actionStart(context, "", gameId, "", index)
        }
    }

    override fun layoutId(): Int = R.layout.activity_circles_details

    override fun initData() {
        useStatusBarColor = false
        setWindowFeature()

        if (intent.hasExtra("circleId")) {
            circleId = intent.getStringExtra("circleId")
        }
        if (intent.hasExtra("modularId")) {
            modularId = intent.getStringExtra("modularId")
        }
        pageIndex = intent.getStringExtra("pageIndex")

        if (intent.hasExtra("gameId")) {
            game_id = intent.getStringExtra("gameId")
        }
        index = when (pageIndex) {
            ConstantsUtils.Page.PAGE_WIKI -> {
                1
            }
            ConstantsUtils.Page.PAGE_NEWS -> {
                2
            }
            else -> {
                0
            }
        }
        setIsOnlyTrackingLeftEdge(true)
    }

    override fun initView() {
        basePresenter?.getCircleNavigation(game_id, circleId)
    }

    fun showGameNavigation(bean: CircleNavigationBean) {
        mCircleNavigationBean = bean
        circleId = mCircleNavigationBean?.cirlce?.id!!
        game_id = mCircleNavigationBean?.cirlce?.game_id!!
        logo = mCircleNavigationBean?.cirlce?.logo!!
        name = mCircleNavigationBean?.cirlce?.name!!
        wiki_type = mCircleNavigationBean?.cirlce?.apply_status!!
        if (!TextUtils.isEmpty(mCircleNavigationBean?.share?.circle_url)) {
            shareUrl = mCircleNavigationBean?.share?.circle_url!!
        }
        initMagicIndicator()
        initListener()

    }

    fun initMagicIndicator() {
        if (pageIndex == ConstantsUtils.Page.PAGE_WIKI && mCircleNavigationBean?.nav?.wiki == "0") {
            showToast("百科已关闭")
            this.finish()
        }
        if (pageIndex == ConstantsUtils.Page.PAGE_NEWS && mCircleNavigationBean?.nav?.information == "0") {
            showToast("咨询已关闭")
            this.finish()
        }


        titles.add("圈子")
        if (mCircleNavigationBean?.nav?.circle == "1") {
            mCircleFragment = CircleFragment.getInstance(circleId, modularId, this)
            mFragmentList.add(mCircleFragment!!)
        }

        if (mCircleNavigationBean?.nav?.wiki == "1") {
            when (mCircleNavigationBean?.cirlce?.wiki_modular) {
                "1" -> {
                    titles.add("百科")
                    mMyGameWikiFragment = GameWikiFragment.getInstance(game_id, logo, name)
                    mFragmentList.add(mMyGameWikiFragment!!)
                }
                "2" -> {
                    titles.add("百科")
                    mGameAddWikiFragment = GameAddWikiFragment.getInstance(game_id, logo, name)
                    mFragmentList.add(mGameAddWikiFragment!!)
                }
                "3" -> {
                    titles.add("百科")
                    mGameWikiBrightFragment =
                        GameWikiBrightFragment.getInstance(game_id, logo, name)
                    mFragmentList.add(mGameWikiBrightFragment!!)
                }
            }

        }

        if (mCircleNavigationBean?.nav?.information == "1") {
            titles.add("资讯")
            mMyGameContentFragment = GameContentFragment.getInstance(game_id, logo, name, this)
            mFragmentList.add(mMyGameContentFragment!!)
        }

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = false
        mTitleAdapter.titles = titles
        commonNavigator.adapter = mTitleAdapter
        magic_indicator.navigator = commonNavigator
        initAdapterCallBack()
        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                onTitleShow(false)
                setSwipeBackEnable(position == 0)
                hideSearchBtn(position)
                hideShareBtn(position)
                if (position == 2 && bgBitmap != null) {
                    mCircleFragment?.setBlurryBG(bgBitmap!!)
                    mMyGameContentFragment?.setBlurryBG(bgBitmap!!)
                }
                when (position) {
                    0 -> {
                        shareUrl = mCircleNavigationBean?.share?.circle_url!!
                    }
                    1 -> {
                        shareUrl = mCircleNavigationBean?.share?.wiki_url!!
                    }
                    2 -> {
                        shareUrl = mCircleNavigationBean?.share?.information_url!!
                    }
                }
            }
        })

        ViewPagerHelper.bind(magic_indicator, mViewPager)

        if (index > mFragmentList.size) {
            index = 0
        }

        mViewPager.setCurrentItem(index, false)
        GlideUtil.loadImg(iv_top_layout, mCircleNavigationBean?.cirlce?.background)

        mHandler.postDelayed({
            setRunBG(mCircleNavigationBean?.cirlce?.background)
        }, 400)

    }

    private fun initAdapterCallBack() {
        mTitleAdapter.titleListener = {
            mViewPager.currentItem = it
            hideSearchBtn(it)
            hideShareBtn(it)
        }
    }

    fun initListener() {
        iv_back.setOnClickListener {
            this.finish()
        }
        iv_search.setOnClickListener {
            if (mViewPager.currentItem == 0) {
                SearchPostActivity().actionStart(this, circleId)
            } else if (mViewPager.currentItem == 1) {
                GameWikiSearchActivity.actionStart(this, game_id)
            }
        }
        iv_share.setOnClickListener {
            if (mCircleNavigationBean == null) {
                return@setOnClickListener
            }

            ShareDialog.create(supportFragmentManager).apply {
                class_name = "Circle"
                share_url =
                    "${shareUrl}?circle_id=${mCircleNavigationBean?.cirlce?.id!!}&game_id=${game_id}"
                share_title = mCircleNavigationBean?.cirlce?.name!!
                share_desc = "加入小虎Hoo圈子和TA一起玩转小虎圈吧~"
                share_image = mCircleNavigationBean?.cirlce?.logo!!
                contentId = mCircleNavigationBean?.cirlce?.game_id!!
                id = mCircleNavigationBean?.cirlce?.id!!
            }.show()


        }
    }

    fun hideSearchBtn(index: Int) {
        if (index == 2) {
            iv_search.visibility = View.GONE
        } else {
            iv_search.visibility = View.VISIBLE
        }
    }

    fun hideShareBtn(index: Int) {
        if (index == 1 && (mGameAddWikiFragment != null || mGameWikiBrightFragment != null)) {
            iv_share.visibility = View.GONE
        } else {
            iv_share.visibility = View.VISIBLE
        }
    }


    private fun setRunBG(url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        executor.execute {
            try {
                bgBitmap =
                    Glide.with(this).asBitmap().error(R.mipmap.ic_launcher).override(200, 100)
                        .load(url).submit().get(3000, TimeUnit.MILLISECONDS)
                mCircleFragment?.setBlurryBG(bgBitmap!!)
                mMyGameWikiFragment?.setBlurryBG(bgBitmap!!)
                mMyGameContentFragment?.setBlurryBG(bgBitmap!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onTitleShow(isShow: Boolean) {

    }

    override fun showAttention(follow_status: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 99) {
            if (data == null) {
                return
            }

            wiki_type = data.getIntExtra("wiki_type", 0)
        }
    }


}