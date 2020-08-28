package com.tinytiger.titi.ui.msg

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.MsgEvent
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.msg.IndexMsgCommentListBean
import com.tinytiger.common.net.data.msg.ReplyListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.msg.LikeUserAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.msg.MsgReplyContract
import com.tinytiger.titi.mvp.presenter.msg.MsgReplyPresenter
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import kotlinx.android.synthetic.main.activity_base_recycler.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 消息——点赞
 */
class LikeActivity : BaseBackActivity(), MsgReplyContract.View {
    override fun showComment(itemInfo: IndexMsgCommentListBean) {
        MessageCommentActivity.actionStart(this, content_id, comment_id, type, "0", JSON.toJSON(itemInfo).toString())
    }

    override fun getReplyList(mBean: ReplyListBean) {
        if (mBean.data.current_page == 1) {
            if (mBean.data.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getEmptyView(
                    this,
                    "",
                    recycler_view
                ))
                refreshLayout.setEnableLoadMore(false)
            } else {
                mAdapter.setNewInstance(mBean.data.data)
            }
        } else {
            mAdapter.addData(mBean.data.data)
        }

        if (mBean.data.current_page >= mBean.data.last_page) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.resetNoMoreData()
        }
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView( RefreshView.getNetworkView(this, recycler_view, { start() }))
            }
        }
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    override fun showLoading() {
        showProgress()
    }


    fun actionStart(context: Context) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
            ToastUtils.show(context, "无网络")
            return
        }
        if (MyUserData.isEmptyToken()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
        val intent = Intent(context, LikeActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.activity_base_recycler
    private val mPresenter by lazy { MsgReplyPresenter() }

    private val mAdapter by lazy { LikeUserAdapter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

    }

    private var content_id = ""
    private var comment_id = 0
    private var type = 0
    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        tvTitle.centerTxt = "点赞"
        tvTitle.setBackOnClick {
            finish()
        }

        recycler_view.adapter = mAdapter
        NIMClient.getService(MsgService::class.java).clearUnreadCount(
            getString(com.netease.nim.uikit.R.string.im_like),
            SessionTypeEnum.P2P
        )
        refreshLayout.setOnRefreshListener { _ ->
            page = 1
            start()

        }

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            start()
        }

        start()

        mAdapter.setOnItemClickListener { _, _, position ->
            val bean = mAdapter.data[position]
            bean.is_read = 1
            mAdapter.notifyItemChanged(position)
            content_id = bean.content_id.toString()
            comment_id = bean.id
            type = bean.type

            mPresenter.editLikeRead(comment_id,bean.type)

            when(bean.type){
                1->{
                    NewsDetailActivity.actionStart(this, bean.content_id.toString())
                }
                2->{
                    VideoDetailActivity.actionStart(this,  bean.content_id.toString(), "")
                }
                3->{
                    GameReviewsActivity.actionStart(this, bean.game_id, bean.assess_id)
                }
                4->{
                    PostActivity.actionStart(this,bean.post_id)
                }
                5->{
                    PostActivity.actionStart(this,bean.post_id)
                }
            }
        }
    }

    override fun start() {
        mPresenter.loadLikeList(page)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private var commentSize = 0
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(event: MsgEvent) {
        if (event.sessionId == getString(R.string.im_like)) {
            commentSize += event.msgSize
        }

        if (commentSize > 0) {
            setHeader()
        }
    }

    private fun setHeader() {
        mAdapter.removeAllHeaderView()
        val header = layoutInflater.inflate(
            R.layout.msg_item_news,
            recycler_view.parent as ViewGroup,
            false
        )
        val tv_title = header.findViewById<TextView>(R.id.tv_title)
        tv_title?.text = "您有$commentSize 条新消息"
        mAdapter.addHeaderView(tvTitle)

        tv_title?.setOnClickListener {
            commentSize = 0
            page = 1
            refreshLayout.autoRefresh()
            mAdapter.removeAllHeaderView()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserStatusEvent(event: UserStatusEvent) {
        if (event.type == -1) {

        } else {
            for (a in mAdapter.data.size - 1 until 0) {
                if (mAdapter.data[a].user_id.toString() == event.user_id) {
                    mAdapter.data.removeAt(a)
                }
            }
            mAdapter.notifyDataSetChanged()
        }
    }
}
