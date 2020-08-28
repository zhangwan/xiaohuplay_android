package com.tinytiger.titi.ui.news

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.UserInfo
import com.tinytiger.common.net.data.gametools.GameAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.news.NewsBeanMulti
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.picbrowser.ImagePreviewLoader
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.news.NewsListAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.news.NewsDetailContract
import com.tinytiger.titi.mvp.presenter.news.NewsDetailPresenter
import com.tinytiger.titi.widget.dialog.FullSheetReplyDialog
import com.tinytiger.titi.widget.popup.PopupInput
import com.tinytiger.titi.widget.view.Anim.LikeView
import kotlinx.android.synthetic.main.activity_news_detail.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author lmq001
 * @date 2020/6/27 0027 下午 4:44
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 图文详情
 */
class NewsDetailActivity : BaseBackActivity(), NewsDetailContract.View,
    PopupInput.OnPopupInputListener, FullSheetReplyDialog.OnFullSheetListener {

    override fun onAddList(comment_id: Int, list: ArrayList<CommentAssessBean>) {}

    override fun showComment(bean: CommentListBean.DataBean) {}

    override fun showResult() {}

    override fun showLike(is_like: Int, id: Int) {
        if (id == 0 && is_like == 1) {
            GameAgentUtils.setGameDetailInfo(gameId, assess_id, 1,0)
        }
    }

    override fun onDismiss(comment_id: Int) {
    }

    override fun onLike(comment_id: Int, islike: Int) {
        if (islike == 1) {
            GameAgentUtils.setGameDetailInfo(gameId, assess_id, 1,0)
        }
    }

    override fun delComment(comment_id: Int) {
        showToast("删除成功")
//        mAdapter.setRemove(comment_id)
        comment_num -= 1
    }

    override fun addCommentTxt(txt: String, comment_id: Int) {
    }

    override fun showAssessList(mBean: CommentAssessInfo) {
    }

    override fun showAssessInfo(bean: GameAssessBean) {
    }

    //首次是否显示
    private var isShowFirst = true

    //private var title = "无"
    private var gameId = "0"
    private var is_mutual = 0
    private var uid = ""
    var view_log_id = ""
    var nestedScrollY = 0

    //评论选中回复id
    private var mSendCommentId = 0

    //回复一级评论id
    private var mTopCommentId = 0
    private var commentUserinfo: UserInfo? = null
    private var mCommentFragment: NewsCommentFragment? = null
    private var mContentInfoBean: ContentInfoBean.DataBean? = null

    private var sharetitle = ""
    private var sharedesc = ""
    private var shareurl = ""
    private var sharelogo = ""
    private val mPresenter by lazy { NewsDetailPresenter() }
    private val mAdapter by lazy { NewsListAdapter(ArrayList()) }

    init {
        mPresenter.attachView(this)
    }

    private var assess_id = "0"
    private var game_id = "2"
    private var ruleType = 0
    private var mContentId = ""

    companion object {
        fun actionStart(context: Context, content_id: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra("content_id", content_id)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.activity_news_detail

    override fun initData() {
        useStatusBarColor = false
        setWindowFeature()
        if (intent.hasExtra("content_id")) {
            mContentId = intent.getStringExtra("content_id")
        }
    }

    override fun initView() {
        if (ScreenUtil.checkDeviceHasNavigationBar(this)) {
            head_layout.setPadding(0, 0, 0, ScreenUtil.getNavigationBarHeight(this))
            llEt.setPadding(0, 0, 0, ScreenUtil.getNavigationBarHeight(this))
        }
        initRecyclerView()
        start()


        val dp210 = Dp2PxUtils.dip2px(this, 200)
        nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                nestedScrollY += scrollY - oldScrollY
                iv_top.alpha = scrollY.toFloat() / dp210
                if (scrollY > dp210) {
                    useStatusBarColor = true
                    llTitle.visibility = View.VISIBLE
                    ivBack.setImageResource(R.mipmap.icon_back)
                    ivMore.setImageResource(R.mipmap.icon_more_b)
                } else {
                    useStatusBarColor = false
                    llTitle.visibility = View.GONE
                    ivBack.setImageResource(R.mipmap.icon_back_w)
                    ivMore.setImageResource(R.mipmap.icon_more_w)
                }
                setStatusBar()
            })

        mCommentFragment = NewsCommentFragment.getInstance(mContentId, 0)
        mCommentFragment?.setonCommentClickListener(object :
            NewsCommentFragment.onCommentClickListener {
            override fun refreshComment(num: Int, needScroll: Boolean) {
                //新增评论时滑动
//                if (needScroll) scrollByDistance()
                mContentInfoBean!!.comment_num += num
                mAdapter.setCommentNum(mContentInfoBean!!.comment_num)
                mCommentFragment?.showCommentTitle(mContentInfoBean!!.comment_num)
                showCommentNum(mContentInfoBean!!)
            }

            override fun showKeyBord(nickname: String) {
                setInput(nickname)
            }
        })

        //添加评论信息
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_container, mCommentFragment!!)
            .commit()

        ivBack.setOnClickListener { finish() }

        tvAttention.setOnClickListener {
            mPresenter.follow(uid, ismutual)
        }

        ivShare.setOnClickListener {
            clickShare()
        }

        ivMore.setOnClickListener {
            clickShare()
        }

        et_send.setOnClickListener {
            mTopCommentId = 0
            mSendCommentId = 0
            setInput("")
        }

        llRead.setOnClickListener {
            scrollByDistance()
        }

        val softKeyBoardListener = SoftKeyBoardListener(this)
        softKeyBoardListener.setListener(object :
            SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {}

            override fun keyBoardHide(height: Int) {
                mPopupInput?.dismiss()
            }
        })

        viewLike.mListener = LikeView.OnLikeViewListener {
            clickLikeView()
        }
    }

    private fun clickLikeView() {
        var likeNum = 0
        if (mContentInfoBean!!.is_like == -1) {
            mPresenter.addLike(mContentId)
            likeNum = 1
        } else {
            likeNum = -1
            mPresenter.cancelLike(mContentId)
        }
        if (mAdapter.data.size > 0 && mAdapter.data[0].itemType == 1) {
            mAdapter.data[0].mNewsInfoBean.like_num += likeNum
            mAdapter.setLikeSize(mAdapter.data[0].mNewsInfoBean.like_num)
        }
    }

    private fun clickShare() {
        if (mContentInfoBean == null) return
        if (!isLoginStart()) return
        mPresenter.clickMore(
            supportFragmentManager, is_collect, mContentId, uid,
            shareurl, sharetitle, sharedesc, sharelogo
        )
    }

    private fun initRecyclerView() {
        recycler_view.adapter = mAdapter
//        recycler_view.isNestedScrollingEnabled = false
        recycler_view.setHasFixedSize(true)
        val list = ArrayList<NewsBeanMulti>()
        list.add(NewsBeanMulti(1))

        mAdapter.setNewInstance(list)
        mAdapter.setOnImageClickListener(object : NewsListAdapter.OnImageClickListener {
            override fun onClick(urls: List<String>, position: Int) {
                if (FastClickUtil.isFastClickTiming()) {
                    return
                }
                ImagePreviewLoader.showImagePreview(this@NewsDetailActivity, position, urls)
            }
        })
        mAdapter.addChildClickViewIds(R.id.tvAttention)
        mAdapter.setOnItemChildClickListener { _, _, _ ->
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnItemChildClickListener
            }

            mPresenter.follow(uid, is_mutual)
        }
    }

    private var mPopupInput: PopupInput? = null
    private fun setInput(hint: String) {
        if (mPopupInput == null) {
            mPopupInput = PopupInput(this, this)
        }
        mPopupInput?.setHint(if (hint.isNotEmpty()) hint else getString(R.string.edit_hint_say_some))
        mPopupInput!!.setAdjustInputMethod(true)
            .setAutoShowInputMethod(mPopupInput!!.findViewById(R.id.et_send), true)
            .showPopupWindow()
    }

    override fun clickInput(keyWord: String) {
        mCommentFragment!!.sendCommend(keyWord)
    }


    override fun start() {
        mPresenter.getContentInfo(mContentId)
    }

    override fun getUserFollow(is_mutual: Int) {
        this.is_mutual = is_mutual
        if (mAdapter.data.size > 0 && mAdapter.data[0].itemType == 1) {
            mAdapter.data[0].mNewsInfoBean.is_mutual = is_mutual
        }
        mAdapter.setfollow(is_mutual)

        setAttention(is_mutual)
    }

    override fun showContentInfo(bean: ContentInfoBean.DataBean) {
        mContentInfoBean = bean
        setRefreshHeader(bean)
        is_mutual = bean.is_mutual
        uid = bean.user_id
        is_collect = bean.is_collect
        shareurl = bean.share_url
        sharetitle = bean.title
        sharedesc = bean.introduce
        sharelogo = bean.cover
        viewLike.showNum = true
        viewLike.showZero = false
        viewLike.setLike(bean.is_like, bean.like_num)
        GlideUtil.loadImg(iv_image, bean.cover)
        showCommentNum(bean)

//        mHandler.postDelayed({
//            mPresenter.setRun(this, bean.cover, iv_top)
//        }, 1000)
        if (isShowFirst) {
            isShowFirst = false
            mHandler.postDelayed({ ivBg.visibility = View.GONE }, 200)
        }

        if (mAdapter.data.size > 0 && mAdapter.data[0].itemType == 1) {
            mAdapter.data[0].mNewsInfoBean = bean
        }
        //获取视频列表信息
        mPresenter.getIntroContentList(mContentId)
        ///显示评论信息
        mCommentFragment?.showContentInfo(bean)
    }

    /**
     * 显示评论数量
     */
    fun showCommentNum(bean: ContentInfoBean.DataBean) {
        tv_comment_num.visibility = if (bean.comment_num != 0) View.VISIBLE else View.GONE
        tv_comment_num.text = StringUtils.sizeToString(bean.comment_num)
    }

    override fun showIntroContentList(bean: ArrayList<RecommendBean>) {
        if (bean.size > 0) {
            mAdapter.addData(NewsBeanMulti(3, "精彩推荐"))
            for (recommend in bean) {
                mAdapter.addData(NewsBeanMulti(recommend))
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    /**
     * 设置头部区域内容
     */
    private fun setRefreshHeader(mBean: ContentInfoBean.DataBean) {
        avUserTitle.setAvatar(mBean.avatar, mBean.master_type).setUserId(mBean.user_id)
        tvNameTitle.setNickname(mBean.nickname, mBean.medal_image)
        setAttention(mBean.is_mutual)
        tvAttention.setOnClickListener {
            mPresenter.follow(mBean.user_id, ismutual)
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

    override fun showLike(is_like: Int) {
        mContentInfoBean!!.is_like = is_like
        viewLike.setLike(is_like, mContentInfoBean!!.like_num)
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
        when (ismutual) {
            1 -> {
                tvAttention.text = "互相关注"
                tvAttention.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_15_aaaaaa)
                tvAttention.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            }
            0 -> {
                tvAttention.text = "已关注"
                tvAttention.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_15_aaaaaa)
                tvAttention.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            }
            -1 -> {
                tvAttention.text = "+ 关注"
                tvAttention.background =
                    ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_16_ffcc03)
                tvAttention.setTextColor(ContextCompat.getColor(this, R.color.color_ffcc03))
            }
            else -> {
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
        val distance = recycler_view.bottom - nestedScrollY - Dp2PxUtils.dip2px(this, 71)
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
