package com.tinytiger.titi.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.PostBean

import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.net.data.circle.post.PostDetailsBean
import com.tinytiger.common.net.data.circle.post.PostListBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.PostAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.circle.post.PostContract
import com.tinytiger.titi.mvp.presenter.circle.post.PostPresenter
import com.tinytiger.titi.utils.CheckUtils
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/5/14 0014 下午 3:12
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 帖子一级级评论
 */
class FullPostDialog : BottomSheetDialogFragment(), PostContract.View,
    FullPostReplyDialog.OnFullSheetListener {

    fun showToast(str: String) {
        ToastUtils.toshort(context, str)
    }

    override fun showLoading() {
        LoadingUtils.getInstance().show(activity)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
    }

    fun show(manager: FragmentManager) {
        if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
            ToastUtils.show(BaseApp._instance, "无网络")
        } else {
            show(manager, "FullPostDialog")
        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == 10002 || errorCode == 1005) {
            showNoCommentDialog(errorMsg)
        } else {
            ToastUtils.toshort(context, errorMsg)
        }
    }

    private val mAdapter by lazy { PostAdapter() }
    private val mPresenter by lazy { PostPresenter() }

    private var mBehavior: BottomSheetBehavior<*>? = null

    var postBean: PostBean? = null

    private var page = 1
    //回复id
    var comment_id = ""

    //类型 1最热 2最新 3最早 默认2最新
    var comment_type = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    private var recycler_view: RecyclerView? = null
    private var et_send: EditText? = null

    private var vKeyBoard:View?=null
    private var rl_content:View?=null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_post_one, null)
        dialog.setContentView(view)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

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

        recycler_view = view.findViewById(R.id.recycler_view)
        recycler_view?.adapter = mAdapter
        et_send = view.findViewById(R.id.et_send)
        vKeyBoard= view.findViewById(R.id.vKeyBoard)
        rl_content= view.findViewById(R.id.rl_content)
        view.findViewById<View>(R.id.tv_title).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.tv_send).setOnClickListener { sendTxt() }
        val tvSelect1 = view.findViewById<TextView>(R.id.tvSelect1)
        val tvSelect2 = view.findViewById<TextView>(R.id.tvSelect2)
        val tvSelect3 = view.findViewById<TextView>(R.id.tvSelect3)

        tvSelect1.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            tvSelect1.setTextColor(ContextCompat.getColor(context!!, R.color.color_ffcc03))
            tvSelect2.setTextColor(ContextCompat.getColor(context!!, R.color.grayAA))
            tvSelect3.setTextColor(ContextCompat.getColor(context!!, R.color.grayAA))
            page = 1
            comment_type = 2
            start()
        }

        tvSelect2.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            tvSelect1.setTextColor(ContextCompat.getColor(context!!, R.color.grayAA))
            tvSelect2.setTextColor(ContextCompat.getColor(context!!, R.color.color_ffcc03))
            tvSelect3.setTextColor(ContextCompat.getColor(context!!, R.color.grayAA))
            page = 1
            comment_type = 3
            start()
        }

        tvSelect3.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            tvSelect1.setTextColor(ContextCompat.getColor(context!!, R.color.grayAA))
            tvSelect2.setTextColor(ContextCompat.getColor(context!!, R.color.grayAA))
            tvSelect3.setTextColor(ContextCompat.getColor(context!!, R.color.color_ffcc03))
            page = 1
            comment_type = 1
            start()
        }

        initData()
        return dialog
    }

    fun start() {
        mPresenter.allComments(postBean!!.id, comment_type, page)
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    var mSendComment: PostData? = null

    /**
     * 是否加载更多
     */
    private var LoadMore = false

    private fun initData() {
        mPresenter.attachView(this)

        mAdapter.mOnPostListener = object : PostAdapter.OnPostListener {
            override fun clickLike(comment_id: String, is_like: Int) {
                mPresenter.likePostComment(comment_id)
            }

            override fun setPostRead(item: PostData) {
                if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                    ToastUtils.show(context, "无网络")
                } else {
                    FullPostReplyDialog().apply {
                        id = postBean!!.id
                        topBean = item
                        topBean?.circle_id = postBean!!.circle_id
                        listener = this@FullPostDialog
                    }.show(childFragmentManager, "FullPostReplyDialog")
                }
            }

            override fun setDelete(item: PostData) {
                TextDialog.create(childFragmentManager).setMessage("确认删除此评论吗？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            mPresenter.delComment(item.id)
                        }
                    }).show()
            }

            override fun setReply(item: PostData) {
                TextDialog.create(childFragmentManager).setMessage("每个问答贴只能采纳一个回答，确定采纳该回答吗？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            mPresenter.replyAdoption(item, postBean!!.id)
                        }
                    }).show()
            }
        }

        recycler_view?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    if (LoadMore && manager.findLastCompletelyVisibleItemPosition() == (manager.itemCount - 1)) {
                        page++
                        start()
                    }
                }
            }
        })
        mAdapter.postUser = postBean!!.user_id
        mAdapter.answer_id = postBean!!.answer_id
        start()


       val mSoftKeyBoard= SoftKeyBoardListener(activity)
        mSoftKeyBoard.setListener(object :
            SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                val lp=vKeyBoard?.layoutParams
                lp?.height=height
                vKeyBoard?.layoutParams=lp
                vKeyBoard?.visibility=View.VISIBLE
            }

            override fun keyBoardHide(height: Int) {
                vKeyBoard?.visibility=View.GONE
            }
        })
        rl_content?.post { mSoftKeyBoard.rootViewVisibleHeight=rl_content!!.height }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun getPostDetailsBean(mBean: PostDetailsBean) {

    }

    var total = 0
    override fun getPostListBean(bean: PostListBean) {
        if (bean.data != null) {
            if (bean.data.current_page == 1) {
                total = bean.data.all_comment_num
                if (bean.data.data.size > 0) {
                    mAdapter.setNewInstance(bean.data.data)
                } else {
                    mAdapter.setEmptyView(RefreshView.getEmptyView(activity, "暂无数据", recycler_view))
                }
            } else {
                mAdapter.addData(bean.data.data)
            }

            mAdapter.removeAllFooterView()
            if (bean.data.current_page >= bean.data.last_page) {
                mAdapter.addFooterView(RefreshView.getNewFooterView(activity, "", recycler_view), 0)
                LoadMore = false
            } else {
                LoadMore = true
            }
        } else {
            mAdapter.removeAllFooterView()
            mAdapter.addFooterView(RefreshView.getNewFooterView(activity, "", recycler_view), 0)
            LoadMore = false
        }
    }

    override fun showMutual(is_mutual: Int) {

    }

    override fun addCommentTxt(txt: String, commentId: Int) {
        mSendComment = null

        total += 1
        et_send?.setText("")
        comment_id = ""
        mAdapter.addCommentItem(txt, commentId, postBean!!.id)
        RefreshView.smoothMoveToPosition(recycler_view, 0)

        CircleAgentUtils.setCirclePostInfo(postBean!!.circle_id, postBean!!.id, 2)
    }

    override fun delComment(comment_id: String) {
        mAdapter.delComment(comment_id)
        total -= 1
    }

    override fun replyAdoption(item: PostData, str: String) {
        postBean!!.answer_id = item.id.toInt()
        mAdapter.answer_id = item.id.toInt()
        mAdapter.notifyDataSetChanged()
    }

    override fun showCollect(is_collect: Int) {

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

        if (CheckUtils.checkLocalAntiSpam(keyWord)) {
            showToast("含有敏感词汇")
            et_send?.setText("")
            return
        }

        closeKeyBord(et_send!!)
        mPresenter.addComment(postBean!!.id, null, keyWord)
    }

    private fun closeKeyBord(mEditText: EditText) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    private fun showKeyBord(editText: EditText) {
        editText.requestFocus()
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showNoCommentDialog(errorMsg: String) {
        TextDialog.create(childFragmentManager).isShowTitle(true)
            .setShowButton(isCancel = false, isConfirm = true).isCancelOutside(false)
            .setMessage(errorMsg).setDismissListener(object : TextDialog.DismissListener {
                override fun onDismiss() {
                    this.onDismiss()
                }
            }).show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (postBean != null) {
            postBean!!.comment_num = total
            listener?.onDismiss(postBean!!.id)
        }
    }

    var listener: OnFullSheetListener? = null

    interface OnFullSheetListener {
        fun onDismiss(id: String)
    }

    override fun onDismiss(comment_id: String) {
        if (mAdapter.data.size > 0) {
            for (i in 0..(mAdapter.data.size - 1)) {
                if (mAdapter.data[i].id == comment_id) {
                    mAdapter.notifyItemChanged(i)
                }
            }
        }
    }

}