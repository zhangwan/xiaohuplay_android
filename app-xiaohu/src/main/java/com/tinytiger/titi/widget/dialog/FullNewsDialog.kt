package com.tinytiger.titi.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.video.CommentDetailBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ReplyDetailBean
import com.tinytiger.common.net.data.video.ReplysUserinfoBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.SoftKeyBoardListener
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.news.NewsTwoAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.data.comment.NewsTwoMulti
import com.tinytiger.titi.mvp.contract.news.CommentTwoContract
import com.tinytiger.titi.mvp.presenter.news.CommentTwoPresenter
import com.tinytiger.titi.utils.CheckUtils
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/6/28 0014 下午 3:12
 * @Copyright 小虎互联科技
 * @since 3.3.0
 * @doc 资讯二级评论
 */
class FullNewsDialog : BottomSheetDialogFragment(), CommentTwoContract.View {

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

    private val mAdapter by lazy { NewsTwoAdapter(ArrayList()) }
    private val mPresenter by lazy { CommentTwoPresenter() }

    private var mBehavior: BottomSheetBehavior<*>? = null
    /**
     * 一级评论
     */
    var topBean: CommentDetailBean? = null

    var sendBean: CommentDetailBean? = null

    private var page = 1
    //回复id
    var comment_id = ""

    //0: 最热排序, 1: 最新排序, 2: 最早排序
    var comment_type = 0
    /**
     * 评论总数
     */
    var total = 0

    /**
     * 顶部高度
     */
    var topHeight=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    private var vKeyBoard:View?=null
    private var recycler_view: RecyclerView? = null
    private var et_send: EditText? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_news_two, null)
        dialog.setContentView(view)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        if (topHeight>0){
            val layoutParams = view.layoutParams
            layoutParams.height = context!!.resources.displayMetrics.heightPixels -Dp2PxUtils.dip2px(context,topHeight)
            view.layoutParams = layoutParams
        }

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
        view.findViewById<View>(R.id.tv_title).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.tv_send).setOnClickListener { sendTxt() }
        vKeyBoard= view.findViewById(R.id.vKeyBoard)

        initData()
        return dialog
    }

    fun start() {
        mPresenter.indexComment(topBean!!.content_id, topBean!!.id, page, comment_type)
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }


    /**
     * 是否加载更多
     */
    private var LoadMore = false

    private fun initData() {
        mPresenter.attachView(this)

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

        mAdapter.mListener = object : NewsTwoAdapter.setOnNewsTwoListener {
            override fun onSelectClick(type: Int) {
                page = 1
                comment_type = type

                start()
            }

            override fun clickLike(id: Int, is_like: Int) {
                mPresenter.addCommentLike(is_like, topBean!!.content_id, id)
            }

            override fun clickDelete(id: Int) {
                TextDialog.create(childFragmentManager)
                    .setMessage("确认删除此评论吗？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            mPresenter.delComment(topBean!!.content_id, id)
                        }
                    })
                    .show()

            }

            override fun clickReply(bean: NewsTwoMulti) {
                sendBean = bean.recommendBean
                if (bean.recommendBean.id != topBean!!.id && SpUtils.getString(R.string.user_id,
                        "") != bean.recommendBean.replysUserinfo.user_id
                ) {
                    et_send?.hint = "@${bean.recommendBean.nickname}"
                } else {
                    et_send?.hint = ""
                }
                showKeyBord(et_send!!)
            }
        }
        mAdapter.userId = SpUtils.getString(R.string.user_id, "0")

        val user = ReplysUserinfoBean()
        user.nickname = topBean!!.nickname
        user.user_id = topBean!!.user_id
        user.avatar = topBean!!.avatar
        user.netease_id = topBean!!.netease_id
        user.master_type = topBean!!.master_type
        user.medal_image = topBean!!.medal_image
        topBean!!.replysUserinfo = user
        sendBean = topBean
        if (topBean != null) {
            mAdapter.addData(NewsTwoMulti(topBean, 1))
        }
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
        recycler_view?.post { mSoftKeyBoard.rootViewVisibleHeight=context!!.resources.displayMetrics.heightPixels }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
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
        mPresenter.addComment(topBean!!.content_id, sendBean!!, keyWord)
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
        if (topBean != null) {
            val replys = ArrayList<ReplyDetailBean>()
            if (mAdapter.data.size > 1) {
                val bean = mAdapter.data[1].recommendBean
                val item = ReplyDetailBean()
                item.replysUserinfo = bean.replysUserinfo
                item.commentUserinfo = bean.commentUserinfo
                item.content = bean.content
                item.create_time = bean.create_time
                replys.add(item)
            }
            if (mAdapter.data.size > 2) {
                val bean = mAdapter.data[2].recommendBean
                val item = ReplyDetailBean()
                item.replysUserinfo = bean.replysUserinfo
                item.commentUserinfo = bean.commentUserinfo
                item.content = bean.content
                item.create_time = bean.create_time
                replys.add(item)
            }
            topBean!!.replys = replys
            topBean!!.comment_num = total
            listener?.onDismiss(topBean!!.id)
        }
    }

    var listener: OnFullSheetListener? = null

    interface OnFullSheetListener {
        fun onDismiss(id: Int)
    }

    override fun showCommentDetail(bean: CommentListBean) {
        if (bean.data != null) {
            total = bean.data.total
            val list = ArrayList<NewsTwoMulti>()

            if (bean.data.data.size > 0) {
                for (i in bean.data.data) {
                    list.add(NewsTwoMulti(i))
                }
            }
            if (page==1){
                list.add(0,mAdapter.getItem(0))
                mAdapter.setNewInstance(list)
            }else{
                mAdapter.addData(list)
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
        mAdapter.setTitleSize(total)
    }

    override fun delComment(comment_id: Int) {
        if (comment_id == topBean!!.id) {
            topBean!!.parent_id = -1
            dismiss()
        } else {
            mAdapter.delComment(comment_id)
            total -= 1
            mAdapter.setTitleSize(total)
        }
    }

    override fun addCommentTxt(reply: CommentDetailBean) {
        mAdapter.addData(1, NewsTwoMulti(reply))
        et_send!!.setText("")
        sendBean = topBean
        total += 1
        mAdapter.setTitleSize(total)
    }
}