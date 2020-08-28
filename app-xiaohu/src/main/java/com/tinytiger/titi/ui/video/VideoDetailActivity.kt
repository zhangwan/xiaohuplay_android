package com.tinytiger.titi.ui.video

import android.content.Context
import android.content.Intent
import android.view.OrientationEventListener
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.tencent.liteav.demo.play.SuperPlayerConst
import com.tencent.liteav.demo.play.SuperPlayerModel
import com.tencent.liteav.demo.play.SuperPlayerView
import com.tencent.rtmp.TXLiveBase
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.DataReportEvent
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.video.RecommendAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.data.comment.IntroduceBeanMulti
import com.tinytiger.titi.mvp.contract.video.VideoContract
import com.tinytiger.titi.mvp.presenter.Video.VideoPresenter
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.news.NewsCommentFragment
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.widget.popup.PopupInput
import com.tinytiger.titi.widget.view.Anim.LikeView
import kotlinx.android.synthetic.main.video_activity_detail.*
import org.greenrobot.eventbus.EventBus

/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 视频详情页 Activity
*/
class VideoDetailActivity : BaseActivity(), VideoContract.View, NetStateChangeObserver,
    PopupInput.OnPopupInputListener {
    override fun showDelComment(comment_id: Int) {
    }

    override fun onNetDisconnected() {
    }

    override fun onNetConnected(networkType: NetworkType?) {
        setVideoNet(networkType!!)
    }

    private fun setVideoNet(networkType: NetworkType) {
        var type = 2
        if (networkType == NetworkType.NETWORK_NO) {
            type = 0
        } else if (networkType == NetworkType.NETWORK_WIFI) {
            type = 1
        }
        mSuperPlayerView.setNetType(type)
    }

    override fun showLikeStatus(is_like: Int, comment_id: Int) {
    }

    override fun showBlackStatus(is_black: Int, userId: String) {
        mContentInfoBean!!.is_black = is_black
        mAdapter.data[0].contentBean.is_black = is_black
        mAdapter.notifyItemChanged(0)
    }

    override fun showMutual(is_mutual: Int) {
        mContentInfoBean!!.is_mutual = is_mutual
        if (mAdapter.data.size > 0 && mAdapter.data[0].itemType == 1) {
            mAdapter.data[0].contentBean.is_mutual = is_mutual
        }
        mAdapter.setMutual(is_mutual)

        setAttention(is_mutual)
    }

    private var mContentId: String = "0"
    private var videoUrl: String = ""

    private var mCommentFragment: NewsCommentFragment? = null

    private var mWindowHeight = 0
    private var nestedScrollY = 0

    companion object {
        private const val CONTENT_ID = "content_id"
        private const val CONTENT_URL = "content_url"

        fun actionStart(context: Context, content_id: String) {
            actionStart(context, content_id, "")
        }

        fun actionStart(context: Context, content_id: String, url: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, VideoDetailActivity::class.java)
            intent.putExtra(CONTENT_ID, content_id)
            intent.putExtra(CONTENT_URL, url)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { VideoPresenter() }
    val mAdapter by lazy { RecommendAdapter(ArrayList()) }

    override fun layoutId(): Int = R.layout.video_activity_detail

    override fun initData() {
        useStatusBarColor = false
        mPresenter.attachView(this)
        NetStateChangeReceiver.registerReceiver(this)
        if (intent.hasExtra(CONTENT_ID)) {
            mContentId = intent.getStringExtra(CONTENT_ID)
        }
        if (intent.hasExtra(CONTENT_URL)) {
            videoUrl = intent.getStringExtra(CONTENT_URL)
        }

        mWindowHeight = resources.displayMetrics.widthPixels * 9 / 16
    }

    override fun initView() {
        if (ScreenUtil.checkDeviceHasNavigationBar(this)) {
            nestedScrollView.setPadding(0, 0, 0, ScreenUtil.getNavigationBarHeight(this))
            llEt.setPadding(0, 0, 0, ScreenUtil.getNavigationBarHeight(this))
        }
        SearchAgentUtils.setNewVideo(mContentId)
        mSuperPlayerView.layoutParams.height = resources.displayMetrics.widthPixels * 9 / 16
        mSuperPlayerView.window = window

        ivShare.setOnClickListener(mOnClickListener)

        mPresenter.getContentInfo(mContentId)

        initFragment()

        finTime = System.currentTimeMillis()
        initVideo()

        val softKeyBoardListener = SoftKeyBoardListener(this)
        softKeyBoardListener.setListener(object :
            SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {}

            override fun keyBoardHide(height: Int) {
                mPopupInput?.dismiss()
            }
        })

        et_send.setOnClickListener {
            setInput("")
        }
//        tv_send.postDelayed({ setAutoOrientation() }, 4000)

        val dp210 = Dp2PxUtils.dip2px(this, 60)
        nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                nestedScrollY += scrollY - oldScrollY
//                iv_top.alpha = scrollY.toFloat() / dp210
//                if (scrollY > dp210) {
//                    llTitle.visibility = View.VISIBLE
//                } else {
//                    llTitle.visibility = View.GONE
//                }
            })

        llRead.setOnClickListener {
            scrollByDistance()
        }

        viewLike.mListener = LikeView.OnLikeViewListener {
            onClickLike(0)
        }
    }

    private var mOrientationListener: OrientationEventListener? = null

    private fun setAutoOrientation() {
        var time = System.currentTimeMillis()
        var orientationType = -1
        mOrientationListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return

                var type = -1
                if (orientation > 350 || orientation < 10) {
                    //竖屏 上
                    type = 0
                } else if (orientation > 80 && orientation < 100) {
                    //右横屏
                    type = 2
                } else if (orientation > 260 && orientation < 280) {
                    //左横屏
                    type = 1
                }

                if (type < 0) {
                    return
                }
                if (orientationType == type) {
                    return
                }
                orientationType = type
                if (System.currentTimeMillis() - time < 1500) {
                    return
                }
                time = System.currentTimeMillis()
                mSuperPlayerView.setScreenOrientation(type)
            }
        }
        //判断设备是否支持旋转
        if (mOrientationListener!!.canDetectOrientation()) {
            mOrientationListener?.enable()
        } else {
            mOrientationListener?.disable()
        }
    }


    fun refreshUrl(id: String, url: String) {
        if (!MyNetworkUtil.isNetworkAvailable(this)) {
            showToast("网络不可用")
            return
        }
        if (FastClickUtil.isFastClickTiming()) {
            return
        }

        getTimeVideo()
        isFirst = false
        mContentId = id
        videoUrl = url

        setVideoPath(videoUrl)
        mPresenter.getContentInfo(mContentId)
    }


    private fun initFragment() {
        mCommentFragment = NewsCommentFragment.getInstance(mContentId, 1)
        mCommentFragment?.setonCommentClickListener(object :
            NewsCommentFragment.onCommentClickListener {
            override fun refreshComment(num: Int, needScroll: Boolean) {
                //新增评论时滑动
//                if (needScroll) scrollByDistance()
                mContentInfoBean!!.comment_num += num
                showCommentNum(mContentInfoBean!!)
                mAdapter.setCommentNum(mContentInfoBean!!.comment_num)
                mCommentFragment?.showCommentTitle(mContentInfoBean!!.comment_num)
            }

            override fun showKeyBord(nickname: String) {
                setInput(nickname)
            }
        })
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_container, mCommentFragment!!)
            .commit()

        recycler_view.adapter = mAdapter
        mAdapter.addChildClickViewIds(
            R.id.iv_avatar, R.id.tv_name, R.id.ll_content, R.id.iv_more,
            R.id.tvAttention, R.id.tv_share_num
        )
        mAdapter.setOnItemChildClickListener(mOnItemChildClickListener)

        mAdapter.listener = object : RecommendAdapter.OnRecommendListener {
            override fun onLike(mBean: ContentInfoBean.DataBean) {
                onClickLike(1)
            }

            override fun onConllection(mBean: ContentInfoBean.DataBean) {
                onClickCollection(1)
            }

            override fun onClickComment(mBean: ContentInfoBean.DataBean) {
                scrollByDistance()
            }
        }
    }

    private var mOnItemChildClickListener = object : OnItemChildClickListener {
        override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
            when (view.id) {
                R.id.iv_avatar, R.id.tv_name -> {
                    if (mContentInfoBean == null) {
                        return
                    }
                    isRefresh = true
                    HomepageActivity.actionStart(
                        this@VideoDetailActivity,
                        mContentInfoBean!!.user_id
                    )
                }
                R.id.ll_content -> {
                    if (mAdapter.data[position].recommendBean != null) {
                        refreshUrl(
                            mAdapter.data[position].recommendBean.id,
                            mAdapter.data[position].recommendBean.video_url
                        )
                    }
                }
                R.id.iv_more -> {
                    if (mContentInfoBean == null) {
                        return
                    }
//                    clickMore(view)
                }
                R.id.tvAttention -> {
                    if (MyUserData.isEmptyToken()) {
                        isRefresh = true
                        EventBus.getDefault().post(ClassEvent("LoginActivity"))
                        return
                    }
                    if (mContentInfoBean == null) {
                        return
                    }
                    mPresenter.doFollow(mContentInfoBean!!.is_mutual, mContentInfoBean!!.user_id)
                }
                R.id.tv_share_num -> {
                    clickShare()
                }
            }
        }

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

    override fun clickInput(keyWord: String) {
        mCommentFragment!!.sendCommend(keyWord)
    }

    private val mOnClickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.ivShare -> {
                clickShare()
            }
        }
    }

    //两遍同步
    fun onClickCollection(type: Int) {
        if (mContentInfoBean == null) {
            return
        }
        if (mContentInfoBean!!.is_collect == -1) {
            mContentInfoBean!!.is_collect = 1
            mPresenter.addCollect(mContentId)
        } else {
            mContentInfoBean!!.is_collect = -1
            mPresenter.cancelCollect(mContentId)
        }

        if (0 == type) {
            mAdapter.setConllections(mContentInfoBean!!.is_collect)
        } else {
//            viewCollection!!.setConllection(mContentInfoBean!!.is_collect, 0)
        }
    }

    /**
     * 发送消息来源
     * type 0本页面 1简介页
     */
    fun onClickLike(type: Int) {
        if (mContentInfoBean == null) return

        if (mContentInfoBean!!.is_like == -1) {
            mContentInfoBean!!.is_like = 1
            mPresenter.addLike(mContentId)
        } else {
            mContentInfoBean!!.is_like = -1
            mPresenter.cancelLike(mContentId)
        }
        if (0 == type) {
            mAdapter.setLikes(mContentInfoBean!!.is_like)
        } else {
            mContentInfoBean!!.like_num += mContentInfoBean!!.is_like
            viewLike.setLike(
                mContentInfoBean!!.is_like, mContentInfoBean!!.like_num
            )
        }
    }

    override fun start() {
    }

    /**
     * 上传统计时间
     */
    private fun getTimeVideo() {
        val time = System.currentTimeMillis() - sysTime
        if (time > 3000) {
            if (mContentInfoBean != null && mContentInfoBean!!.view_log_id != null) {

                if (mSuperPlayerView.videoTimeStop < mSuperPlayerView.videoTimeStart) {
                    //videoTimeStorp未重新赋值
                    mSuperPlayerView.videoTimeStop = System.currentTimeMillis()
                }
                if (mSuperPlayerView.playState == 1) {
                    //多次列表页切换,videoTimeStorp未重新赋值
                    mSuperPlayerView.videoTimeStop = System.currentTimeMillis()
                }

                EventBus.getDefault().post(
                    DataReportEvent(
                        mContentInfoBean!!.view_log_id,
                        (time / 1000).toInt(),
                        ((mSuperPlayerView.videoTimeStop - mSuperPlayerView.videoTimeStart) / 1000).toInt()
                    )
                )
            }
        }
        mSuperPlayerView.videoTimeStart = System.currentTimeMillis()
        mSuperPlayerView.videoTimeStop = 0
        sysTime = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        //TCAgent.onPageStart(this, "页面-视频详情")
        NetStateChangeReceiver.registerObserver(this)
        sysTime = System.currentTimeMillis()

        //屏幕常亮
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        if (mSuperPlayerView.playState == SuperPlayerConst.PLAYSTATE_PLAY) {
            mSuperPlayerView.onResume()
            if (mSuperPlayerView.playMode == SuperPlayerConst.PLAYMODE_FLOAT) {
                mSuperPlayerView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW)
            }
        }

        setVideoNet(com.tinytiger.titi.utils.net.NetworkUtil.getNetworkType(this))

        if (isRefresh) {
            isRefresh = false
            mPresenter.getContentInfo(mContentId)

            mCommentFragment?.apply {
                mCommentFragment!!.refreshAdapter()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        // TCAgent.onPageEnd(this, "页面-视频详情")
        NetStateChangeReceiver.unRegisterObserver(this)
        getTimeVideo()
        window.setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (mSuperPlayerView.playMode != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.onPause()
        }
    }

    override fun onDestroy() {
        NetStateChangeReceiver.unRegisterReceiver(this)
        super.onDestroy()
        mOrientationListener?.disable()
        mPresenter.detachView()
        mSuperPlayerView.release()
        if (mSuperPlayerView.playMode != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.resetPlayer()
        }
    }

    private var mContentInfoBean: ContentInfoBean.DataBean? = null

    override fun showContentInfo(bean: ContentInfoBean.DataBean) {
        mContentInfoBean = bean
        mContentId = bean.id

        mSuperPlayerView.setBgImage(bean.cover)
        //当视频没有传入url,或者刷新时
        if (mSuperPlayerView.mCurrentPlayVideoURL == null) {
            setVideoPath(bean.video_url)
        }

        viewLike.showNum = true
        viewLike.showZero = false
        viewLike.setLike(bean.is_like, bean.like_num)
//        viewCollection.setConllection(bean.is_collect, 0)

        displayIntroduceInfo()

        //获取更多推荐
        mPresenter.getIntroContentList(mContentId)

        if (!isFirst) {
            mCommentFragment?.apply {
                showContentInfo(bean)
            }
            isFirst = true
        }

        if (isShowFirst) {
            isShowFirst = false
            mHandler.postDelayed({ ivBg.visibility = View.GONE }, 200)
        }
    }

    //首次是否显示
    private var isShowFirst = true
    private var isFirst = false
    var isRefresh = false

    /**
     * 跳转指定view
     */
    private fun scrollByDistance() {
        val distance = recycler_view.bottom - nestedScrollY -
                ScreenUtil.getNavigationBarHeight(this) + Dp2PxUtils.dip2px(this, 53)
        nestedScrollView.fling(distance)
        nestedScrollView.smoothScrollBy(0, distance)
    }

    /**
     * 设置列表数据
     */
    override fun showIntroContentList(bean: ArrayList<RecommendBean>) {
        mSuperPlayerView.showIntroContentList(bean)
        displayIntroContent(bean)
    }

    override fun showResult() {
        mPresenter.getContentInfo(mContentId)
    }

    /**
     * 显示评论数量
     */
    fun showCommentNum(bean: ContentInfoBean.DataBean) {
        tv_comment_num.visibility = if (bean.comment_num != 0) View.VISIBLE else View.GONE
        tv_comment_num.text = StringUtils.sizeToString(bean.comment_num)
    }

    fun displayIntroduceInfo() {
        displayRefreshHeader(mContentInfoBean!!)
        showCommentNum(mContentInfoBean!!)
        if (mAdapter.data.size > 0 && mAdapter.data[0].contentBean != null) {
            mAdapter.data[0].contentBean = mContentInfoBean
            mAdapter.notifyDataSetChanged()
        } else {
            mAdapter.addData(0, IntroduceBeanMulti(mContentInfoBean))
        }
    }

    fun displayIntroContent(bean: ArrayList<RecommendBean>) {
        if (bean.size > 0) {
            if (mAdapter.data.size > 0 && mAdapter.data[0].contentBean != null) {
                val intro: IntroduceBeanMulti = mAdapter.data[0]
                mAdapter.data.clear()
                mAdapter.addData(intro)
                mAdapter.addData(IntroduceBeanMulti("精彩推荐"))
                for (item in bean) {
                    mAdapter.addData(IntroduceBeanMulti(item))
                }
            }
        }
    }

    /**
     * 设置头部区域内容
     */
    private fun displayRefreshHeader(mBean: ContentInfoBean.DataBean) {
        avUserTitle.setAvatar(mBean.avatar, mBean.master_type).setUserId(mBean.user_id)
        tvNameTitle.setNickname(mBean.nickname, mBean.medal_image)
        setAttention(mBean.is_mutual)
        tvAttention.setOnClickListener {
            if (MyUserData.isEmptyToken()) {
                isRefresh = true
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            mPresenter.doFollow(mBean!!.is_mutual, mBean!!.user_id)
        }
    }

    private fun setAttention(is_mutual: Int) {
        //1:互相关注 0:已关注 -1:未关注 -2:自己
        tvAttention.visibility = View.VISIBLE
        when (is_mutual) {
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

    override fun showComment(bean: CommentListBean.DataBean) {
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == 10002 || errorCode == 10005) {
            showNoCommentDialog(errorMsg)
        } else if (errorCode == ErrorStatus.NETWORK_ERROR) {
            mHandler.postDelayed({ mCommentFragment?.showErrorMsg(errorMsg, errorCode) }, 300)
        } else {
            showToast(errorMsg)
        }
    }

    /**
     * 内容不存在弹框
     */
    private fun showNoCommentDialog(errorMsg: String) {
        TextDialog
            .create(supportFragmentManager)
            .setShowButton(isCancel = false, isConfirm = true)
            .isCancelOutside(false)
            .setMessage(errorMsg)
            .setDismissListener(object : TextDialog.DismissListener {
                override fun onDismiss() {
                    finish()
                }
            })
            .show()
        mSuperPlayerView.mCurrentPlayVideoURL = ""
        mHandler.postDelayed({
            mSuperPlayerView.stopPlay()
        }, 400)
    }

    override fun onBackPressed() {
        mfinish()
    }

    private var finTime = 0.toLong()
    private fun mfinish() {
        if (mSuperPlayerView.playMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            mSuperPlayerView.setPlayMode()
        } else {
            if (System.currentTimeMillis() - finTime < 1200) {

            } else {
                finish()
            }
        }
    }


    private fun clickShareType(platform: String) {
        if (mContentInfoBean == null) {
            return
        }

        val share_url =
            mContentInfoBean!!.share_url + "?user_id=" + mContentInfoBean!!.user_id + "&content_id=" + mContentInfoBean!!.id
        EventBus.getDefault()
            .post(
                ShareEvent(
                    "no",
                    share_url,
                    mContentInfoBean!!.title,
                    mContentInfoBean!!.introduce,
                    mContentInfoBean!!.cover,
                    platform
                )
            )

        mContentInfoBean!!.share_num++
        displayIntroduceInfo()
    }

    fun clickShare() {
        if (mContentInfoBean == null) return

        val url =
            mContentInfoBean!!.share_url + "?user_id=" + mContentInfoBean!!.user_id + "&content_id=" + mContentInfoBean!!.id

        ShareDialog.create(supportFragmentManager).apply {
            class_name = "no"
            share_url = url
            share_title = mContentInfoBean!!.title
            share_desc = mContentInfoBean!!.introduce
            share_image = mContentInfoBean!!.cover
            userId = mContentInfoBean!!.user_id
            collectionType = 0
            contentId = "" + mContentId
            report_type = 2
        }.setOnItemClickListener(object : ShareDialog.OnItemClickListener {
            override fun click(type: Int) {
                if (type == 0) {
                    mContentInfoBean!!.share_num++
                    displayIntroduceInfo()
                }
            }

        })
            .show()
    }

    private fun setVideoPath(url: String) {
        val model2 = SuperPlayerModel()
        model2.title = "720P"
        model2.url = url
        mSuperPlayerView.playWithModel(model2)
    }

    private fun initVideo() {
        TXLiveBase.setConsoleEnabled(false)
        //屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (videoUrl.isNotEmpty()) {
            setVideoPath(videoUrl)
        }

        mSuperPlayerView.setPlayerViewCallback(object : SuperPlayerView.OnSuperPlayerViewCallback {
            override fun onMoreView(type: Boolean) {
                clickShare()
            }

            override fun clickTypeShare(share: String) {
                clickShareType(share)
            }

            override fun clickRecommend(id: String, url: String) {
                refreshUrl(id, url)
            }

            override fun onClickSmallReturnBtn() {
                finish()
            }
        })
    }
}