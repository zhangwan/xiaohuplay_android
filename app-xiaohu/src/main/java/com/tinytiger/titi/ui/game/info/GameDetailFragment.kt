package com.tinytiger.titi.ui.game.info


import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.google.android.material.appbar.AppBarLayout
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil.Companion.isNetworkAvailable
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.net.data.gametools.GameCommentBean
import com.tinytiger.common.net.data.gametools.GameCommentList
import com.tinytiger.common.net.data.gametools.GameInfoDetailBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.picbrowser.ImagePreviewLoader
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.GameCommentAdapter
import com.tinytiger.titi.adapter.gametools.info.MultiGameDetailAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.data.game.MultiGameDetailBean
import com.tinytiger.titi.mvp.contract.gametools.GameDetailContract
import com.tinytiger.titi.mvp.presenter.gametools.GameDetailPresenter
import com.tinytiger.titi.ui.game.listener.OnGameStatusListener
import com.tinytiger.titi.ui.video.VideoActivity
import com.tinytiger.titi.ui.yungame.YunGameActivity
import com.tinytiger.titi.widget.AppBarStateChangeListener
import com.tinytiger.titi.widget.dialog.EvaluationDialog
import com.tinytiger.titi.widget.dialog.GameScreenDialog
import com.tinytiger.titi.widget.dialog.OnAmwayListener
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import com.xwdz.download.notify.DataUpdatedWatcher
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.my_game_fragment_detail.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus
import java.util.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 游戏详情 Fragment 游戏信息页
*/
class GameDetailFragment : BaseFragment(), GameDetailContract.View {


    private var game_id: String = "0"

    private var topHeight = 430.0f
    private var mOnTitleStatusListener: OnGameStatusListener? = null
    private val mFragmentContainerHelper = FragmentContainerHelper()
    private val mData = ArrayList<MultiGameDetailBean>()

    val mMultiAdapter by lazy {
        MultiGameDetailAdapter(mData)
    }
    private val mAdapter by lazy { GameCommentAdapter() }
    private var user_id: String = "0"

    private var selectStars: String = ""

    private var type: Int = 1 //1 = 最新 2= 最热

    private var mGameInfoDetailBean: GameInfoDetailBean.Data? = null

    companion object {

        fun getInstance(
            game_id: String, bean: GameInfoDetailBean.Data?,
            listener: OnGameStatusListener
        ): GameDetailFragment {
            val fragment = GameDetailFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mGameInfoDetailBean = bean
            fragment.game_id = game_id
            fragment.mOnTitleStatusListener = listener
            return fragment
        }
    }

    private val mPresenter by lazy { GameDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.my_game_fragment_detail


    override fun initView() {
        if (ScreenUtil.checkDeviceHasNavigationBar(activity)) {
            ll_bottom.setPadding(
                0, 0, Dp2PxUtils.dip2px(activity, 15),
                ScreenUtil.getNavigationBarHeight(activity)
            )
        }

        mPresenter.attachView(this)
        initRecyclerView()
        initMagicIndicator(magic_indicator)
        initMagicIndicator(magic_indicator1)

        if ((activity as GameDetailActivity).pageType == "1001") {
            showRatingDialog()
        }
        fab_add.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            showRatingDialog()
        }
        fab_add1.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            showRatingDialog()
        }
        fab_subscribe.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            mPresenter.addAppointment(game_id)
        }

        tv_screen.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            showScreenDialog()
        }
        tv_screen1.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            showScreenDialog()
        }

        tvAttention.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            if (!isNetworkAvailable(getContext()!!)) {
                ToastUtils.show(getContext(), "当前网络不可用")
                return@setOnClickListener
            }
            mPresenter.GameFollow(game_id, isFollow)
        }

        mAdapter.game_id = game_id

        if (mGameInfoDetailBean != null) {
            showGameInfoData(mGameInfoDetailBean!!)
            user_id = SpUtils.getString(R.string.user_id, "")
            start()
        }
        fl_drag.setOnClickChildListener {
            YunGameActivity.actionStart(activity!!, game_id, gamePackage)
        }
    }

    private fun initMagicIndicator(magic_indicator: MagicIndicator) {
        val titles = arrayOf("最新", "  最热")
        val commonNavigator = CommonNavigator(context)
        commonNavigator.isAdjustMode = false
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val titleView = Indicator.mTitleView(context, titles[index], 1f)
                titleView.setOnClickListener {
                    mFragmentContainerHelper.handlePageSelected(index)
                    type = index + 1
                    start()
                }
                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
            }
        }

        magic_indicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(magic_indicator)
    }

    private fun initRecyclerView() {
        rv_comment.adapter = mAdapter
        rv_comment.layoutManager = LinearLayoutManager(context)
        rv_comment.setHasFixedSize(true)

        mAdapter.listener = object : GameCommentAdapter.OnGameCommentListener {
            override fun onShare(mBean: GameCommentBean) {
                if (mGameInfoDetailBean == null) {
                    return
                }

                ShareDialog.create(childFragmentManager).apply {
                    class_name = "no"
                    share_url =
                        mGameInfoDetailBean!!.comment_details + "?game_id=" + game_id + "&assess_id=" + mBean.id
                    share_title = mBean.nickname
                    share_desc = mBean.title
                    share_image = mBean.avatar
                }.show()
            }

            override fun onLike(mBean: GameCommentBean) {
                mPresenter.likeAssessOrTag(game_id, mBean.id, mBean.is_like)
            }

            override fun onAttention(position: Int, mBean: GameCommentBean) {
                mPresenter.doFollow(
                    mBean.user_id, mBean.user_follow_status

                )
            }
        }

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getGameInfoCommentList(game_id, user_id, selectStars, type, page)
        }

        recycler_view.adapter = mMultiAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
        mMultiAdapter.mOnGameDetailItemListener =
            object : MultiGameDetailAdapter.OnGameDetailItemListener {
                override fun showIntroItem(position: Int) {
                    showIntroItemImg(position)
                }

                override fun showTakeScore() {
                    if (FastClickUtil.isFastClickTiming()) {
                        return
                    }
                    showRatingDialog()
                }

                override fun setGameFollow(follow_status: Int) {
                    (activity as GameDetailActivity).showAttention(follow_status)
                }
            }


        app_bar_layout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onOffsetChanged(appBarLayout: AppBarLayout) {
                if (iv_top == null) {
                    return
                }

                iv_top.alpha = 1 - (topHeight + appBarLayout.top) / topHeight
                if (appBarLayout.bottom < 200) {
                    mOnTitleStatusListener?.onTitleShow(true)
                    rl_screen.visibility = View.INVISIBLE
                    rl_screen1.visibility = View.VISIBLE
                } else {
                    mOnTitleStatusListener?.onTitleShow(false)
                    rl_screen.visibility = View.VISIBLE
                    rl_screen1.visibility = View.GONE
                }
            }

            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
            }
        })

        mMultiAdapter.width = resources.displayMetrics.widthPixels
    }

    var mEvaluationDialog: EvaluationDialog? = null

    /**
     * 评分
     */
    private fun showRatingDialog() {
        if (MyUserData.isEmptyToken()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
        if (mGameInfoDetailBean == null) {
            return
        }

        if (mEvaluationDialog != null) {
            mEvaluationDialog!!.dismiss()
            mEvaluationDialog = null
        }


        val url =
            mGameInfoDetailBean!!.review + "${if (mGameInfoDetailBean!!.review.contains("?")) "&" else "?"}token=${SpUtils.getString(
                R.string.token,
                ""
            )}&game_id=$game_id"

        Logger.d(url)
        mEvaluationDialog = EvaluationDialog.create(childFragmentManager)
            .setGameListener(game_id, url, OnAmwayListener {
                showToast("点评成功")
                start()
                for (i in 0..(mMultiAdapter.data.size - 1)) {
                    if (mMultiAdapter.data[i].assessInfo != null) {
                        mMultiAdapter.data[i].assessInfo.total += 1
                        mMultiAdapter.notifyItemChanged(i)
                    }
                }
            }).show()
    }

    private fun showScreenDialog() {
        GameScreenDialog.create(childFragmentManager).setStars(selectStars)
            .setViewListener(object : GameScreenDialog.ViewListener {
                override fun click(string: String) {
                    selectStars = string
                    start()
                }
            }).show()
    }

    override fun start() {
        page = 1
        mPresenter.getGameInfoCommentList(game_id, user_id, selectStars, type, page)
    }


    override fun onResume() {
        super.onResume()
        if (mAdapter.isRefresh) {
            start()
            mAdapter.isRefresh = false
        }
        QuietDownloader.addObserver(watcher)

        dpvProgress.newinstallApk()
    }

    override fun onDestroy() {
        super.onDestroy()
        QuietDownloader.removeObserver(watcher)
    }

    private var gamePackage = ""
    private var subscriberUrl = ""
    override fun showGameInfoData(bean: GameInfoDetailBean.Data) {
        //TYPE_LEVEL_TITLE = 1 标题
        mData.add(MultiGameDetailBean(bean))

        // 植株图
        if (bean.game_info.radar_chart_status == 1 && !bean.game_info.radar_chart_url.isNullOrEmpty()) {
            val url = bean.game_info.radar_chart_url + "?game_id=" + bean.game_info.id
            mData.add(MultiGameDetailBean(url, 6))
        }

        if (bean.assess_info != null && bean.assess_info.show_score == 1) {
            // TYPE_LEVEL_RATE = 5 评分
            mData.add(MultiGameDetailBean(bean.assess_info))
        }
        //  TYPE_LEVEL_INTRODUCTION = 3 游戏简介
        mData.add(MultiGameDetailBean(bean.game_info, 3))

        //  TYPE_LEVEL_VIDEO = 4 视频
        if (bean.recommended_video != null && bean.recommended_video.size > 0) {
            mData.add(MultiGameDetailBean(bean.recommended_video))
        }

        // TYPE_LEVEL_WORD_CLOUD = 2 词云
        if (bean.show_viewpoint_list == 1 && bean.tag_cloud_url != null && bean.viewpoint_list != null && bean.viewpoint_list.size > 14) {
            val url = bean.tag_cloud_url + "?list=" + JSON.toJSON(bean.viewpoint_list)
            mData.add(MultiGameDetailBean(url, 2))

        }

        // 圈子信息
        if (bean.circle_info != null) {
            mData.add(MultiGameDetailBean(bean.circle_info))
        }

        // 游戏信息
        if (bean.game_info != null && (!bean.game_info.version.isNullOrBlank()
                    && !bean.game_info.package_size_android.isNullOrBlank()
                    && !bean.game_info.package_update_time.isNullOrBlank())
        ) {
            mData.add(MultiGameDetailBean(bean.game_info, 8))
        }

        mMultiAdapter.setNewInstance(mData)

       // bean.game_info.package_type=2
        if (!bean.game_info.download_url.isNullOrEmpty() && bean.game_info.package_type != 3) {
            val item = MineGameBean()
            item.id = game_id
            item.name = bean.game_info.name
            item.download_url = bean.game_info.download_url
            item.package_size_android = bean.game_info.package_size_android
            item.package_name_android = bean.game_info.package_name_android
            item.version = bean.game_info.version
            item.logo = bean.game_info.logo
            dpvProgress.setPackageUi(1)
            dpvProgress.setDownloadUrl(item)

        } else {
            fab_subscribe.visibility = View.VISIBLE
            dpvProgress.visibility = View.GONE
            fab_add.visibility = View.GONE
            fab_add1.visibility = View.VISIBLE
        }
        showGameFollow(bean.game_info.follow_status, 0)

        if (bean.game_info.is_cloud_game == 1) {
            gamePackage = bean.game_info.package_name_android
            tvYunGame.visibility = View.VISIBLE
        } else {
            tvYunGame.visibility = View.GONE
        }

        showSubscribeStatus(bean.game_info.appointment == 1)
        subscriberUrl = bean.game_info.radar_chart_url
    }

    /**
     * 底部评论列表
     */
    override fun showGameInfoCommentList(bean: GameCommentList.Data) {
        mAdapter.removeAllFooterView()
        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
                rv_comment.scrollState
            } else {
                mAdapter.setNewInstance(bean.data)
            }
        } else {
            mAdapter.addData(bean.data)
        }
        refreshLayout.finishLoadMore()

        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            if (bean.total > 0) {
                mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
            }
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
    }


    override fun showFollowStatus(is_follow: Int, user_id: String) {
        if (user_id.isNotEmpty()) {
            for (user in mAdapter.data) {
                if (user_id == user.user_id) {
                    user.user_follow_status = is_follow
                }
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun addAppointSuccess() {
        showSubscribeStatus(true)
//        TextDialog.create(activity!!.supportFragmentManager)
//            .apply {
//                title = "账号预约成功"
//                cancelText = "暂不开启"
//                confirmText = "去开启"
//                showTitle = true
//                titleColor = R.color.color34
//                messageColor = R.color.color66
//            }
//            .setMessage("是否需要开启微信提醒？")
//            .setViewListener(object : TextDialog.ViewListener {
//                override fun click() {
//                    if (MyUserData.isEmptyToken()) {
//                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
//                        return
//                    }
//                    WebActivity.actionStart(activity!!, subscriberUrl)
//                }
//            }).show()
    }

    /**
     * 预约按钮的状态
     * @param isSubscribered true-预约成功 false-未预约
     */
    private fun showSubscribeStatus(isSubscribered: Boolean) {
        fab_subscribe.isEnabled = !isSubscribered
        if (isSubscribered) {
            fab_subscribe.setBackgroundResource(R.drawable.solid_rectangle_5_d8d8d8)
            tv_subscribe.text = "预约成功"
            tv_subscribe.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else {
            fab_subscribe.setBackgroundResource(R.drawable.solid_gradient_3_36e0a4)
            tv_subscribe.text = "预约"
            tv_subscribe.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(activity!!, R.mipmap.ic_game_time), null, null, null
            )
        }
    }


    override fun showlikeAssess(position: Int, message: String, is_like: Int) {
    }

    var isFollow = 0
    override fun showGameFollow(is_follow: Int, currentItem: Int) {
        isFollow = is_follow
        if (is_follow == 1) {
            tvAttention.text = "已收藏"
            tvAttention.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            tvAttention.background =
                ContextCompat.getDrawable(activity!!, R.drawable.solid_rectangle_5_dddddd)
            tvAttention.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else {
            tvAttention.text = "收藏"
            tvAttention.setTextColor(ContextCompat.getColor(activity!!, R.color.gray33))
            tvAttention.background =
                ContextCompat.getDrawable(activity!!, R.drawable.stroke_rectangle_5_979797)
            tvAttention.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(activity!!, R.mipmap.icon_game_star), null, null, null
            )
        }
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    fun setBlurryBG(bit: Bitmap) {
        Blurry.with(context).radius(10).sampling(8).async().from(bit).into(iv_top)
    }


    /**
     * 预览图片
     */
    fun showIntroItemImg(position: Int) {
        if (FastClickUtil.isFastClick(800)) {
            return
        }
        val hasVideo = mGameInfoDetailBean!!.game_info!!.video_introduce.isNotEmpty()
        if (hasVideo) {
            if (position == 0) {
                VideoActivity.actionStart(
                    context!!,
                    mGameInfoDetailBean!!.game_info.video_introduce
                )
            } else {
                val urls = ArrayList<String>()
                for (item in 0 until mGameInfoDetailBean!!.game_info!!.images_introduce.size) {
                    if (item != 0) {
                        urls.add(mGameInfoDetailBean!!.game_info!!.images_introduce[item])
                    }
                }
                ImagePreviewLoader.showImagePreview(activity, position - 1, urls)
            }
        } else {
            val urls = ArrayList<String>()
            for (item in mGameInfoDetailBean!!.game_info!!.images_introduce) {
                urls.add(item)
            }

            ImagePreviewLoader.showImagePreview(activity, position, urls)
        }
    }

    private val watcher = object : DataUpdatedWatcher() {
        override fun notifyUpdate(data: DownloadEntry) {
            dpvProgress.setDownloadData(data)
        }
    }

}