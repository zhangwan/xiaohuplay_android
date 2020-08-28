package com.tinytiger.titi.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.net.data.circle.post.PostDetailsBean
import com.tinytiger.common.net.data.circle.post.PostListBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.PostTwoAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.circle.post.PostContract
import com.tinytiger.titi.mvp.presenter.circle.post.PostPresenter
import com.tinytiger.titi.utils.CheckUtils
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 *
 * @author zhw_luke
 * @date 2020/5/14 0014 下午 3:12
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 帖子二级评论
 */
class FullPostReplyDialog : BottomSheetDialogFragment(), PostContract.View {

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

    private val mAdapter by lazy { PostTwoAdapter() }
    private val mPresenter by lazy { PostPresenter() }

    private var mBehavior: BottomSheetBehavior<*>? = null


    private var page = 1
    /**
     * post_id 帖子id
     */
    var id = "0"
    var topBean: PostData? = null
    private var vKeyBoard:View?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    private var recycler_view: RecyclerView? = null
    private var et_send: EditText? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_post_two, null)
        dialog.setContentView(view)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val layoutParams = view.layoutParams
        layoutParams.height = (context!!.resources.displayMetrics.heightPixels * 0.94).toInt()
        view.layoutParams = layoutParams


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
        vKeyBoard= view.findViewById(R.id.vKeyBoard)

        recycler_view = view.findViewById(R.id.recycler_view)
        recycler_view?.adapter = mAdapter
        et_send = view.findViewById(R.id.et_send)
        view.findViewById<ImageView>(R.id.iv_close_left).setOnClickListener { dismiss() }
        view.findViewById<TextView>(R.id.tv_send).setOnClickListener { sendTxt() }
        initData()

        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    var mSendComment: PostData? = null
    private fun initData() {
        mPresenter.attachView(this)

        mAdapter.mOnPostListener = object : PostTwoAdapter.OnPostListener {
            override fun clickLike(comment_id: String, is_like: Int) {
                mPresenter.likePostComment(comment_id)
            }

            override fun clickReply(item: PostData) {

                showKeyBord(et_send!!)

                if (item.id != topBean!!.id && SpUtils.getString(R.string.user_id, "") != item.user_id) {
                    et_send?.hint = "@${item.nickname}"
                    mSendComment = item
                } else {
                    mSendComment = null
                    et_send?.hint = ""
                }
            }

            override fun setDelete(item: PostData) {
                TextDialog.create(childFragmentManager)
                    .setMessage("确认删除此评论吗？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            mPresenter.delComment(item.id)
                        }
                    })
                    .show()
            }
        }
        if (topBean != null) {
            mAdapter.addData(topBean!!)
        }
        start()
        recycler_view?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    if (manager.findLastCompletelyVisibleItemPosition() == (manager.itemCount - 1) && LoadMore) {
                        page++
                        start()
                    }
                }
            }
        })

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
        recycler_view?.post { mSoftKeyBoard.rootViewVisibleHeight=context!!.resources.displayMetrics.heightPixels }
    }

    fun start() {
        mPresenter.subCommentList(topBean!!.id, page)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun getPostDetailsBean(mBean: PostDetailsBean) {

    }

    /**
     * 是否加载更多
     */
    private var LoadMore = false

    override fun getPostListBean(bean: PostListBean) {
        if (bean.data != null) {
            if (bean.data.current_page == 1) {
                if (bean.data.data.size > 0) {
                    mAdapter.addData(bean.data.data)
                } else {
                    mAdapter.setEmptyView(
                        RefreshView.getEmptyView(
                            activity,
                            "暂无数据",
                            recycler_view
                        )
                    )
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
        }
    }

    override fun showMutual(is_mutual: Int) {

    }

    override fun addCommentTxt(txt: String, commentId: Int) {
        var reply_nickname = ""
        if (mSendComment != null && mSendComment?.id != topBean?.id) {
            reply_nickname = mSendComment!!.nickname
        }
        et_send?.hint = ""
        et_send?.setText("")
        mAdapter.addCommentItem(txt, commentId, id, reply_nickname)

        val list = ArrayList<PostData>()
        if (mAdapter.data.size > 1) {
            list.add(mAdapter.data[1])
            if (mAdapter.data.size > 2) {
                list.add(mAdapter.data[2])
            }
        }
        topBean!!.comment_num += 1
        topBean!!.reply_list = list
        mAdapter.notifyItemChanged(0)
        mSendComment = null

        CircleAgentUtils.setCirclePostInfo(topBean!!.circle_id,id,2)
    }

    override fun delComment(comment_id: String) {
        mAdapter.delComment(comment_id)
        val list = ArrayList<PostData>()
        if (mAdapter.data.size > 1) {
            list.add(mAdapter.data[1])
            if (mAdapter.data.size > 2) {
                list.add(mAdapter.data[2])
            }
        }
        topBean!!.comment_num -= 1
        topBean!!.reply_count -= 1
        topBean!!.reply_list = list
        mAdapter.notifyItemChanged(0)
    }

    override fun replyAdoption(item: PostData,str:String) {
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
        if (mSendComment != null) {
            mPresenter.addComment(id, mSendComment!!.id, keyWord)
        } else {
            mPresenter.addComment(id, topBean!!.id, keyWord)
        }
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDismiss(topBean!!.id)
    }

    var listener: OnFullSheetListener? = null

    interface OnFullSheetListener {
        fun onDismiss(comment_id: String)
    }

}