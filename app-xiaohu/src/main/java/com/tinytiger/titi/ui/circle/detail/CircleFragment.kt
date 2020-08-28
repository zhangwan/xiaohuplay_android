package com.tinytiger.titi.ui.circle.detail


import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView

import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager

import com.google.android.material.appbar.AppBarLayout
import com.makeramen.roundedimageview.RoundedImageView
import com.orhanobut.logger.Logger
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.net.data.circle.CircleInfoBean
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.VolumeChangeObserver
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.home.CircleImageNetAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesInfoContract
import com.tinytiger.titi.mvp.presenter.circle.CirclesInfoPresenter
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.post.SendPostActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.game.listener.OnGameStatusListener
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.titi.adapter.post.BasePostNodeDataUtils
import com.tinytiger.titi.ui.web.WebActivity
import com.tinytiger.titi.utils.AutoPlayUtils
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.widget.AppBarStateChangeListener
import com.tinytiger.titi.widget.dialog.FullAnnouncementDialog
import com.tinytiger.titi.widget.video.MyJzVideoView
import com.tinytiger.titi.widget.view.AttentionView
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.circle_fragment_detail.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/5/9 0009 上午 11:37
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 圈子详情
 */
class CircleFragment : BaseFragment(), CirclesInfoContract.View,
    CircleContentFragment.OnCircleContentListener, NetStateChangeObserver,
    VolumeChangeObserver.OnVolumeChangeListener {


    private var circleId: String = ""
    private var circleName = ""
    private var modularId = ""
    private var circleLogo = ""
    private var gameId: String = ""
    private var topHeight = 430.0f
    private var applyAdminUrl = ""
    var ModularList = arrayListOf<CircleInfoBean.Data.tabModular>()
    private var mOnTitleStatusListener: OnGameStatusListener? = null
    private var mVolumeChangeObserver: VolumeChangeObserver? = null
    var circleNum = 0
    var circlePostNum = ""

    companion object {
        var circleFragment: CircleFragment? = null
        var postBean: PostBean? = null
        var mIndex = 0
        fun getInstance(circleId: String, modularId: String,
            listener: OnGameStatusListener): CircleFragment {
            val fragment = CircleFragment()
            fragment.arguments = Bundle()
            fragment.circleId = circleId
            fragment.modularId = modularId
            fragment.mOnTitleStatusListener = listener
            return fragment
        }

        fun getInstance(listener: OnGameStatusListener, game_id: String): CircleFragment {
            val fragment = CircleFragment()
            fragment.arguments = Bundle()
            fragment.gameId = game_id
            fragment.mOnTitleStatusListener = listener
            return fragment
        }
    }

    val mPresenter by lazy { CirclesInfoPresenter() }
    override fun getLayoutId(): Int = R.layout.circle_fragment_detail

    override fun initView() {
        circleFragment = this
        mPresenter.attachView(this)
        AutoPlayUtils.index = 0
        NetStateChangeReceiver.registerReceiver(activity)
        NetStateChangeReceiver.registerObserver(this)
        mVolumeChangeObserver = VolumeChangeObserver(getContext())
        mVolumeChangeObserver?.setOnVolumeChangeListener(this)
        mVolumeChangeObserver?.registerVolumeReceiver()
        app_bar_layout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onOffsetChanged(appBarLayout: AppBarLayout) {
                if (iv_top == null) {
                    return
                }
                iv_top.alpha = 1 - (topHeight + appBarLayout.top) / topHeight
                if (appBarLayout.bottom < 200) {
                    mOnTitleStatusListener?.onTitleShow(true)
                    rl_screen1.visibility = View.VISIBLE
                } else {
                    mOnTitleStatusListener?.onTitleShow(false)
                    rl_screen1.visibility = View.GONE
                }


            }

            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
            }
        })
        start()

        tvJoin.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                if (is_mutual == 1) {
                    TextDialog.create(childFragmentManager).setMessage("确定退出该圈子吗？")
                        .setViewListener(object : TextDialog.ViewListener {
                            override fun click() {
                                mPresenter.joinCircle(circleId, is_mutual)
                            }
                        }).show()
                } else {
                    mPresenter.joinCircle(circleId, is_mutual)
                }
            }
        }

        var lastx = 0f
        var lastY = 0f
        var time = System.currentTimeMillis()
        ivAdd.setOnTouchListener { _, event ->
            val x = event.rawX
            val y = event.rawY
            if (event.action == MotionEvent.ACTION_DOWN) {
                lastx = x
                lastY = y
                time = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                ivAdd.translationX = ivAdd.translationX + (x - lastx)
                ivAdd.translationY = ivAdd.translationY + (y - lastY)
                lastx = x
                lastY = y
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - time < 300) {
                    SendPostActivity.actionStart(activity!!, circleId, circleName)
                }
            }
            true
        }
        initListener()
    }

    fun initListener() {
        tv_game.setOnClickListener {
            GameDetailActivity.actionStart(activity!!, gameId, 6)
        }
        tvApply.setOnClickListener {
            if (isLoginStart()) {
                WebActivity.actionStart(activity!!, "$applyAdminUrl?circle_id=$circleId")
            }

        }
    }

    private val titles = ArrayList<String>()
    private val mFragmentList = ArrayList<Fragment>()
    private fun initMagicIndicator(magic_indicator: MagicIndicator) {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.isAdjustMode = false
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val titleView = Indicator.mTitleView(context, titles[index], 1f)
                titleView.setOnClickListener {
                    BasePostNodeDataUtils.isLooper = true
                    AutoPlayUtils.index = index
                    viewPager.currentItem = index
                    mIndex = index

                }
                return titleView
            }

            override fun getIndicator(context: Context): LinePagerIndicator {
                return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
            }
        }

        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                BasePostNodeDataUtils.isLooper = true
                AutoPlayUtils.index = position
                mIndex = position
                if (activity is CirclesDetailsActivity) {
                    (activity as CirclesDetailsActivity).setSwipeBackEnable(position == 0)
                }
            }
        })
    }


    override fun start() {
        mPresenter.getGameCircleInfo(gameId, circleId)
    }

    var is_join = 0
    var background=""
    override fun showGameCircleInfo(bean: CircleInfoBean) {
        circleId = bean.data.circle_info.id
        circleName = bean.data.circle_info.name
        circleLogo = bean.data.circle_info.logo
        background = bean.data.circle_info.background
        applyAdminUrl = bean.data.circle_info.apply_admin_url
        if (!TextUtils.isEmpty(bean.data.game_info.id)) {
            gameId = bean.data.game_info.id
        }

        if (!TextUtils.isEmpty(gameId)) {
            tv_game.visibility = View.VISIBLE
        } else {
            tv_game.visibility = View.GONE
        }

        GlideUtil.loadImg(iv_avatar, bean.data.circle_info.logo)

        if (bean.data.game_info.name.length > 15) {
            tv_name.textSize = 15.toFloat()
        }
        tv_name.text = bean.data.game_info.name

        if (tv_name.lineCount > 1) {
            tvDesc1.text = bean.data.game_info.one_introduce
            tvDesc1.visibility = View.VISIBLE
        } else {
            tvDesc.text = bean.data.game_info.one_introduce
            tvDesc1.visibility = View.GONE
        }

        shareTitle = "${tv_name.text}--小虎Hoo玩好游戏"
        circleNum = bean.data.circle_info.add_circle_num
        var circleNumber = StringUtils.sizeToString(bean.data.circle_info.add_circle_num)
        circlePostNum = StringUtils.sizeToString(bean.data.circle_info.circle_post_num)
        var text = "${circleNumber}人已加入      ${circlePostNum}条内容"
        setJoinText(text)

        shareImage = bean.data.game_info.logo
        shareUrl = bean.data.circle_info.share_url + "?circle_id=${circleId}"
        is_join = bean.data.circle_info.is_join
        tvJoin.setJoin(is_join)

        if ("1" == bean.data.circle_info.is_opened_admin && bean.data.circle_info.apply_status == "1") {
            tvApply.visibility = View.VISIBLE
        } else {
            tvApply.visibility = View.INVISIBLE
        }

        banner.visibility = View.GONE
        llHorn.visibility = View.GONE
        llTop.visibility = View.GONE
        rl_screen.visibility = View.GONE
        for (i in bean.data.tab_modular) {
            when (i.is_type) {
                1 -> {
                    if (i.is_show == 1 && bean.data.banner != null && bean.data.banner.size > 0) {
                        banner.visibility = View.VISIBLE
                        //只有一张banner图则拷贝一份，方便展示
                        if (bean.data.banner.size == 1) bean.data.banner.addAll(bean.data.banner)
                        banner.adapter = CircleImageNetAdapter(bean.data.banner)
                        //item左右展示的宽度
                        val itemWidth = ((Dp2PxUtils.px2dip(activity,
                            ScreenUtil.getScreenWidth().toFloat()) - 210) / 2)
                        banner.setBannerGalleryEffect(itemWidth, 15)

                    }
                }
                2 -> {
                    if (i.is_show == 1 && bean.data.notice != null) {
                        llHorn.visibility = View.VISIBLE
                        tvHorn.text = bean.data.notice.content
                        tvHorn.setOnClickListener {
                            FullAnnouncementDialog().apply {
                                content = bean.data.notice.content
                            }.show(childFragmentManager, "dialog")
                        }
                    }
                }
                3 -> {
                    if (i.is_show == 1 && bean.data.top_post_list != null && bean.data.top_post_list.size > 0) {
                        llTop.visibility = View.VISIBLE
                        tvTopDes1.text = bean.data.top_post_list[0].content
                        llTop_1.setOnClickListener {
                            PostActivity.actionStart(activity!!, bean.data.top_post_list[0].id)
                        }
                        if (bean.data.top_post_list.size > 1) {
                            tvTopDes2.text = bean.data.top_post_list[1].content
                            llTop_2.setOnClickListener {
                                PostActivity.actionStart(activity!!, bean.data.top_post_list[1].id)
                            }
                        } else {
                            vLine.visibility = View.GONE
                            llTop_2.visibility = View.GONE
                        }
                    }
                }
                4 -> {
                    AutoPlayUtils.index = 0
                    var currentIndex = -1
                    if (i.is_show == 1 && bean.data.tag_modular != null && bean.data.tag_modular.size > 0) {
                        ModularList = bean.data.tag_modular
                        rl_screen.visibility = View.VISIBLE
                        for (index in 0 until bean.data.tag_modular.size) {

                            AutoPlayUtils.playerMap[index] = -1

                            Logger.d(bean.data.tag_modular[index].name)
                            if (modularId == bean.data.tag_modular[index].id) {
                                currentIndex = index
                                AutoPlayUtils.index = currentIndex
                            }
                            titles.add(bean.data.tag_modular[index].name)
                            val fragment = CircleContentFragment.getInstance(circleId,
                                bean.data.tag_modular[index].id,
                                bean.data.tag_modular[index].tag_type, index)
                            fragment.setCircleContentListener(this)
                            mFragmentList.add(fragment)
                        }

                        initMagicIndicator(magic_indicator)
                        initMagicIndicator(magic_indicator1)
                        viewPager.adapter = TabAdapter(childFragmentManager, mFragmentList)
                        if (currentIndex >= 0) {
                            viewPager.currentItem = currentIndex
                        } else if (modularId.isNotEmpty() && modularId != "0") {
                            showToast("该模块不存在")
                        }

                    }
                }
            }
        }

        if (bean.data.circle_admin_list != null && bean.data.circle_admin_list.size > 0) {
            llCircleSize.visibility = View.VISIBLE
            val dp5 = Dp2PxUtils.dip2px(context, 5)
            for (i in bean.data.circle_admin_list) {
                val imgCur = RoundedImageView(context)
                imgCur.isOval = true
                imgCur.layoutParams = ViewGroup.LayoutParams(dp5 * 5, dp5 * 5)
                imgCur.setPadding(dp5)
                imgCur.setPadding(0, dp5, dp5, 0)
                imgCur.scaleType = ImageView.ScaleType.CENTER_CROP
                imgCur.setOnClickListener {
                    HomepageActivity.actionStart(activity!!, i.user_id)
                }

                llCircleSize.addView(imgCur)
                GlideUtil.loadImg(imgCur, i.avatar)
            }
        }
    }

    override fun showCircleList(bean: PostInfoBean) {

    }

    private fun setJoinText(text: String) {
        var spannableStr = SpannableString(text)
        var absoluteSizeSpan = AbsoluteSizeSpan(22, true)
        var absoluteSizeSpan1 = AbsoluteSizeSpan(22, true)
        spannableStr.setSpan(absoluteSizeSpan, 0, circleNum.toString().length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableStr.setSpan(absoluteSizeSpan1, circleNum.toString().length + 4, text.length - 3,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tvRead.text = spannableStr
    }

    override fun showJoin(join: Int) {
        if (join == 1) {
            CircleAgentUtils.setCircleOn(circleId)
        }
        var text = ""
        //加入
        text = if (join == 1) {
            circleNum += 1
            "${circleNum}人已加入      ${circlePostNum}条内容"
        } else {
            circleNum -= 1
            "${circleNum}人已加入      ${circlePostNum}条内容"
        }
        setJoinText(text)

        val bean = CircleBean()
        bean.is_join = join
        bean.name = circleName
        bean.id = circleId
        bean.logo = circleLogo
        bean.background=background
        EventBus.getDefault().post(bean)
        tvJoin.setJoin(join)
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == 10000) {
            if (activity is CirclesDetailsActivity) {
                (activity as CirclesDetailsActivity).finish()
            }
        }
    }

    fun setBlurryBG(bit: Bitmap) {
        if (context == null) {
            return
        }
        Blurry.with(context).radius(10).sampling(8).async().from(bit).into(iv_top)
    }

    var shareUrl = ""
    var shareImage = ""
    var shareTitle = ""
    fun share() {
        ShareDialog.create(childFragmentManager).apply {
            class_name = "Circle"
            share_url = shareUrl
            share_title = shareTitle
            share_desc = "加入小虎Hoo圈子和TA一起玩转小虎圈吧~"
            share_image = shareImage
        }.show()
    }

    override fun onContentChangeListener(item: PostBean) {
        if (ModularList.size == 0) {
            return
        }
        if (TextUtils.isEmpty(item.modular_id)) {
            return
        }
        if (item.circle_id == circleId) {
            var currentIndex = -1
            for (index in 0 until ModularList.size) {
                if (item.modular_id == ModularList[index].id) {
                    currentIndex = index
                    if (currentIndex == viewPager.currentItem) {
                        return
                    }

                    viewPager.currentItem = currentIndex
                    return
                }
            }
            if (currentIndex == -1) {
                showToast("该模块不存在")
            }
        } else {
            CirclesDetailsActivity.actionStart(context!!, item.circle_id, item.modular_id, "")
            activity?.finish()
        }
    }

    fun refreshPostEvent(item: PostBean) {
        item.status = "0"
        postBean = item
        var currentIndex = -1
        if (item.modular_id.isEmpty()) {
            viewPager.currentItem = 0
            return
        }
        for (index in 0 until ModularList.size) {
            if (item.modular_id == ModularList[index].id) {
                currentIndex = index
                if (currentIndex == viewPager.currentItem) {
                    return
                }

                viewPager.currentItem = currentIndex
                return
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        circleFragment = null
        NetStateChangeReceiver.unRegisterObserver(this)
        NetStateChangeReceiver.unRegisterReceiver(activity)
        mVolumeChangeObserver?.unregisterVolumeReceiver()
    }

    override fun onNetDisconnected() {

    }

    override fun onNetConnected(networkType: NetworkType?) {
        var type = 2
        if (networkType == NetworkType.NETWORK_NO) {
            type = 0
        } else if (networkType == NetworkType.NETWORK_WIFI) {
            type = 1
        }
        if (viewPager != null) {
            var index = viewPager.currentItem
            var fragment = mFragmentList[index] as CircleContentFragment
            if (fragment != null) {
                var layoutManager = fragment.recyclerView?.layoutManager as LinearLayoutManager?
                var positionInList =
                    if (AutoPlayUtils.playerMap[AutoPlayUtils.index] != null) AutoPlayUtils.playerMap[AutoPlayUtils.index]!! else 0
                if (layoutManager != null) {
                    var parentView: View? =
                        layoutManager!!.findViewByPosition(positionInList) ?: return
                    parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)?.setType(type)
                }

            }
        }

    }

    override fun onVolumeChange(volume: Int) {
        if (viewPager != null) {
            val index = viewPager.currentItem
            if (index >= mFragmentList.size) {
                return
            }
            val fragment = mFragmentList[index] as CircleContentFragment

            val layoutManager = fragment.recyclerView?.layoutManager as LinearLayoutManager?
            val positionInList =
                if (AutoPlayUtils.playerMap[AutoPlayUtils.index] != null) AutoPlayUtils.playerMap[AutoPlayUtils.index]!! else 0
            if (layoutManager != null) {
                try {
                    val parentView: View? =
                        layoutManager.findViewByPosition(positionInList) ?: return
                    parentView?.findViewById<MyJzVideoView>(R.id.jzVideoView)
                        ?.setVolumeChange(volume)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

    }

}