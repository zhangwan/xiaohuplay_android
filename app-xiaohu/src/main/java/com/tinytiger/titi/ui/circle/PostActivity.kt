package com.tinytiger.titi.ui.circle

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import cn.jzvd.Jzvd
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.net.data.circle.post.PostDetailsBean
import com.tinytiger.common.net.data.circle.post.PostListBean
import com.tinytiger.common.utils.*
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.widget.MoreDialog
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.PostAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.circle.post.PostContract
import com.tinytiger.titi.mvp.presenter.circle.post.PostPresenter
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.circle.post.SelectFriendActivity
import com.tinytiger.titi.ui.video.VideoActivity
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.widget.dialog.FullPostReplyDialog
import com.tinytiger.titi.widget.popup.PopupInput
import com.tinytiger.titi.widget.video.MyJzVideoView
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView
import kotlinx.android.synthetic.main.post_activity_info.*
import kotlinx.android.synthetic.main.post_item_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 4:26
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 帖子详情
 */
class PostActivity : BaseBackActivity(), PostContract.View, PopupInput.OnPopupInputListener,
    FullPostReplyDialog.OnFullSheetListener, NetStateChangeObserver, VolumeChangeObserver.OnVolumeChangeListener {

    private val mAdapter by lazy { PostAdapter() }
    private val mPresenter by lazy { PostPresenter() }

    companion object {
        fun actionStart(context: Context, postid: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("postId", postid)
            context.startActivity(intent)
        }
    }

    private var postId = ""
    var videoLenght = 0L
    var videoUrl: String? = null
    var jzVideoView: MyJzVideoView? = null
    private var mVolumeChangeObserver: VolumeChangeObserver? = null
    override fun layoutId(): Int = R.layout.post_activity_info


    override fun initData() {
        setWindowFeature()
        mPresenter.attachView(this)
        postId = intent.getStringExtra("postId")

    }

    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        NetStateChangeReceiver.registerReceiver(this)
        NetStateChangeReceiver.registerObserver(this)
        mVolumeChangeObserver = VolumeChangeObserver(this)
        mVolumeChangeObserver?.setOnVolumeChangeListener(this)
        mVolumeChangeObserver?.registerVolumeReceiver()
        ivBack.setOnClickListener {
            finish()
        }

        ivMore.setOnClickListener {
            if (mPostDetailsBean == null) {
                return@setOnClickListener
            }

            if (!isLoginStart()) {
                return@setOnClickListener
            }

            MoreDialog.create(supportFragmentManager).apply {
                collectionType = mPostDetailsBean!!.data.is_collect
                report_type = 6
                contentId = postId
                user_id = mPostDetailsBean!!.data.user_id
            }.setOnItemClickListener(object : MoreDialog.OnItemClickListener {
                override fun click(type: Int) {
                    mPresenter.collectPost(circle_id,postId)
                }
            }).show()
        }
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            start()
        }

        recycler_view.adapter = mAdapter
        mPresenter.getPostInfo(this, postId)

        val dp210 = Dp2PxUtils.dip2px(this, 100)
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            ivTitle.alpha = 1 - scrollY.toFloat() / dp210
            if (scrollY > dp210) {
                avAttentionTitle.visibility = View.VISIBLE
                avUserTitle.visibility = View.VISIBLE
                tvNameTitle.visibility = View.VISIBLE
            } else {
                avAttentionTitle.visibility = View.GONE
                avUserTitle.visibility = View.GONE
                tvNameTitle.visibility = View.GONE
            }
        })


        mAdapter.mOnPostListener = object : PostAdapter.OnPostListener {
            override fun clickLike(comment_id: String, is_like: Int) {
                mPresenter.likePostComment(comment_id)
            }

            override fun setPostRead(item: PostData) {
                postRead(item)
            }

            override fun setDelete(item: PostData) {
                setCommentDelete(item.id)
            }

            override fun setReply(item: PostData) {
                TextDialog.create(supportFragmentManager).setMessage("每个问答贴只能采纳一个回答，确定采纳该回答吗？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            mPresenter.replyAdoption(item, postId)
                        }
                    }).show()
            }

        }

        val softKeyBoardListener = SoftKeyBoardListener(this)
        softKeyBoardListener.setListener(object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
            }

            override fun keyBoardHide(height: Int) {
                mPopupInput?.dismiss()
            }
        })

        et_send.setOnClickListener {
            setInput("")
        }

        ivShareBut.setOnClickListener {
            setShare()
        }
    }

    private fun setCommentDelete(commentId: String) {
        TextDialog.create(supportFragmentManager).setMessage("确认删除此评论吗？")
            .setViewListener(object : TextDialog.ViewListener {
                override fun click() {
                    mPresenter.delComment(commentId)
                }
            }).show()
    }


    //类型 1最热 2最新 3最早 默认2最新
    var comment_type = 2
    var circle_id = "0"
    override fun start() {
        mPresenter.allComments(postId, comment_type, page)
    }

    override fun getPostDetailsBean(mBean: PostDetailsBean) {
        getRefreshHeader(mBean)
        start()
        circle_id = mBean.data.circle_id
        videoUrl = mBean.data.video_url
        videoLenght = mBean.data.video_length
        CircleAgentUtils.setCirclePostInfo(circle_id, postId, 4)
        if (!TextUtils.isEmpty(videoUrl)) {
            rl_video.visibility = View.VISIBLE
            fl_content.visibility = View.VISIBLE
            ivView9.visibility = View.GONE
            when (mBean.data.status) {
                "0" -> {
                    fl_content.visibility = View.GONE
                    ivStart.visibility = View.GONE
                    tvStatusHint.visibility = View.VISIBLE
                    tvStatusHint.text = getString(R.string.post_video_audit_tip)
                }
                "1" -> {
                    setVidew()
                }
            }


        } else {
            ivView9.visibility = View.VISIBLE
            rl_video.visibility = View.GONE
        }

    }

    override fun getPostListBean(bean: PostListBean) {
        if (bean.data.current_page == 1) {
            if (bean.data != null && bean.data.data.size > 0) {
                if (mPostDetailsBean != null && mPostDetailsBean!!.data.answer_id > 0) {
                    for (i in 0..bean.data.data.size - 1) {
                        if (bean.data.data[i].id == "" + mPostDetailsBean!!.data.answer_id) {
                            bean.data.data.removeAt(i)
                            break
                        }
                    }
                }

                mAdapter.setNewInstance(bean.data.data)
            } else {
                mAdapter.setNewInstance(ArrayList())
                mAdapter.setEmptyView(RefreshView.getEmptyView(this, "暂无数据", recycler_view))
            }
        } else {
            if (mPostDetailsBean != null && mPostDetailsBean!!.data.answer_id > 0) {
                for (i in 0..bean.data.data.size - 1) {
                    if (bean.data.data[i].id == "" + mPostDetailsBean!!.data.answer_id) {
                        bean.data.data.removeAt(i)
                        break
                    }
                }
            }
            mAdapter.addData(bean.data.data)
        }

        mAdapter.removeAllFooterView()
        if (bean.data.current_page >= bean.data.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(this, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
    }

    override fun showMutual(is_mutual: Int) {

        mPostDetailsBean?.data!!.is_mutual = is_mutual
        avAttentionTitle.setMutual(is_mutual)
        avAttention.setMutual(is_mutual)
    }

    override fun addCommentTxt(txt: String, comment_id: Int) {
        mAdapter.addCommentItem(txt, comment_id, postId)
        comment_num += 1
        tvReadBut.text = StringUtils.sizeToString(comment_num)

        CircleAgentUtils.setCirclePostInfo(circle_id, postId, 2)
    }

    override fun delComment(comment_id: String) {
        if (mAdapter.answer_id > 0 && comment_id == mPostDetailsBean?.data!!.answer_info.id) {
            mAdapter.answer_id = 0
            mPostDetailsBean?.data!!.answer_id = 0
            mPostDetailsBean?.data!!.answer_info = null
            setReply()
            mAdapter.notifyDataSetChanged()
        } else {
            mAdapter.delComment(comment_id)
        }
        comment_num -= 1
        tvReadBut.text = StringUtils.sizeToString(comment_num)
    }

    override fun replyAdoption(item: PostData, replyAdoption: String) {
        if (!TextUtils.isEmpty(replyAdoption)) {
            tvInvite.visibility = View.GONE
            tvAnswerName.visibility = View.VISIBLE
            tvAnswerName.text = getString(R.string.post_answers_tip, replyAdoption)
        }

        mAdapter.answer_id = item.id.toInt()
        mPostDetailsBean?.data!!.answer_id = 1
        mPostDetailsBean?.data!!.answer_info = item
        setReply()
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].id == "" + mPostDetailsBean!!.data.answer_id) {
                mAdapter.data.removeAt(i)
                break
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun showCollect(is_collect: Int) {
        showToast(if (is_collect == 1) "收藏成功" else "取消收藏成功")
        mPostDetailsBean?.data!!.is_collect = is_collect
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    var comment_num = 0
    var mPostDetailsBean: PostDetailsBean? = null
    private fun getRefreshHeader(mBean: PostDetailsBean) {
        if (MyUserData.isEmptyToken()) {
            mBean.data.is_mutual = -1
        }
        mAdapter.answer_id = mBean.data.answer_id

        if (mBean.data.answer_id == -1) {
            tvPostTitle.text = "所有评论"
        }

        nestedScrollView.visibility = View.VISIBLE
        mPostDetailsBean = mBean
        if(mBean.data.user_info!=null){
            mAdapter.postUser = mBean.data.user_info.user_id
            avUserTitle.setAvatar(mBean.data.user_info.avatar)
            avUser.setAvatar(mBean.data.user_info.avatar, mBean.data.user_info.master_type)
                .setUserId(mBean.data.user_info.user_id)
            tvName.setNickname(mBean.data.user_info.nickname, mBean.data.user_info.medal_image)
            tvNameTitle.setNickname(mBean.data.user_info.nickname)
        }
        if (!TextUtils.isEmpty(mBean.data.title)) {
            tvTitle.text = mBean.data.title
            tvTitle.visibility = View.VISIBLE
        } else {
            tvTitle.visibility = View.GONE
        }

        var circleName = ""
        if (!TextUtils.isEmpty(mBean.data.circle_name)) {
            circleName = "${mBean.data.circle_name}"
        }
        circleName += if (!TextUtils.isEmpty(mBean.data.modular_name)) {
            if ("综合" == mBean.data.modular_name) {
                ""
            } else {
                if (!TextUtils.isEmpty(circleName)) {
                    "-${mBean.data.modular_name}"
                } else {
                    ""
                }
            }
        } else {
            ""
        }
        tvCircleName.text = circleName
        tvCircleName.visibility = View.VISIBLE
        if (TextUtils.isEmpty(circleName)) {
            tvCircleName.visibility = View.INVISIBLE
        }
        if (-1 == mBean.data.answer_id) { //综合
            tvAnswerName.visibility = View.GONE
            tvInvite.visibility = View.GONE
        } else {
            if (0 == mBean.data.answer_id) { //待解答
                tvAnswerName.visibility = View.GONE
                tvInvite.visibility = View.VISIBLE
            } else {
                tvInvite.visibility = View.GONE
                tvAnswerName.visibility = View.VISIBLE
                tvAnswerName.text = getString(R.string.post_answers_tip, mBean.data.participate_num)
            }
        }
        tvCircleName.setOnClickListener {
            CirclesDetailsActivity.actionStart(this, mBean.data.circle_id, mBean.data.modular_id,"")
        }
        tvInvite.setOnClickListener {
            SelectFriendActivity.actionStart(this, postId)
        }

        avAttentionTitle.setMutual(mBean.data.is_mutual)
        avAttentionTitle.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                mPresenter.doFollow(is_mutual, mBean.data.user_info.user_id)
            }
        }
        avAttention.setMutual(mBean.data.is_mutual)
        avAttention.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                mPresenter.doFollow(is_mutual, mBean.data.user_info.user_id)
            }
        }
        tvTime.text = TimeZoneUtil.getShortTimeShowString(mBean.data.create_time)

        val tabs = ArrayList<String>()
        if (mBean.data.circle_post != null && mBean.data.circle_post.size > 0) {
            for (i in mBean.data.circle_post) {
                if (mBean.data.circle_id != i.id) {
                    tabs.add("#${i.name}")
                }
            }
        }

        satvDesc.create(ShowAllTextView.Builder().setText(mBean.data.content).setLabelList(tabs).setType(
            ShowAllTextView.TEXT).setOnClickListener { _, index, _ ->
            CirclesDetailsActivity.actionStart(this, mBean.data.circle_post[index].id,"","")
        })

        if (mBean.data.img_url != null && mBean.data.img_url.size > 0) {
            ivView9.visibility = View.VISIBLE
            ivView9.setImages(mBean.data.img_url)
        } else {
            ivView9.visibility = View.GONE
        }

        tvSize.text = "${StringUtils.sizeToString(mBean.data.view_num)}次浏览"
        comment_num = mBean.data.comment_num
        tvReadBut.text = StringUtils.sizeToString(comment_num)

        viewLikeBut.setLike(mBean.data.is_like, mBean.data.like_num)
        viewLikeBut.mListener = LikeView.OnLikeViewListener {
            mPresenter.likePost(mBean.data.id)
            mBean.data.like_num = viewLikeBut.like_num
            mBean.data.is_like = viewLikeBut.is_like

            if (viewLikeBut.is_like == 1) {
                CircleAgentUtils.setCirclePostInfo(circle_id, postId, 1)
            }
        }

        tvSelect1.setOnClickListener {
            tvSelect1.setTextColor(ContextCompat.getColor(this, R.color.color_ffcc03))
            tvSelect2.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            tvSelect3.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            page = 1
            comment_type = 2
            start()
        }
        tvSelect2.setOnClickListener {
            tvSelect1.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            tvSelect2.setTextColor(ContextCompat.getColor(this, R.color.color_ffcc03))
            tvSelect3.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            page = 1
            comment_type = 3
            start()
        }
        tvSelect3.setOnClickListener {
            tvSelect1.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            tvSelect2.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            tvSelect3.setTextColor(ContextCompat.getColor(this, R.color.color_ffcc03))
            page = 1
            comment_type = 1
            start()
        }
        setReply()

    }


    private fun setReply() {
        if (mPostDetailsBean == null) {
            rlReply.visibility = View.GONE
            vView.visibility = View.GONE
            return
        }

        if (mPostDetailsBean!!.data.answer_id > 0) {
            val item = mPostDetailsBean!!.data.answer_info
            avUser1.setAvatar(item.avatar, item.master_type).setUserId(item.user_id)
            tvName1.setNickname(item.nickname, item.medal_image)

            rlReply.visibility = View.VISIBLE
            if (item.is_black == 1) {

                rlReply.background = ContextCompat.getDrawable(this, R.color.transparent_10)
                tvRead.visibility = View.GONE
                tvBack.visibility = View.VISIBLE
                vView.visibility = View.GONE
                viewLike.visibility = View.GONE
                vView2.visibility = View.GONE
                vView3.visibility = View.GONE
            } else {
                rlReply.background = ContextCompat.getDrawable(this, R.color.white)
                tvRead.visibility = View.VISIBLE
                tvBack.visibility = View.GONE
                vView.visibility = View.VISIBLE
                ivReply.visibility = View.VISIBLE
                viewLike.visibility = View.VISIBLE
                vView2.visibility = View.VISIBLE
                vView3.visibility = View.VISIBLE
                satvDesc1.create(ShowAllTextView.Builder().setText(item.content))
                tvTime1.text = TimeZoneUtil.getShortTimeShowString(item.create_time)

                tvRead.text = StringUtils.sizeToString(item.comment_num)
                tvRead.setOnClickListener {
                    postRead(item)
                }

                viewLike.setLike(item.is_like, item.like_num)
                viewLike.mListener = LikeView.OnLikeViewListener { mPresenter.likePostComment(item.id) }

                if (item.reply_list != null && item.reply_list.size > 0) {
                    llComment.visibility = View.VISIBLE
                    setllComment(item)
                } else {
                    llComment.visibility = View.GONE
                }
            }
            rlReply.setOnClickListener {
                postRead(item)
            }


            if (item.user_id == SpUtils.getString(R.string.user_id, "")) {
                tvDelete.visibility = View.VISIBLE
                tvDelete.setOnClickListener {
                    setCommentDelete(item.id)
                }
            } else {
                tvDelete.visibility = View.GONE
            }

        } else {
            rlReply.visibility = View.GONE
            vView.visibility = View.GONE
        }
    }

    /**
     * 问答贴二级评论
     */
    private fun setllComment(bean: PostData) {
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()
        val item = bean.reply_list
        for (i in item) {
            val view = View.inflate(this, R.layout.post_item_comment_two, null)
            GlideUtil.loadImg(view.findViewById(R.id.ivIcon), i.avatar)
            val tvName = view.findViewById<TextView>(R.id.tvName)
            if (!i.reply_nickname.isNullOrEmpty()) {
                val nickname = StringUtils.stringName(i.nickname)
                val sb = SpannableString("$nickname 回复 @${i.reply_nickname}")
                sb.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.gray33)), nickname.length,
                    nickname.length + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvName.text = sb
            } else {
                tvName.text = i.nickname
            }
            view.findViewById<TextView>(R.id.tvTime).text = TimeZoneUtil.getShortTimeShowString(i.create_time)
            val tvDesc = view.findViewById<ShowAllTextView>(R.id.tvInfo)
            tvDesc.create(ShowAllTextView.Builder().setText(i.content))
            llComment.addView(view)
        }

        if (bean.comment_num > 2) {
            val llMore = llComment[1].findViewById<View>(R.id.llMore)
            llMore.visibility = View.VISIBLE
            llMore.setOnClickListener {
                postRead(bean)
            }
            llComment[1].findViewById<TextView>(R.id.tvMore).text = "共${bean.comment_num}条回复 >"
        }
    }


    private var mPopupInput: PopupInput? = null
    private fun setInput(hint: String) {
        if (mPopupInput == null) {
            mPopupInput = PopupInput(this, this)
        }
        mPopupInput?.setHint(hint)
        mPopupInput!!.setAdjustInputMethod(true).setAutoShowInputMethod(mPopupInput!!.findViewById(R.id.et_send), true)
            .showPopupWindow()
    }

    override fun clickInput(str: String) {
        mPresenter.addComment(postId, null, str)
    }

    private fun setShare() {
        ShareDialog.create(supportFragmentManager).apply {
            class_name = "Post"
            share_url =
                mPostDetailsBean?.data!!.share_url + "?post_id=${postId}&comment_id=${mPostDetailsBean?.data!!.id}"
            share_title = "来自「${mPostDetailsBean?.data!!.user_info.nickname}」"
            share_desc = mPostDetailsBean?.data!!.content

            if (mPostDetailsBean?.data!!.img_url != null && mPostDetailsBean?.data!!.img_url.size > 0) {
                share_image = mPostDetailsBean?.data!!.img_url[0]
            }

            contentId = mPostDetailsBean?.data!!.id
            id = mPostDetailsBean?.data!!.circle_id
        }.show()

//        CircleAgentUtils.setCirclePostInfo(circle_id, postId, 3)
    }

    private fun postRead(item: PostData) {
        FullPostReplyDialog().apply {
            id = postId
            item.circle_id = circle_id
            topBean = item
            listener = this@PostActivity
        }.show(supportFragmentManager, "dialog")
    }

    override fun onDismiss(comment_id: String) {
        if (mPostDetailsBean != null && mPostDetailsBean!!.data.answer_info != null && mPostDetailsBean!!.data.answer_info.id == comment_id) {
            setReply()
        } else {
            for (i in 0..(mAdapter.data.size - 1)) {
                if (mAdapter.data[i].id == comment_id) {
                    mAdapter.notifyItemChanged(i)
                }
            }
        }
    }

    /**
     * 黑名单列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserStatusEvent(event: UserStatusEvent) {
        if (mPostDetailsBean != null && mPostDetailsBean!!.data != null && mPostDetailsBean!!.data.answer_info != null && mPostDetailsBean!!.data.answer_info.user_id == event.user_id) {
            mPostDetailsBean!!.data.answer_info.is_black = event.type
            setReply()
        }
    }


    fun setVidew() {
        jzVideoView=MyJzVideoView(this)
        jzVideoView?.setUp(videoUrl, "", 0)
        Thread(Runnable {
            Thread.sleep(30)
            jzVideoView?.post {
                jzVideoView?.startButton?.performClick()
            }
        }).start()
        fl_content.addView(jzVideoView,
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        jzVideoView?.setClickUi(object : MyJzVideoView.ClickUi {
            override fun onClickUiToggle() {
                var share_url =
                    mPostDetailsBean?.data!!.share_url + "?post_id=${postId}&comment_id=${mPostDetailsBean?.data!!.id}"
                var share_title = "来自「${mPostDetailsBean?.data!!.user_info.nickname}」"
                var share_desc = mPostDetailsBean?.data!!.content
                var share_image = ""
                if (mPostDetailsBean?.data!!.img_url != null && mPostDetailsBean?.data!!.img_url.size > 0) {
                    share_image = mPostDetailsBean?.data!!.img_url[0]
                }
                var shareEvent = ShareEvent(share_url, share_title, share_desc, share_image)
                VideoActivity.actionStart(this@PostActivity, videoUrl!!, 1, shareEvent)
            }

            override fun onClickStart() {

            }

            override fun onShareClick() {
                setShare()
            }

        })


    }

    override fun onPause() {
        super.onPause()
        if (Jzvd.CURRENT_JZVD != null) {
        }
        Jzvd.goOnPlayOnPause()
    }

    override fun onResume() {
        super.onResume()
        if (jzVideoView!=null&&!TextUtils.isEmpty(videoUrl)) {
            jzVideoView?.startButton?.performClick()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        jzVideoView=null
        fl_content.removeAllViews()
        mVolumeChangeObserver?.unregisterVolumeReceiver()
        NetStateChangeReceiver.unRegisterObserver(this)
        NetStateChangeReceiver.unRegisterReceiver(this)
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
        jzVideoView?.setType(type)
    }

    override fun onVolumeChange(volume: Int) {
        jzVideoView?.setVolumeChange(volume)
    }


}