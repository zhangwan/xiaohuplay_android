package com.tinytiger.titi.widget.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.GameReviewAdapter2
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.news.CommentInfoContract
import com.tinytiger.titi.mvp.presenter.news.CommentInfoPresenter
import com.tinytiger.titi.utils.CheckUtils
import kotlinx.android.synthetic.main.comment_preview_layout.view.*
import org.greenrobot.eventbus.EventBus


/**
 * @author lmq001
 * @date 2020/6/9 下午 3:12
 * @Copyright 小虎互联科技
 * @since 3.2.0
 * @doc 帖子一、二级评论
 */
class FullCommentPreview : RelativeLayout, FullPostReplyDialog.OnFullSheetListener,
    CommentInfoContract.View {

    private var postBean: PostBean? = null
    private var activity: AppCompatActivity? = null
    private var page = 1
    private var tvTitle: TextView? = null
    private var recycler_view: RecyclerView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var et_send: EditText? = null
    var mSendComment: PostData? = null

    //页面类型的评论  1-图文/视频详情页（默认） 2-点评页
    var pageType = 1

    //页面标题
    var title: String? = null

    //传递参数id
    var id = ""

    //设置列表是否可以刷新,默认不可以
    var enableScroll = false

    //是否显示底部发送区域
    var enableSend = false

    //页面返回回调监听
    private var listener: OnFullSheetListener? = null

    //回复id
    var comment_id = ""

    //类型 1最热 2最新 3最早 默认2最新
    var comment_type = 2

    // 是否加载更多
    private var LoadMore = false

    private val mPresenter by lazy { CommentInfoPresenter() }
    private val mAdapter by lazy { GameReviewAdapter2() }

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context?) {
        this.activity = context as AppCompatActivity
        initView()
        initListener()
    }

    /**
     * 页面类型的评论  1-图文/视频详情页（默认） 2-点评页
     */
    fun setPageType(type: Int): FullCommentPreview {
        this.pageType = type
        return this
    }

    /**
     * 设置传递的参数id
     */
    fun setId(id: String): FullCommentPreview {
        this.id = id
        return this
    }

    /**
     * 设置传递的参数
     */
    fun setParamData(item: PostBean): FullCommentPreview {
        this.postBean = item
        return this
    }

    /**
     * 设置页面标题
     */
    fun setTitle(title: String): FullCommentPreview {
        this.title = title
        return this
    }

    /**
     * 设置列表是否可以滑动
     */
    fun setEnableScroll(scroll: Boolean): FullCommentPreview {
        this.enableScroll = scroll
        return this
    }

    /**
     * 是否显示底部发送区域
     */
    fun setEnableSend(enableSend: Boolean): FullCommentPreview {
        this.enableSend = enableSend
        return this
    }

    /**
     * 设置退出回调监听
     */
    fun setOnFullSheetListener(listener: OnFullSheetListener): FullCommentPreview {
        this.listener = listener
        return this
    }

    /**
     * 开始加载数据
     */
    fun start() {
        //图文/视频详情页
        if (pageType == 1 && id.isNotEmpty()) {
            mPresenter.indexComment(id, page)
        }
    }

    private fun initView() {
        mPresenter.attachView(this)
        LayoutInflater.from(activity).inflate(R.layout.comment_preview_layout, this)

//        ll_bottom.visibility = if (enableSend) View.VISIBLE else View.GONE
        if (this.title != null && this.title!!.isNotEmpty()) {
            tvTitle!!.text = this.title
        }

        recycler_view?.adapter = mAdapter
        refreshLayout?.setEnableRefresh(enableScroll)
        refreshLayout?.setEnableRefresh(false)
        refreshLayout?.setOnLoadMoreListener {
//            page++
//            mPresenter.indexComment(content_id, page)
        }
//        mAdapter.user_id = SpUtils.getString(R.string.user_id, "")


        tv_title.setOnClickListener { }
//        tv_send.setOnClickListener { sendTxt() }
        //评论类型 1最热 2最新 3最早
        tvSelect1.setOnClickListener { selectCommentType(2) }
        tvSelect2.setOnClickListener { selectCommentType(3) }
        tvSelect3.setOnClickListener { selectCommentType(1) }
    }

    /**
     * 选择评论类型
     * @param type 类型 1最热 2最新 3最早 默认2最新
     */
    private fun selectCommentType(type: Int) {
        if (FastClickUtil.isFastClick()) {
            return
        }
        tvSelect1.setTextColor(
            if (type == 2) ContextCompat.getColor(context!!, R.color.color_ffcc03)
            else ContextCompat.getColor(context!!, R.color.grayAA)
        )
        tvSelect2.setTextColor(
            if (type == 3) ContextCompat.getColor(context!!, R.color.color_ffcc03)
            else ContextCompat.getColor(context!!, R.color.grayAA)
        )
        tvSelect3.setTextColor(
            if (type == 1) ContextCompat.getColor(context!!, R.color.color_ffcc03)
            else ContextCompat.getColor(context!!, R.color.grayAA)
        )
        page = 1
        comment_type = type
        start()
    }

    fun showToast(str: String) {
        ToastUtils.toshort(context, str)
    }

    override fun showLoading() {
        LoadingUtils.getInstance().show(activity!!)
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

    private fun initListener() {

    }

//    var total = 0
//    override fun getPostListBean(bean: PostListBean) {
//        if (bean.data != null) {
//            if (bean.data.current_page == 1) {
//                total = bean.data.all_comment_num
//                if (bean.data.data.size > 0) {
//                    mAdapter.setNewInstance(bean.data.data)
//                } else {
//                    mAdapter.setEmptyView(RefreshView.getEmptyView(activity, "暂无数据", recycler_view))
//                }
//            } else {
//                mAdapter.addData(bean.data.data)
//            }
//
//            mAdapter.removeAllFooterView()
//            if (bean.data.current_page >= bean.data.last_page) {
//                mAdapter.addFooterView(RefreshView.getNewFooterView(activity, "", recycler_view), 0)
//                LoadMore = false
//            } else {
//                LoadMore = true
//            }
//        } else {
//            mAdapter.removeAllFooterView()
//            mAdapter.addFooterView(RefreshView.getNewFooterView(activity, "", recycler_view), 0)
//            LoadMore = false
//        }
//    }

    override fun showContentInfo(bean: ContentInfoBean.DataBean) {
        TODO("Not yet implemented")
    }

    override fun showComment(bean: CommentListBean.DataBean) {
        TODO("Not yet implemented")
    }

    override fun showLikeStatus(is_like: Int, comment_id: Int) {
        TODO("Not yet implemented")
    }

    override fun showIntroContentList(bean: ArrayList<RecommendBean>) {
        TODO("Not yet implemented")
    }

    override fun showResult() {
        TODO("Not yet implemented")
    }

    override fun showMutual(is_mutual: Int) {
        TODO("Not yet implemented")
    }

    override fun showBlackStatus(is_black: Int, userId: String) {
        TODO("Not yet implemented")
    }

    override fun showDelComment(comment_id: Int) {
        TODO("Not yet implemented")
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
//        mPresenter.addComment(postBean!!.id, null, keyWord)
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
        TextDialog.create(activity!!.supportFragmentManager).isShowTitle(true)
            .setShowButton(isCancel = false, isConfirm = true).isCancelOutside(false)
            .setMessage(errorMsg).setDismissListener(object : TextDialog.DismissListener {
                override fun onDismiss() {
                    this.onDismiss()
                }
            }).show()
    }

    /**
     * 退出时调用，用于刷新上个页面
     */
    fun onDismiss() {
        mPresenter.detachView()
        if (postBean != null) {
//            postBean!!.comment_num = total
            listener?.onDismiss(postBean!!.id)
        }
    }

    interface OnFullSheetListener {
        fun onDismiss(id: String)
    }

    override fun onDismiss(comment_id: String) {
        if (mAdapter.data.size > 0) {
//            for (i in 0..(mAdapter.data.size - 1)) {
//                if (mAdapter.data[i].id == comment_id) {
//                    mAdapter.notifyItemChanged(i)
//                }
//            }
        }
    }

}