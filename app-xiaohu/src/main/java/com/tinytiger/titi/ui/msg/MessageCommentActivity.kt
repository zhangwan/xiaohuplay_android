package com.tinytiger.titi.ui.msg

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.msg.IndexMsgCommentListBean
import com.tinytiger.common.net.data.msg.MsgCommentBean
import com.tinytiger.common.net.data.msg.UserBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.msg.MsgCommentAdapter
import com.tinytiger.titi.im.config.preference.Preferences
import com.tinytiger.titi.mvp.contract.video.MsgCommentContract
import com.tinytiger.titi.mvp.presenter.Video.MsgCommentPresenter
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import com.tinytiger.titi.ui.video.view.SoftEditTextUtils
import com.tinytiger.titi.utils.CheckUtils
import com.tinytiger.titi.widget.view.Anim.LikeView
import kotlinx.android.synthetic.main.activity_message_comment.*

/**
 *
 * @author zhw_luke
 * @date 2019/12/15 0015 下午 4:23
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc im评论->评论详情页
 */
class MessageCommentActivity : BaseBackActivity(), MsgCommentContract.View {

    override fun showBlackStatus(is_black: Int, user_id: String) {
        for (i in mAdapter.data) {
            if (i.replysUserinfo.user_id == user_id) {
                i.is_black = is_black
            }
        }
        if (is_black == 1) {
            showToast("拉黑成功")
        } else {
            showToast("取消拉黑成功")
        }
        supportFragmentManager
    }

    override fun delComment(comment_id: Int) {
        showToast("删除成功")

        if (comment_id == mTopCommentId) {
            finish()
        } else {
            for (i in 0..(mAdapter.data.size - 1)) {
                if (mAdapter.data[i].id == comment_id) {
                    mAdapter.remove(i)
                    break
                }
            }
        }
    }

    override fun addCommentTxt(txt: String, comment_id: Int) {
        val user = UserBean()
        user.user_id = SpUtils.getString(R.string.user_id, "")
        user.nickname = SpUtils.getString(R.string.nickname, "")
        user.netease_id = Preferences.getUserAccount()
        user.avatar = SpUtils.getString(R.string.avatar, "")

        val bean = MsgCommentBean(
            comment_id,
            TimeZoneUtil.formatTime2(System.currentTimeMillis()),
            txt,
            user
        )
        mAdapter.addData(0, bean)

        mHandler.postDelayed({
            RefreshView.smoothMoveToPosition(recycler_view, 0)
        }, 500)
    }

    override fun isLike(islike: Int, comment_id: Int) {
        if (comment_id == mTopCommentId) {
            val bean = recommendBean!!
            bean.is_like = islike
            bean.like_num += islike
        }
    }

    override fun showComment(bean: IndexMsgCommentListBean.DataBean) {
        if (gt_or_lt == 0) {
            //向下取
            if (page == 1) {
                mAdapter.author_user_id = bean.author_user_id
                recommendBean = bean.top_comment_info
                getRefreshHeader()

                mAdapter.setNewInstance(bean.data)
                if (bean.current_comment_info != null) {
                    mAdapter.top_comment_id = bean.current_comment_info.id
                    mAdapter.addData(0, bean.current_comment_info)
                    mSendCommentId = bean.current_comment_info.id
                    et_send.hint = "@${bean.current_comment_info.replysUserinfo.nickname}"
                }
                //自动加载第一次,最新数据
                page1++
                if (mCommentId==0){
                    gt_or_lt = 1
                    start()
                }

            } else {
                mAdapter.addData(bean.data)
            }
            if (bean.current_page >= bean.last_page) {
                mAdapter.addFooterView(RefreshView.getFooterView(this, "", recycler_view), 0)
                refreshLayout.setEnableLoadMore(false)
            }
        } else {
            //向上取
            mAdapter.addData(0, bean.data)

            if (bean.current_page >= bean.last_page) {
                refreshLayout.setEnableRefresh(false)
            }
        }
    }

    //内容id
    private var mContentId = "0"
    //评论id
    private var mCommentId: Int = 0
    //内容分类id
    private var type = 0
    private var json = ""
    //当前一级评论id
    private var mTopCommentId: Int = 0
    //发送评论id
    private var mSendCommentId: Int = 0

    private var recommendBean: MsgCommentBean? = null

    companion object {
        fun actionStart(context: Context, content_id: String, comment_id: Int, type: Int, game_id: String, data: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }

            val intent = Intent(context, MessageCommentActivity::class.java)
            intent.putExtra("content_id", content_id)
            intent.putExtra("comment_id", comment_id)
            intent.putExtra("type", type)
            intent.putExtra("game_id", game_id)
            intent.putExtra("json", data)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.activity_message_comment

    private val mPresenter by lazy { MsgCommentPresenter() }
    private val mAdapter by lazy { MsgCommentAdapter() }

    init {
        mPresenter.attachView(this)
    }


    override fun initData() {
        if (intent.hasExtra("content_id")) {
            mContentId = intent.getStringExtra("content_id")
        }
        mCommentId = intent.getIntExtra("comment_id", 0)
        type = intent.getIntExtra("type", 0)
        if (intent.hasExtra("json")) {
            json = intent.getStringExtra("json")
        }

        if (intent.hasExtra("game_id")) {
            game_id = intent.getStringExtra("game_id")
        }

    }

    override fun initView() {
        title_view.centerTxt = "评论详情"
        title_view.setBackOnClick {
            finish()
        }

        title_view.rightTxt="查看原文"
        title_view.setRightTxtColor(ContextCompat.getColor(this,R.color.color_ffcc03))
        title_view.setRightOnClick {
            //0=点评,1=图文资讯,2=视频资讯,4=帖子
            when(type){
                0->{
                    GameReviewsActivity.actionStart(this,game_id,mContentId)
                }
                1->{
                    NewsDetailActivity.actionStart(this, mContentId)
                }
                2->{
                    VideoDetailActivity.actionStart(this, mContentId, "")
                }
                4->{
                    PostActivity.actionStart(this,mContentId)
                }
            }
        }

        //发送
        tv_send.setOnClickListener {
            sendTxt()
        }
        tv_send.isClickable = false
        mAdapter.setOnExpandClickListener(mOnExpandClickListener)
        refreshLayout.setEnableOverScrollDrag(true)

        recycler_view.adapter = mAdapter

        refreshLayout.setOnRefreshListener { _ ->
            page1++
            gt_or_lt = 1
            start()
        }
        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            gt_or_lt = 0
            start()
        }

        if (json.isNotEmpty()) {
            val bean = JSON.parseObject(json, IndexMsgCommentListBean::class.java)
            showComment(bean.data)
        } else {
            showLoading()
            page = 1
            start()
        }

        et_send.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et_send.text.toString().isEmpty()) {
                    tv_send.isClickable = false
                    tv_send.background =
                        ContextCompat.getDrawable(this@MessageCommentActivity, R.drawable.stroke_rectangle_15_aaaaaa)
                } else {
                    tv_send.isClickable = true
                    tv_send.background =
                        ContextCompat.getDrawable(this@MessageCommentActivity, R.drawable.solid_gradient_15_ffcc03)
                }
            }
        })

    }

    private var game_id = "0"
    private var gt_or_lt = 0
    private var page1 = 0
    override fun start() {
        if (gt_or_lt == 0) {
            //向下取
            mPresenter.indexMsgComment(mContentId, mCommentId,type,game_id , page,gt_or_lt)
        } else {
            //向上取
            mPresenter.indexMsgComment(mContentId, mCommentId,type,game_id , page1,gt_or_lt)
        }
    }

    private val mOnExpandClickListener = object : MsgCommentAdapter.OnExpandClickListener {
        override fun clickDelete(comment_id: Int) {
            TextDialog.create(supportFragmentManager)
                .setMessage("确认删除此评论吗？")
                .setViewListener(object : TextDialog.ViewListener {
                    override fun click() {
                        //#类型:0=点评,1=图文资讯,2=视频资讯,4=帖子  [点评的评论才有此字段]
                        when(type){
                            0->{
                                mPresenter.delCommentMe(comment_id)
                            }
                            1->{
                                mPresenter.delComment(mContentId, comment_id)
                            }
                            2->{
                                mPresenter.delComment(mContentId, comment_id)
                            }
                            4->{
                                mPresenter.delCommentPost(comment_id)
                            }
                        }
                    }
                })
                .show()
        }

        override fun clickLike(comment_id: Int, is_like: Int) {
            when(type){
                0->{
                    mPresenter.handleLike(game_id, mContentId, comment_id)
                }
                1->{
                    mPresenter.loadLike(is_like, mContentId, comment_id)
                }
                2->{
                    mPresenter.loadLike(is_like, mContentId, comment_id)
                }
                4->{
                    mPresenter.likePostComment(""+comment_id)
                }
            }

        }

        override fun clickReply(comment_id: Int, reply_name: String) {
            mSendCommentId = comment_id
            showKeyBord(et_send)
            if (comment_id == mTopCommentId) {
                et_send.hint = ""
            } else {
                et_send.hint = "@$reply_name"
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (SoftEditTextUtils.isSoftShowing(this) && SoftEditTextUtils.isShouldHideInput(view, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private fun sendTxt() {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }

        if (!isLoginStart()) {
            return
        }

        val keyWord = et_send.text.toString().trim()
        if (keyWord.isEmpty()) {
            showToast("请输入评论")
            return
        }
        if (keyWord.length > 1000) {
            showToast("字数限制最多1000字，请调整后再发")
            return
        }

        if (CheckUtils.checkLocalAntiSpam(keyWord)) {
            showToast("含有敏感词汇")
            et_send.setText("")
            return
        }

        closeKeyBord(et_send)
        et_send.setText("")
        if (mSendCommentId == 0) {
            mSendCommentId = mTopCommentId
        }

        when(type){
            0->{
                mPresenter.addComment(game_id, mContentId, ""+mSendCommentId, keyWord)
            }
            1->{
                mPresenter.addComment(mContentId, mSendCommentId, keyWord)
            }
            2->{
                mPresenter.addComment(mContentId, mSendCommentId, keyWord)
            }
            4->{
                mPresenter. addCommentPost(mContentId, ""+mSendCommentId, keyWord)
            }
        }

    }

    /**
     * 关闭软键盘
     */
    private fun closeKeyBord(mEditText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
        mEditText.setCursorVisible(false)

    }

    /**
     * 自动弹软键盘
     *
     * @param context
     * @param et
     */
    fun showKeyBord(editText: EditText) {
        editText.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
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
        if (errorCode == 10002 || errorCode == 1005) {
            showNoCommentDialog(errorMsg)
        } else {
            showToast(errorMsg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private fun showNoCommentDialog(errorMsg: String) {
        TextDialog
            .create(supportFragmentManager)
            .isShowTitle(true)
            .setShowButton(isCancel = false, isConfirm = true)
            .isCancelOutside(false)
            .setMessage(errorMsg)
            .setDismissListener(object : TextDialog.DismissListener {
                override fun onDismiss() {
                    finish()
                }
            })
            .show()
    }

    private fun getRefreshHeader() {
        val bean = recommendBean!!
        avUser.setAvatar(bean.replysUserinfo.avatar,bean.replysUserinfo.master_type)

        mTopCommentId = bean.id
        mSendCommentId = mTopCommentId
        var nickname = "该用户已注销"
        if (bean.replysUserinfo != null && bean.replysUserinfo.nickname != null) {
            nickname = bean.replysUserinfo.nickname
            if (nickname.length > 8) {
                nickname = nickname.substring(0, 8) + "..."
            }
            comment_item_userName.setNickname(nickname,bean.replysUserinfo.medal_image)
        }else{
            comment_item_userName.setNickname(nickname)
        }
        comment_item_content.text = bean.content
        comment_item_time.text = TimeZoneUtil.getShortTimeShowString(bean.create_time)

        if (bean.has_badge == 1) {
            comment_item_hot.visibility = View.VISIBLE
        } else {
            comment_item_hot.visibility = View.GONE
        }

        viewLike.setLike(bean.is_like, bean.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            mOnExpandClickListener.clickLike(bean.id, bean.is_like)
        }

        if (bean.replysUserinfo.user_id == SpUtils.getString(R.string.user_id, "0")) {
            comment_item_more.visibility=View.VISIBLE
        } else {
            comment_item_more.visibility=View.GONE
        }

        if (bean.is_author == 1) {
            comment_item_author.text = "作者"
            comment_item_author.visibility = View.VISIBLE
        } else if (bean.is_lz == 1) {
            comment_item_author.text = "楼主"
            comment_item_author.visibility = View.VISIBLE
        } else {
            comment_item_author.visibility = View.GONE
        }

        when (bean.replysUserinfo.user_id) {
            mAdapter.author_user_id -> {
                comment_item_author.text = "作者"
                comment_item_author.visibility = View.VISIBLE
            }
            bean.top_parent_user_id -> {
                comment_item_author.text = "楼主"
                comment_item_author.visibility = View.VISIBLE
            }
            else -> {
                comment_item_author.visibility = View.GONE
            }
        }

        comment_item_userName.setOnClickListener {
            HomepageActivity.actionStart(this, bean.replysUserinfo.user_id)
        }

        comment_item_more.setOnClickListener {
            if (bean.replysUserinfo.user_id == SpUtils.getString(R.string.user_id, "0")) {
                mOnExpandClickListener.clickDelete(bean.id)
            }
        }
        comment_item_content.setOnClickListener {
            mOnExpandClickListener.clickReply(bean.id, bean.replysUserinfo.nickname)
        }
    }

}
