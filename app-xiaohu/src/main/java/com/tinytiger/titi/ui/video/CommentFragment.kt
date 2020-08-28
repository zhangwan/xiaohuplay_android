package com.tinytiger.titi.ui.video

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.msg.CommentBeanMulti
import com.tinytiger.common.net.data.video.CommentDetailBean
import com.tinytiger.common.net.data.video.CommentDetailListBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ReplyDetailBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.video.CommentDetailAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.video.CommentContract
import com.tinytiger.titi.mvp.presenter.Video.CommentPresenter
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.mine.other.ReportActivity
import com.tinytiger.titi.utils.CheckUtils
import com.tinytiger.titi.widget.popup.User2Popup
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import kotlinx.android.synthetic.main.base_recycler.*
import kotlinx.android.synthetic.main.fragment_comment.*
import org.greenrobot.eventbus.EventBus
import java.util.*


/*
* @author Tamas
* create at 2019/11/12 0012
* Email: ljw_163mail@163.com
* description: Fragment 评论
*/
class CommentFragment : BaseFragment(), CommentContract.View,
    CommentDetailAdapter.OnExpandClickListener {


    private var mContentId: String = "0"

    // 0 VIDEO  1 NEWS
    private var mType = 0


    companion object {
        fun getInstance(content_id: String, type: Int): CommentFragment {
            val fragment = CommentFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mContentId = content_id
            fragment.mType = type
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_comment

    private val mPresenter by lazy { CommentPresenter() }

    private val mList = ArrayList<MultiItemEntity>()

    private var mAdapter: CommentDetailAdapter? = null

    override fun initView() {
        mPresenter.attachView(this)

        if (mType == 0) {
            rl_video.visibility = View.VISIBLE
            rl_news.visibility = View.GONE
        } else {
            rl_video.visibility = View.GONE
            rl_news.visibility = View.VISIBLE
        }

        iv_close.setOnClickListener(mOnClickListener)
        iv_close_news.setOnClickListener(mOnClickListener)
        ivLike.setOnClickListener(mOnClickListener)
        tv_send.setOnClickListener(mOnClickListener)

        mAdapter = CommentDetailAdapter(mList)
        mAdapter?.setOnExpandClickListener(this)

        recycler_view.setLayoutManager(LinearLayoutManager(mContext))
        recycler_view.setAdapter(mAdapter)

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            mPresenter.indexCommentDetail(mContentId, commentDetail!!.id, page + 1)
        }


        val softKeyBoardListener = SoftKeyBoardListener(activity)
        softKeyBoardListener.setListener(object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                changeSoftInput(true)
            }

            override fun keyBoardHide(height: Int) {
                changeSoftInput(false)
            }

        })
    }


    private fun changeSoftInput(isSend: Boolean) {
        ivLike.visibility = if (isSend) View.GONE else View.VISIBLE
        tv_send.visibility = if (isSend) View.VISIBLE else View.GONE
    }

    private var cur_comment_id = 0
    private var commentDetail: CommentDetailBean? = null

    fun setComment(commentBean: CommentDetailBean, comment_id: Int, content_user_id: String, is_rely: Boolean) {
        page = 1
        if (cur_comment_id != commentBean.id) {
            cur_comment_id = commentBean.id
            reply_comment_id = comment_id
            this.commentDetail = commentBean
            mAdapter!!.lz_id = commentBean.user_id
            mAdapter!!.author_id = content_user_id
            ivLike.setImageResource(if (commentBean.is_like == 1) R.mipmap.icon_like_2 else R.mipmap.icon_like)
        }
        mPresenter.indexCommentDetail(mContentId, comment_id, 1)
        if (is_rely && commentBean.nickname != null) {
            mHandler.postDelayed({
                clickReply(comment_id, "",commentBean.user_id)
            }, 400)
        }
    }


    private var reply_comment_id = 0
    private var reply_comment_name = ""


    override fun clickReply(comment_id: Int, reply_name: String,reply_user_id: String) {
        if(reply_comment_id !=comment_id){
            et_send.setText("")
        }
        reply_comment_id = comment_id

        if (reply_name.isEmpty()) {
            et_send.hint = "回复@${commentDetail!!.nickname}"
            reply_comment_name = ""
        } else {

            val text =  when(reply_user_id){
                mAdapter!!.author_id->"(作者)"
                mAdapter!!.lz_id->"(楼主)"
                else ->""
            }
            et_send.hint = "回复@$reply_name"
            reply_comment_name = reply_name+text
        }


        showKeyBord(et_send)
    }


    override fun showCommentDetail(bean: CommentDetailListBean.DataBean) {

        refreshLayout.finishLoadMore()
        if (bean.total == 0) {
            mList.clear()
            mList.add(commentDetail!!)
            mList.add(CommentBeanMulti(3, "无评论"))
        } else {
            if (bean.current_page == 1) {
                mList.clear()
                mList.add(commentDetail!!)
                mList.add(CommentBeanMulti(2, "全部评论"))
                mList.addAll(bean.data)
            } else {
                mList.addAll(bean.data)
            }
            setTotalNumber(bean.total)
        }
        page = bean.current_page

        mAdapter!!.notifyDataSetChanged()


        if (mAdapter!!.footerLayoutCount == 1) {
            mAdapter!!.removeAllFooterView()
        }
        if (bean.current_page >= bean.last_page) {
            if (mAdapter!!.data.size != 0) {
                mAdapter!!.addFooterView(RefreshView.getFooterView(context, "已显示全部评论", recycler_view), 0)
            }
            refreshLayout.setEnableLoadMore(false)
            refreshLayout.setEnableOverScrollDrag(true)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
    }




    private fun setTotalNumber(total: Int) {

        tv_title.text = "评论详情(${total})"
        tv_title_news.text = "${total}条回复"
        commentDetail!!.comment_num = total
        mAdapter!!.notifyItemChanged(0)
    }


    override fun addCommentTxt(reply: ReplyDetailBean) {

        //无评论
        if (mList.size == 2) {
            mAdapter!!.remove(1)
            mList.add(CommentBeanMulti(2, "全部评论"))
        }
        mAdapter!!.addData(2, reply)

        setTotalNumber(commentDetail!!.comment_num + 1)
        recycler_view.smoothScrollToPosition(0)
    }


    override fun showDelComment(position: Int) {
        if (position == 0) {
            onFinish()
            mClickListener?.clickBack()
        } else {
            if (position < mList.size) {
                mAdapter!!.remove(position)
                if (mAdapter!!.data.size == 2) {
                    mAdapter!!.remove(1)
                    mAdapter!!.addData(CommentBeanMulti(3, "无评论"))
                }
            }

            setTotalNumber(commentDetail!!.comment_num - 1)

        }
    }


    private val mOnClickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.iv_close, R.id.iv_close_news -> {
                onFinish()
                mClickListener?.clickBack()
            }
            R.id.ivLike -> {
                if (MyUserData.isEmptyToken()) {
                    isRefresh = true
                    EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    return@OnClickListener
                }
                mPresenter.addCommentLike(commentDetail!!.is_like, mContentId, commentDetail!!.id)
            }
            R.id.tv_send -> {
                sendMessage()
            }
        }
    }


    private fun sendMessage() {
        if (FastClickUtil.isFastClickTiming())
            return

        if (MyUserData.isEmptyToken()) {
            isRefresh = true
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
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


        mPresenter.addComment(mContentId, reply_comment_id, keyWord, reply_comment_name)
        closeKeyBord(et_send)
        et_send.setText("")

        et_send.hint = ""
        reply_comment_id = cur_comment_id
        reply_comment_name = ""

    }


     fun onFinish() {
        ivLike.setImageResource(R.mipmap.icon_like)
        tv_title.text = "评论详情"
        tv_title_news.text = "评论详情"
        et_send.setText("")
        mList.clear()
        page = 1
        mAdapter?.notifyDataSetChanged()
    }

    override fun showResult() {

        mPresenter.indexCommentDetail(mContentId, commentDetail!!.id, 1)
    }


    override fun clickUser(user_id: String) {

        if (activity is VideoDetailActivity) {
            (activity as VideoDetailActivity).isRefresh = true
        }
        HomepageActivity.actionStart(context!!, user_id)
    }


    private var mPopupWindow: User2Popup? = null

    override fun clickMore(rootView: View, comment_id: Int, user_id: String, netease_id: String?, is_black: Int) {

        if (mPopupWindow == null) {
            mPopupWindow = User2Popup(context)
            mPopupWindow!!.setShowAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f))
                .setDismissAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f))
            mPopupWindow!!.setPopupGravity(Gravity.BOTTOM)
        }

        mPopupWindow?.setReport(View.OnClickListener {
            mPopupWindow?.dismiss()
            if (MyUserData.isEmptyToken()) {
                isRefresh = true
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@OnClickListener
            }
            ReportActivity.actionStart(
                context!!,
                2,
                comment_id.toString(),user_id
            )
        })

        val location = IntArray(2)
        rootView.getLocationInWindow(location)
        rootView.getLocationOnScreen(location)

        mPopupWindow!!.setBackground(null)
            .setBlurBackgroundEnable(false)
            .showPopupWindow(
                location[0] - Dp2PxUtils.dip2px(context, 60),
                location[1] + Dp2PxUtils.dip2px(context, 25)
            )
    }

    private var isRefresh = false

    override fun clickLike(comment_id: Int, is_like: Int) {
        mPresenter.addCommentLike(is_like, mContentId, comment_id)
    }

    override fun clickDelete(position: Int, comment_id: Int) {
        if (MyUserData.isEmptyToken()) {
            isRefresh = true
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
        TextDialog.create(childFragmentManager)
            .isShowTitle(true)
            .setMessage("确认删除此评论吗？")
            .setViewListener(object : TextDialog.ViewListener {
                override fun click() {
                    mPresenter.delComment(position, mContentId, comment_id)
                }
            })
            .show()
    }


    override fun showBlackStatus(is_black: Int, userId: String) {

        for (reply in mList) {
            when (reply.itemType) {
                0 -> {
                    val bean = reply as CommentDetailBean
                    if (bean.user_id == userId) {
                        bean.is_black = is_black
                    }
                }
                1 -> {
                    val bean = reply as ReplyDetailBean
                    if (bean.user_id == userId) {
                        bean.is_black = is_black
                    }
                }
                else -> {

                }
            }
        }
        mAdapter!!.notifyDataSetChanged()
    }


    override fun showLikeStatus(is_like: Int, comment_id: Int) {
    }

    override fun onResume() {
        super.onResume()
        if(isRefresh){
            isRefresh = false
            mAdapter!!.notifyDataSetChanged()
        }
    }

    override fun start() {


    }

    override fun showComment(bean: CommentListBean.DataBean) {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)

    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishLoadMore()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    private var mClickListener: OnCommentClickListener? = null

    fun setOnCommentClickListener(click: OnCommentClickListener) {
        this.mClickListener = click
    }

    interface OnCommentClickListener {
        fun clickBack()
    }


    /**
     * 关闭软键盘
     */
    private fun closeKeyBord(mEditText: EditText) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)

        ll_bottom.requestFocus()
    }

    /**
     * 自动弹软键盘
     *
     * @param context
     * @param et
     */
    private fun showKeyBord(editText: EditText) {
        editText.requestFocus()
        val imm = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

}
