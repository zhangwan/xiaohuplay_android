package com.tinytiger.titi.ui.game

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.UserInfo
import com.tinytiger.common.net.data.gametools.AssessTagBean
import com.tinytiger.common.net.data.gametools.GameAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.common.widget.picbrowser.ImagePreviewLoader
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.AmwayFelxboxAdapter
import com.tinytiger.titi.adapter.gametools.GameReviewAdapter2
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.gametools.GameReviewContract
import com.tinytiger.titi.mvp.presenter.gametools.GameReviewPresenter
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.widget.dialog.FullSheetReplyDialog
import com.tinytiger.titi.widget.dialog.ShareScreenDialog
import com.tinytiger.titi.widget.popup.PopupInput
import com.tinytiger.titi.widget.view.Anim.LikeView
import kotlinx.android.synthetic.main.game_activity_reviews.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 *
 * @author zhw_luke
 * @date 2020/2/27 0027 下午 4:44
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 游戏点评详情
 */
class GameReviewsActivity : BaseBackActivity(), GameReviewContract.View,
    PopupInput.OnPopupInputListener, FullSheetReplyDialog.OnFullSheetListener {

    override fun onAddList(comment_id: Int, list: ArrayList<CommentAssessBean>) {
        mAdapter.setReplys(comment_id, list)
    }

    override fun showLike(is_like: Int, id: Int) {
        if (id == 0 && is_like == 1) {
            GameAgentUtils.setGameDetailInfo(game_id, assess_id, 1,open_from)
        }
    }

    override fun showCollect(collect: Int) {
        is_collect = collect
        if (collect == 1) {
            showToast("收藏成功")
        } else {
            showToast("取消收藏成功")
        }
    }

    override fun onDismiss(comment_id: Int) {
        mAdapter.setRemove(comment_id)
    }

    override fun onLike(comment_id: Int, islike: Int) {
        mAdapter.setLike(comment_id, islike)
        if (islike == 1) {
            GameAgentUtils.setGameDetailInfo(game_id, assess_id, 1,open_from)
        }

    }

    override fun delComment(comment_id: Int) {
        showToast("删除成功")

        mAdapter.setRemove(comment_id)
        comment_num -= 1
        setSize()
    }

    override fun clickInput(keyWord: String) {
        mPresenter.addComment(game_id, assess_id, "$mSendCommentId", keyWord)
    }

    override fun addCommentTxt(txt: String, comment_id: Int) {
        comment_num += 1
        setSize()

        if (mSendCommentId != 0) {
            mAdapter.addBeanTwo(txt, comment_id, mTopCommentId, commentUserinfo)
        } else {
            mAdapter.addBean(txt, comment_id)
            scrollByDistance()
        }

        GameAgentUtils.setGameDetailInfo(game_id, assess_id, 2,open_from)

    }

    override fun getUserFollow(is_mutual: Int) {
        setAttention(is_mutual)
    }

    override fun showAssessList(mBean: CommentAssessInfo) {
        if (mBean.data == null) {
            return
        }
        mAdapter.author_id = (mBean.data.author_user_id)

        if (mBean.data.current_page == 1) {
            mAdapter.setNewInstance(mBean.data.data)

            if (topTitle != 0) {
                topTitle = 0
                mHandler.postDelayed({
                    scrollByDistance()
                }, 1200)
            }
        } else {
            mAdapter.addData(mBean.data.data)
        }
        mAdapter.removeAllFooterView()
        if (mBean.data.current_page >= mBean.data.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getFooterView(this, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
    }

    override fun showAssessInfo(bean: GameAssessBean) {
        getRefreshHeader(bean.data)
        sharetitle = "《${bean.data.name}》到底好不好玩？"

        tv_name.text = StringUtils.stringName(bean.data.name)
        tv_namesize.text = StringUtils.sizeToString(bean.data.assess_num) + "条安利"
        GlideUtil.loadImg(iv_image, bean.data.thumbnail)

        ratingBar.rating = bean.data.score
        shareurl = bean.data.share_url
        sharelogo = bean.data.logo

        mPresenter.indexAssessComment(game_id, assess_id, page, ruleType)
        mHandler.postDelayed({
            mPresenter.setRun(this, bean.data.background, iv_top)
        }, 1000)

        gameId = bean.data.id
        GameAgentUtils.setGameDetailInfo(game_id, assess_id, 4,open_from)
    }

    //private var title = "无"
    private var gameId = "0"





    //评论选中回复id
    private var mSendCommentId = 0

    //回复一级评论id
    private var mTopCommentId = 0
    private var commentUserinfo: UserInfo? = null

    private var sharetitle = ""
    private var sharedesc = ""
    private var shareurl = ""
    private var sharelogo = ""
    private val mPresenter by lazy { GameReviewPresenter() }
    private val mAdapter by lazy { GameReviewAdapter2() }
    private var dp80 = 160
    private var nestedScrollY = 0

    init {
        mPresenter.attachView(this)
    }

    private var assess_id = "0"
    private var game_id = "2"
    private var ruleType = 0
    private var userid = ""

    companion object {
        fun actionStart(context: Context, game_id: String, assess_id: String) {
            actionStart(context, game_id, assess_id, 0)
        }

        fun actionStart(context: Context, game_id: String, assess_id: String,open_from:Int) {
            actionStart(context, game_id, assess_id, 0,open_from)
        }

        fun actionStart(context: Context, game_id: String, assess_id: String, top: Int,open_from:Int) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, GameReviewsActivity::class.java)
            intent.putExtra("game_id", game_id)
            intent.putExtra("assess_id", assess_id)
            intent.putExtra("top", top)
            intent.putExtra("open_from", open_from)
            context.startActivity(intent)
        }
    }

    /**
     * 页面来源 0首页 1发现页
     */
    var open_from=0

    override fun layoutId(): Int = R.layout.game_activity_reviews

    override fun initData() {
        useStatusBarColor = false
        setWindowFeature()
        assess_id = intent.getStringExtra("assess_id")
        game_id = intent.getStringExtra("game_id")
        topTitle = intent.getIntExtra("top", 0)
        open_from = intent.getIntExtra("open_from", 0)
    }

    /**
     * 跳转位置view
     * 1 跳转评论区域
     * 0 无跳转
     */
    private var topTitle = 0

    override fun initView() {
        if (ScreenUtil.checkDeviceHasNavigationBar(this)) {
            llEt.setPadding(0, 0, 0, ScreenUtil.getNavigationBarHeight(this))
        }
        dp80 = Dp2PxUtils.dip2px(this, 80)
        start()
        refreshLayout.setEnableRefresh(false)
        ivBack.setOnClickListener {
            finish()
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter

        mAdapter.mOnPostListener = object : GameReviewAdapter2.OnPostListener {
            override fun onMoreClick(mBean: CommentAssessBean, type: Boolean, view: View) {
                if (type) {
                    if (MyUserData.isEmptyToken()) {
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                        return
                    }
                    TextDialog.create(supportFragmentManager).isShowTitle(true)
                        .setMessage("确认删除此评论吗？")
                        .setViewListener(object : TextDialog.ViewListener {
                            override fun click() {
                                mPresenter.delCommentMe(mBean.id)
                            }
                        }).show()
                } else {
                    mPresenter.clickMore(this@GameReviewsActivity, view, mBean.id, mBean.user_id)
                }
            }

            override fun onReplyClick(mBean: CommentAssessBean, replys: CommentAssessBean) {
                mTopCommentId = mBean.id
                mSendCommentId = replys.id
                commentUserinfo = null
                if (mTopCommentId != mSendCommentId) {
                    commentUserinfo = replys.replysUserinfo
                }

                setInput("@${replys.replysUserinfo.nickname}")
            }

            override fun onItemClick(mBean: CommentAssessBean) {
                FullSheetReplyDialog().apply {
                    mBeanTop = mBean
                    gameId = game_id
                    mContentId = assess_id
                    listener = this@GameReviewsActivity
                }.show(supportFragmentManager, "dialog")
            }

            override fun onLikeClick(mBean: CommentAssessBean) {
                mPresenter.handleLike(game_id, assess_id, mBean.id)
            }
        }


        val dp210 = Dp2PxUtils.dip2px(this, 150)
        nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                nestedScrollY += scrollY - oldScrollY
                iv_top.alpha = scrollY.toFloat() / dp210
                if (scrollY > dp210) {
                    rlTitle.visibility = View.VISIBLE
                    ivMore.setImageResource(R.mipmap.game_icon_share)
                } else {
                    rlTitle.visibility = View.GONE
                    ivMore.setImageResource(R.mipmap.icon_more_w)
                }
            })

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            mPresenter.indexAssessComment(game_id, assess_id, page, ruleType)
        }

        tvAttention.setOnClickListener {
            mPresenter.follow(userid, ismutual)
        }

        viewLike.mListener = LikeView.OnLikeViewListener {
            mPresenter.likeAssessOrTag(game_id, assess_id, "0")
        }

        ivShare.setOnClickListener {
            //  val  bit=  ScreenShootUtil.screenshot(x5webView)
//            x5webView.clearCache(true)
//            val  bit1= x5webView.drawToBitmap()
//
//            ShareScreenDialog.create(supportFragmentManager).apply {
//                bitmap=bit1
//            }.show()

            mPresenter.setShare(false, supportFragmentManager, game_id, assess_id, shareurl,
                sharetitle, sharedesc, sharelogo, is_collect, userid,open_from)
        }
        ivMore.setOnClickListener {

            mPresenter.setShare(
                true, supportFragmentManager, game_id, assess_id,
                shareurl, sharetitle, sharedesc,
                sharelogo, is_collect, userid,open_from
            )
//            GameAgentUtils.setGameDetailInfo(gameId, assess_id, 3,open_from)
        }

        rlGame.setOnClickListener {
            GameDetailActivity.actionStart(this, game_id, 0)
        }

        et_send.setOnClickListener {
            mTopCommentId = 0
            mSendCommentId = 0
            setInput("")
        }

        flRead.setOnClickListener {
            scrollByDistance()
        }


        val softKeyBoardListener = SoftKeyBoardListener(this)
        softKeyBoardListener.setListener(object :
            SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
            }

            override fun keyBoardHide(height: Int) {
                mPopupInput?.dismiss()
            }
        })
    }

    private var mPopupInput: PopupInput? = null
    private fun setInput(hint: String) {
        if (mPopupInput == null) {
            mPopupInput = PopupInput(this, this)
        }
        mPopupInput?.setHint(hint)
        mPopupInput!!.setAdjustInputMethod(true)
            .setAutoShowInputMethod(mPopupInput!!.findViewById(R.id.et_send), true)
            .showPopupWindow()
    }


    override fun start() {
        mPresenter.getAssessInfo(game_id, assess_id)
    }


    private val mFragmentContainerHelper = FragmentContainerHelper()

    private fun getRefreshHeader(mBean: GameAssessBean.Data) {
        ivBg.visibility = View.VISIBLE

        userid = mBean.user_id

        avUser.setAvatar(mBean.avatar, mBean.master_type).setUserId(mBean.user_id)
        tvName1.setNickname(mBean.nickname, mBean.medal_image)
        avUserTitle.setAvatar(mBean.avatar, mBean.master_type).setUserId(mBean.user_id)
        tvNameTitle.setNickname(mBean.nickname, mBean.medal_image)

        ratingBar1.rating = Math.round(mBean.score).toFloat()
        setAttention(mBean.is_mutual)

        tvAttention1.setOnClickListener {
            mPresenter.follow(userid, ismutual)
        }


        if (mBean.viewpoint_list != null && mBean.viewpoint_list.size > 0) {
            recycler_tab.visibility = View.VISIBLE
            tvSelect.visibility = View.VISIBLE
            val manager = FlexboxLayoutManager(this)
            manager.flexDirection = FlexDirection.ROW
            manager.flexWrap = FlexWrap.WRAP
            manager.alignItems = AlignItems.STRETCH
            recycler_tab.clipToPadding = false
            recycler_tab.layoutManager = manager
            val adapter = AmwayFelxboxAdapter()
            adapter.showType = true
            adapter.setNewInstance(mBean.viewpoint_list)
            recycler_tab.adapter = adapter
            adapter.listener = object : AmwayFelxboxAdapter.OnAssessTagListener {
                override fun onAssessTag(mBean: AssessTagBean) {
                    mPresenter.likeAssessOrTag(game_id, assess_id, mBean.id)
                }
            }
        } else {
            recycler_tab.visibility = View.GONE
            tvSelect.visibility = View.GONE
        }

        sharedesc = "看看来自" + mBean.nickname + "对《${mBean.name}》的评价"

        //解决RecycleView和WebView滑动空白的问题
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val chart_url = mBean.assess_radar_chart_url + "?game_id=$game_id&assess_id=$assess_id"
        x5webView.imageClick { imageUrls, position ->
            if (FastClickUtil.isFastClickTiming()) {
                return@imageClick
            }
            ImagePreviewLoader.showImagePreview(this, position, imageUrls)
        }.loadDataURL(chart_url)
        x5webView.layoutParams = params


        comment_num = mBean.comment_num
        val titles = ArrayList<String>()
        titles.add("最新")
        titles.add("最热")
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = false
        val mAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val titleView = Indicator.mTitleView(context, titles[index], 1f)
                titleView.setOnClickListener {
                    mFragmentContainerHelper.handlePageSelected(index)
                    page = 1
                    ruleType = index
                    showLoading()
                    mPresenter.indexAssessComment(game_id, assess_id, page, ruleType)
                }
                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
            }
        }
        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(magic_indicator)

        setSize()
        is_collect = mBean.is_collect
        viewLike.showNum = false
        viewLike.showZero = false
        viewLike.setLike(mBean.is_like, mBean.like_num)
    }

    private fun setSize() {
        tvSize?.text = "共${StringUtils.sizeToString(comment_num)}条"
//        tv_comment_num.visibility = if (comment_num != 0) View.VISIBLE else View.GONE
        tv_comment_num.text = StringUtils.sizeToString(comment_num)
    }


    private var comment_num = 0
    private var ismutual = -1
    private var is_collect = 0
    private fun setAttention(is_mutual: Int) {
        ismutual = is_mutual
        if (!isLogin()) {
            ismutual = -1
        }
        //1:互相关注 0:已关注 -1:未关注 -2:自己
        tvAttention.visibility = View.VISIBLE
        tvAttention1.visibility = View.VISIBLE
        when (ismutual) {
            1 -> {
                tvAttention1.text = "互相关注"
                tvAttention1.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_15_aaaaaa)
                tvAttention1.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
                tvAttention.text = "互相关注"
                tvAttention.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_15_aaaaaa)
                tvAttention.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            }
            0 -> {
                tvAttention1.text = "已关注"
                tvAttention1.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_15_aaaaaa)
                tvAttention1.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
                tvAttention.text = "已关注"
                tvAttention.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_15_aaaaaa)
                tvAttention.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            }
            -1 -> {
                tvAttention1.text = "+ 关注"
                tvAttention1.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_16_ffcc03)
                tvAttention1.setTextColor(ContextCompat.getColor(this, R.color.color_ffcc03))

                tvAttention.text = "+ 关注"
                tvAttention.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_16_ffcc03)
                tvAttention.setTextColor(ContextCompat.getColor(this, R.color.color_ffcc03))
            }
            else -> {
                tvAttention1.visibility = View.GONE
                tvAttention.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        mPresenter.mPopupWindow?.dismiss()
    }

    /**
     * 跳转指定view
     */
    private fun scrollByDistance() {
        val distance = refreshLayout.top - dp80 * 2 - nestedScrollY
        nestedScrollView.fling(distance)
        nestedScrollView.smoothScrollBy(0, distance)
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        when (errorCode) {
            ErrorStatus.NETWORK_ERROR -> {
                if (mAdapter.data.size == 0) {
                    mAdapter.setEmptyView(
                        RefreshView.getNetworkView(this, recycler_view, { start() })
                    )
                } else {
                    showToast(errorMsg)
                }
            }
            10002, 10005, 10009 -> {
                showToast(errorMsg)
                finish()
            }
            else -> {
                showToast(errorMsg)
            }
        }
    }

}