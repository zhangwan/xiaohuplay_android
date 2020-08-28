package com.tinytiger.titi.ui.news

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.CommentDetailBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.video.NewsCommentAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.video.VideoContract
import com.tinytiger.titi.mvp.presenter.Video.VideoPresenter
import com.tinytiger.titi.ui.mine.other.ReportActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import com.tinytiger.titi.widget.dialog.FullNewsDialog
import com.tinytiger.titi.widget.popup.User2Popup
import kotlinx.android.synthetic.main.comment_preview_layout.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/3/11 0011 下午 5:37
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 评价详情
 */
class NewsCommentFragment : BaseFragment(), VideoContract.View, FullNewsDialog.OnFullSheetListener {

    private val mPresenter by lazy { VideoPresenter() }
    private val mAdapter by lazy { NewsCommentAdapter() }

    override fun showMutual(is_mutual: Int) {
    }

    private var mActivity: Activity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity
    }

    private var isShowFirstComment = false
    private var comment_total = 0
    private var content_id = ""
    private var comment_id = 0

    //排序类型 0: 最热排序, 1: 最新排序, 2: 最早排序
    var comment_type = 1

    //类型 0: 图文详情, 1: 视频详情
    var pageType = 1

    companion object {
        fun getInstance(content_id: String, pageType: Int = 0): NewsCommentFragment {
            val fragment = NewsCommentFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.content_id = content_id
            fragment.pageType = pageType

            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.comment_preview_layout


    override fun initView() {
        mPresenter.attachView(this)

        if (pageType == 0) {
            fl_content.minimumHeight =
                ScreenUtil.getScreenHeight() - ScreenUtil.dp2px(activity, 110f)
        } else {
            fl_content.minimumHeight =
                ScreenUtil.getScreenHeight() - ScreenUtil.dp2px(activity, 300f)
        }
        recycler_view.adapter = mAdapter
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.indexComment(content_id, page, comment_type)
        }

        mAdapter.mOnPostListener = object : NewsCommentAdapter.OnPostListener {
            override fun clickLike(comment_id: Int, is_like: Int) {
                mPresenter.addCommentLike(is_like, content_id, comment_id)
            }

            override fun setPostRead(item: CommentDetailBean) {
                //跳转二级评论列表
                item.content_id = content_id
                comment_total = item.comment_num
                FullNewsDialog().apply {
                    topBean = item
                    topHeight = 40
                    listener = this@NewsCommentFragment
                }.show(childFragmentManager)
            }

            override fun setDelete(item: CommentDetailBean) {
                clickDelete(item.id)
            }

            override fun setReply(replys: CommentDetailBean) {
                comment_id = replys.id
                var hint = ""
                if (SpUtils.getString(R.string.user_id, "") != replys.replysUserinfo.user_id) {
                    hint = "@${replys.replysUserinfo.nickname}"
                }
                mCommentClickListener?.showKeyBord(hint)
            }
        }
        //评论类型 0: 最热排序, 1: 最新排序, 2: 最早排序
        tvSelect1.setOnClickListener { selectCommentType(1) }
        tvSelect2.setOnClickListener { selectCommentType(2) }
        tvSelect3.setOnClickListener { selectCommentType(0) }
    }

    /**
     * 选择评论类型
     * @param type 评论类型 0: 最热排序, 1: 最新排序, 2: 最早排序
     */
    private fun selectCommentType(type: Int) {
        if (FastClickUtil.isFastClick()) {
            return
        }
        tvSelect1.setTextColor(
            if (type == 1) ContextCompat.getColor(context!!, R.color.color_ffcc03)
            else ContextCompat.getColor(context!!, R.color.grayAA)
        )
        tvSelect2.setTextColor(
            if (type == 2) ContextCompat.getColor(context!!, R.color.color_ffcc03)
            else ContextCompat.getColor(context!!, R.color.grayAA)
        )
        tvSelect3.setTextColor(
            if (type == 0) ContextCompat.getColor(context!!, R.color.color_ffcc03)
            else ContextCompat.getColor(context!!, R.color.grayAA)
        )
        page = 1
        comment_type = type
        start()
    }

    fun refreshAdapter() {
        mAdapter.notifyDataSetChanged()
    }

    fun sendCommend(commend: String) {
        mPresenter.addComment(content_id, comment_id, commend)
    }

    override fun start() {
        page = 1
        mPresenter.indexComment(content_id, page, comment_type)
    }

    private var author_id = "0"

    override fun showContentInfo(bean: ContentInfoBean.DataBean) {
        content_id = bean.id.toString()
        author_id = bean.user_id
        //        mAdapter.author_id = (bean.user_id)
        showCommentTitle(bean.comment_num)
        start()
    }

    fun showCommentTitle(comment_num: Int) {
        tv_title.text = "所有评论(${comment_num})"
    }

    override fun showComment(bean: CommentListBean.DataBean) {
        mAdapter.author_id = (bean.author_user_id)

        if (bean.current_page == 1) {
            mAdapter.setNewInstance(bean.data)
            if (bean.data.size == 0) {
//                mAdapter.setEmptyView(RefreshView.getEmptyView(activity, "", recycler_view))
            } else {
                refreshLayout.resetNoMoreData()
            }
        } else {
            mAdapter.addData(bean.data)

        }
        mAdapter.removeAllFooterView()
        if (bean.current_page >= bean.last_page) {
            refreshLayout.finishLoadMoreWithNoMoreData()
            mAdapter.addFooterView(RefreshView.getFooterView(activity, "", recycler_view), 0)
        } else {
            refreshLayout.finishLoadMore()
        }
    }


    private var mPopupWindow: User2Popup? = null


    fun clickMore(rootView: View, commentDetailBean: CommentDetailBean) {
        if (mPopupWindow == null) {
            mPopupWindow = User2Popup(context)
            mPopupWindow!!.setShowAnimation(
                AmintionUtils().createScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f)
            ).setDismissAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f))
            mPopupWindow!!.setPopupGravity(Gravity.BOTTOM)
        }

        mPopupWindow?.setReport(View.OnClickListener {
            mPopupWindow?.dismiss()
            if (MyUserData.isEmptyToken()) {
                if (mActivity is VideoDetailActivity) {
                    (mActivity as VideoDetailActivity).isRefresh = true
                }
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@OnClickListener
            }
            ReportActivity.actionStart(
                context!!, 2, commentDetailBean.id.toString(),
                commentDetailBean.user_id
            )
        })

        val location = IntArray(2)
        rootView.getLocationInWindow(location)
        rootView.getLocationOnScreen(location)

        mPopupWindow!!.setBackground(null).setBlurBackgroundEnable(false)
            .showPopupWindow(
                location[0] - Dp2PxUtils.dip2px(context, 60),
                location[1] + Dp2PxUtils.dip2px(context, 25)
            )
    }


    fun clickDelete(comment_id: Int) {
        if (MyUserData.isEmptyToken()) {
            if (mActivity is VideoDetailActivity) {
                (mActivity as VideoDetailActivity).isRefresh = true
            }
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
        TextDialog.create(childFragmentManager).isShowTitle(true).setMessage("确认删除此评论吗？")
            .setViewListener(object : TextDialog.ViewListener {
                override fun click() {
                    mPresenter.delComment(content_id, comment_id)
                }
            }).show()
    }

    override fun showDelComment(comment_id: Int) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].id == comment_id) {
                mCommentClickListener!!.refreshComment(-mAdapter.data[i].comment_num - 1, false)
                mAdapter.remove(i)
                if (mAdapter.data.size == 0) {
                    mAdapter.notifyDataSetChanged()
                }
                return
            }
        }
    }

    override fun showLikeStatus(is_like: Int, comment_id: Int) {
    }


    /**
     * 返回拉黑状态
     * @param is_black int
     * @param userId String
     */
    override fun showBlackStatus(is_black: Int, userId: String) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].user_id == userId) {
                mAdapter.data[i].is_black = is_black
                return
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    fun clickLike(comment_id: Int, is_like: Int) {
        if (MyUserData.isEmptyToken()) {
            if (mActivity is VideoDetailActivity) {
                (mActivity as VideoDetailActivity).isRefresh = true
            }

            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
    }


    override fun showIntroContentList(bean: ArrayList<RecommendBean>) {

    }


    override fun showResult() {
        mCommentClickListener?.refreshComment(1, true)
        comment_id = 0
        start()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() })
                )
            }
        }
        showToast(errorMsg)
    }

    override fun showLoading() {
        LoadingUtils.getInstance().show(context)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    private var mCommentClickListener: onCommentClickListener? = null

    fun setonCommentClickListener(clickListener: onCommentClickListener) {
        this.mCommentClickListener = clickListener
    }

    interface onCommentClickListener {
        fun refreshComment(num: Int, needScroll: Boolean)

        fun showKeyBord(nickname: String)
    }

    override fun onDismiss(id: Int) {
        for (i in 0..mAdapter.data.size - 1) {
            if (id == mAdapter.data[i].id) {
                if (mAdapter.data[i].parent_id == -1) {
                    mAdapter.remove(i)
                } else {
                    mAdapter.notifyItemChanged(i)
                }
                mCommentClickListener?.refreshComment(
                    mAdapter.data[i].comment_num - comment_total, false
                )
                return
            }
        }
    }

}
