package com.tinytiger.titi.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orhanobut.logger.Logger
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.UserInfo
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.dialog.TextDialog

import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.GameCommentTwoAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.im.config.preference.Preferences
import com.tinytiger.titi.mvp.contract.gametools.GameCommentContract
import com.tinytiger.titi.mvp.presenter.gametools.GameCommentPresenter
import com.tinytiger.titi.ui.mine.other.ReportActivity
import com.tinytiger.titi.utils.CheckUtils
import com.tinytiger.titi.widget.popup.User2Popup
import org.greenrobot.eventbus.EventBus

/**
 *
 * @Author luke
 * @Date 2020-03-03 20:44
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 二级评论详情
 *
 */
class FullSheetReplyDialog : BottomSheetDialogFragment(), GameCommentContract.View {

    override fun showAssessList(bean: CommentAssessInfo) {
        mAdapter.addData(bean.data.data)
        if (bean.data.current_page >= bean.data.last_page) {
            mAdapter.addFooterView(RefreshView.getFooterView(activity, "- - - 人家是一只有底线的小脑斧 - - -", recycler_view), 0)
            LoadMore = false
        } else {
            LoadMore = true
        }
    }

    override fun delComment(comment_id: Int) {
        showToast("删除成功")
        if (comment_id == mTopCommentId) {
            listener?.onDismiss(comment_id)
            deltype = false
            dismiss()
        } else {
            for (i in 0..(mAdapter.data.size - 1)) {
                if (mAdapter.data[i].id == comment_id) {
                    mAdapter.remove(i)
                    break
                }
            }
            mBeanTop!!.comment_num-=1
            mAdapter.notifyItemChanged(0)
        }
    }

    override fun isLike(islike: Int, comment_id: Int) {

    }

    override fun addCommentTxt(txt: String, comment_id: Int) {
        val bean = CommentAssessBean()
        bean.user_id = SpUtils.getString(R.string.user_id, "")
        bean.nickname = SpUtils.getString(R.string.nickname, "")
        bean.netease_id = Preferences.getUserAccount()
        bean.avatar = SpUtils.getString(R.string.avatar, "")
        bean.create_time = TimeZoneUtil.formatTime2(System.currentTimeMillis())
        bean.content = txt
        bean.lz_user_id = mBeanTop?.lz_user_id
        bean.id = comment_id
        bean.is_like=-1

        val  user=UserInfo()
        user.user_id = SpUtils.getString(R.string.user_id, "")
        user.nickname = SpUtils.getString(R.string.nickname, "")
        user.netease_id = Preferences.getUserAccount()
        user.avatar = SpUtils.getString(R.string.avatar, "")
        bean.replysUserinfo=user
        user.medal_image=SpUtils.getString(R.string.medal_image,"")
        user.master_type=SpUtils.getInt(R.string.master_type,0)

        if (commentUserinfo != null && commentUserinfo!!.user_id != user.user_id) {
            bean.commentUserinfo = commentUserinfo
        }

        mAdapter.addData(1, bean)
        mBeanTop!!.comment_num+=1
        mAdapter.notifyItemChanged(0)
    }

    fun showToast(str: String) {
        ToastUtils.toshort(context, str)
    }

    override fun showLoading() {
        LoadingUtils.getInstance().show(activity)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == 10002 || errorCode == 1005) {
            showNoCommentDialog(errorMsg)
        } else {
            ToastUtils.toshort(context, errorMsg)
        }
    }

    private val mPresenter by lazy { GameCommentPresenter() }
    private val mAdapter by lazy { GameCommentTwoAdapter() }

    private var mBehavior: BottomSheetBehavior<*>? = null
    private var tv_title: TextView? = null
    private var recycler_view: RecyclerView? = null
    private var et_send: EditText? = null

    var page = 1

    //游戏id
    var gameId = ""
    //评价id
    var mContentId = ""
    //一级id
    var mTopCommentId = 0

    var mBeanTop: CommentAssessBean? = null

    //选中回复id
    var mSendCommentId = 0


    /**
     * 是否回调二级评论
     */
    var deltype = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_reply, null)
        dialog.setContentView(view)

        tv_title = view.findViewById(R.id.tv_title)
        recycler_view = view.findViewById(R.id.recycler_view)
        et_send = view.findViewById(R.id.et_send)


        mBehavior = BottomSheetBehavior.from(view.parent as View)
        mBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss()
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(view: View, v: Float) {

            }
        })
        recycler_view?.adapter = mAdapter
        view.findViewById<ImageView>(R.id.iv_close_left).setOnClickListener { dismiss() }
        view.findViewById<TextView>(R.id.tv_send).setOnClickListener { sendTxt() }

        val layoutParams = view.layoutParams
        layoutParams.height = (context!!.resources.displayMetrics.heightPixels * 0.94).toInt()
        view.layoutParams = layoutParams
        initData()

        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private var LoadMore = false
    private fun initData() {
        mPresenter.attachView(this)

        mTopCommentId = mBeanTop!!.id
        mAdapter.addData(mBeanTop!!)
        mPresenter.indexAssessComment(gameId, mContentId, "" + mBeanTop?.id, page)

        mAdapter.setOnExpandClickListener(mOnExpandClickListener)
        recycler_view?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    if (manager.findLastCompletelyVisibleItemPosition() == (manager.itemCount - 1) && LoadMore) {
                        page++
                        mPresenter.indexAssessComment(gameId, mContentId, "" + mBeanTop?.id, page)
                    }
                }
            }
        })
    }

    /**
     * 关闭软键盘
     */
    private fun closeKeyBord(mEditText: EditText) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    /**
     * 自动弹软键盘
     *
     * @param context
     * @param et
     */
    private fun showKeyBord(editText: EditText) {
        editText.requestFocus()
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        if (mDemoPopup != null) {
            mDemoPopup!!.dismiss()
            mDemoPopup = null
        }
    }

    private fun showNoCommentDialog(errorMsg: String) {
        deltype=false
        TextDialog
            .create(childFragmentManager)
            .isShowTitle(true)
            .setShowButton(isCancel = false, isConfirm = true)
            .isCancelOutside(false)
            .setMessage(errorMsg)
            .setDismissListener(object : TextDialog.DismissListener {
                override fun onDismiss() {
                    this.onDismiss()
                }
            })
            .show()
    }
    private var commentUserinfo: UserInfo? = null
    private var mDemoPopup: User2Popup? = null
    private fun showPop(view: View, bean: CommentAssessBean) {
        if (mDemoPopup == null) {
            mDemoPopup = User2Popup(activity)
            mDemoPopup!!.setShowAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f))
                .setDismissAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f))
            mDemoPopup!!.setPopupGravity(Gravity.BOTTOM)
        }

        mDemoPopup!!.setReport(View.OnClickListener {
            mDemoPopup!!.dismiss()

            ReportActivity.actionStart(
                activity!!,
                2,
                bean.id.toString(),bean.user_id
            )
        })

        val location = IntArray(2)
        view.getLocationInWindow(location)
        view.getLocationOnScreen(location)

        mDemoPopup!!.setBackground(null)
            .setBlurBackgroundEnable(false)
            .showPopupWindow(
                location[0] - Dp2PxUtils.dip2px(activity, 60),
                location[1] + Dp2PxUtils.dip2px(activity, 25)
            )
    }

    private val mOnExpandClickListener = object : GameCommentTwoAdapter.OnExpandClickListener {
        override fun clickDelete(comment_id: Int) {
            TextDialog.create(childFragmentManager)
                .setMessage("确认删除此评论吗？")
                .setViewListener(object : TextDialog.ViewListener {
                    override fun click() {
                        mPresenter.delCommentMe(comment_id)
                    }
                })
                .show()
        }

        override fun clickReply(item: CommentAssessBean) {
            mSendCommentId = item.id
            if (mTopCommentId!=mSendCommentId){
                commentUserinfo = item.replysUserinfo
            }
            showKeyBord(et_send!!)
            if (item.id == mTopCommentId) {
                et_send?.hint = ""
            } else {
                et_send?.hint = "@${item.nickname}"
            }
        }

        override fun clickMore(view: View, item: CommentAssessBean) {
            showPop(view, item)
        }

        override fun clickLike(comment_id: Int, is_like: Int) {
            mPresenter.handleLike(gameId, mContentId, comment_id)
        }

    }

    private fun sendTxt() {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }

        if (MyUserData.isEmptyToken()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }

        val keyWord = et_send!!.text.toString().trim()
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
            et_send?.setText("")
            return
        }

        closeKeyBord(et_send!!)
        et_send?.setText("")
        if (mSendCommentId == 0) {
            mSendCommentId = mTopCommentId
        }

        mPresenter.addComment(gameId, mContentId, "" + mSendCommentId, keyWord)
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        /**
         * 回调更新二级评论
         */
        if (deltype && listener != null ) {
            val list = ArrayList<CommentAssessBean>()
            if (mAdapter.data.size > 1){
                var size = mAdapter.data.size-1

                for (i in 1..size) {
                    list.add(mAdapter.data[i])
                }
            }

            listener?.onAddList(mBeanTop!!.id, list)
        }
    }


    var listener: OnFullSheetListener? = null

    interface OnFullSheetListener {
        fun onDismiss(comment_id: Int)
        fun onLike(comment_id: Int, islike: Int)
        fun onAddList(comment_id: Int, list: ArrayList<CommentAssessBean>)
    }

}