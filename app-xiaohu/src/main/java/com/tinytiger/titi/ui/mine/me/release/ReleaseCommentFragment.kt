package com.tinytiger.titi.ui.mine.me.release

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.home2.ReleaseCommentAdapter
import com.tinytiger.titi.mvp.contract.center.MineWorkContract
import com.tinytiger.titi.mvp.presenter.center.MineWorksPresenter
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.utils.UIHelper
import kotlinx.android.synthetic.main.activity_base_recycler.*
import org.greenrobot.eventbus.EventBus

/**
 * @author zwy
 * create at 2020/5/19 0019
 * description: 个人中心-我的发布-点评
 */
class ReleaseCommentFragment : BaseFragment(), MineWorkContract.View {

    private val mPresenter by lazy { MineWorksPresenter() }
    private val mAdapter by lazy { ReleaseCommentAdapter() }
    override fun getLayoutId(): Int = R.layout.release_fragment_comment
    var count = 0
    var menuVisible: Boolean = true
    override fun initView() {
        mPresenter.attachView(this)
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        initListener()
    }

    private fun initListener() {
        mAdapter.addChildClickViewIds(
            R.id.ll_content,
            R.id.rlGameInfo,
            R.id.rl_game,
            R.id.ll_title,
            R.id.llReply,
            R.id.iv_game, R.id.tvName, R.id.ratingBar,
            R.id.tvRead,
            R.id.tvShare
        )
        mAdapter.setOnItemChildClickListener { _, view, position ->
            if (activity?.tvTitle?.rightTxt == "完成" || activity?.tvTitle?.rightTxt == "删除") {
                mAdapter.data[position].isSelected = !mAdapter.data[position].isSelected
                mAdapter.notifyItemChanged(position)

                if (mAdapter.data[position].isSelected) {
                    activity?.tvTitle?.rightTxt = "删除"
                    activity?.tvTitle?.setRightTxtColor(Color.parseColor("#FF556E"))
                } else {
                    var is_select = false
                    for (item in mAdapter.data) {
                        if (item.isSelected) {
                            is_select = true
                        }
                    }
                    if (!is_select) {
                        activity?.tvTitle?.rightTxt = "完成"
                        activity?.tvTitle?.setRightTxtColor(Color.parseColor("#0FB50A"))
                    }
                }

            } else {
                when (view.id) {
                    R.id.ll_content -> {
                        if (mAdapter.showDeleteIcon) {
                            mAdapter.data[position].isSelected = mAdapter.data[position].isSelected
                            mAdapter.notifyItemChanged(position)
                        } else {
                            GameReviewsActivity.actionStart(
                                context!!,
                                mAdapter.data[position].game_id,
                                mAdapter.data[position].id
                            )
                        }
                    }

                    R.id.iv_game, R.id.tvName, R.id.ratingBar, R.id.rlGameInfo -> {
                        GameDetailActivity.actionStart(
                            context!!,
                            mAdapter.data[position].game_id,
                            0
                        )
                    }
                    R.id.ll_title, R.id.llReply -> {
                        if (SpUtils.getString(R.string.token, "").isEmpty()) {
                            EventBus.getDefault().post(ClassEvent("LoginActivity"))
                            return@setOnItemChildClickListener
                        }

                        GameReviewsActivity.actionStart(
                            context!!,
                            mAdapter.data[position].game_id,
                            mAdapter.data[position].id,
                            0
                        )

                    }
                    R.id.tvRead -> {
                        if (SpUtils.getString(R.string.token, "").isEmpty()) {
                            EventBus.getDefault().post(ClassEvent("LoginActivity"))
                            return@setOnItemChildClickListener
                        }
                        GameReviewsActivity.actionStart(
                            context!!,
                            mAdapter.data[position].game_id,
                            mAdapter.data[position].id,
                            1
                        )

                    }
                    R.id.tvShare -> {
                        if (FastClickUtil.isFastClickTiming()) {
                            return@setOnItemChildClickListener
                        }
                        ShareDialog.create(childFragmentManager)
                            .apply {
                                class_name = "GameAssess"
                                share_url =
                                    mAdapter.data[position].share_url + "?game_id=${mAdapter.data[position].game_id}&assess_id=${mAdapter.data[position].id}"
                                share_title = "《${mAdapter.data[position].game_name}》的评价"
                                share_desc = mAdapter.data[position].title
                                share_image = mAdapter.data[position].logo
                                contentId = mAdapter.data[position].id
                                id = mAdapter.data[position].game_id
                            }
                            .show()
                    }
                }
            }

        }
        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getUserGameAmwayList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getUserGameAmwayList(page)
        }
        mAdapter.listener = object : ReleaseCommentAdapter.OnHomePushListener {
            override fun onDelete(item: AmwayBean, view: View) {
                UIHelper().showPopupWindow(activity!!, view, "删除", View.OnClickListener {
                    delComment(item.id)
                })
            }

            override fun onLike(item: AmwayBean) {
                mPresenter.likeAssessOrTag(item.game_id, item.id, "0")
            }

            override fun onChecked(is_check: Boolean) {
                if (is_check) {
                    activity?.tvTitle?.rightTxt = "删除"
                    activity?.tvTitle?.setRightTxtColor(Color.parseColor("#FF556E"))
                } else {
                    var is_select = false
                    for (item in mAdapter.data) {
                        if (item.isSelected) {
                            is_select = true
                        }
                    }
                    if (!is_select) {
                        activity?.tvTitle?.rightTxt = "完成"
                        activity?.tvTitle?.setRightTxtColor(Color.parseColor("#0FB50A"))
                    }
                }
            }
        }
    }

    fun delComment(id: String) {
        TextDialog.create(childFragmentManager)
            .setMessage("确定删除该评价吗？")
            .setViewListener(object : TextDialog.ViewListener {
                override fun click() {

                    mPresenter.delUserGameAmwayList(id)
                }

            })
            .show()
    }

    fun showEditStatus(is_show: Boolean) {

        if (is_show == mAdapter.showDeleteIcon) {
            return
        }
        for (i in mAdapter.data) {
            i.isSelected = false
        }

        mAdapter.showDeleteIcon = is_show
        mAdapter.notifyDataSetChanged()

    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        this.menuVisible = menuVisible
    }

    override fun onResume() {
        super.onResume()
        if (menuVisible) {
            page = 1
            start()
        }
    }

    override fun start() {
        page = 1
        mPresenter.getUserGameAmwayList(page)
    }

    override fun showGameAmwayList(bean: UserGameAmwayList.Data) {
        mAdapter.showDeleteIcon = false

        mAdapter.removeAllFooterView()
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(context, "暂无数据", recycler_view)
                )
                //  refreshLayout.setEnableLoadMore(false)
            }
            mAdapter.setNewInstance(bean.data)


        } else {
            mAdapter.addData(bean.data)
        }
        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
        (activity as MineReleaseActivity).showEditButton(mAdapter.data.size)
    }

    override fun delUserGameAmway() {
        showToast("删除成功")
        count = 0
        start()
    }

    override fun showDynamicNodeList(bean: PostInfoBean) {

    }

    override fun showAnswersNodeList(bean: PostInfoBean) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        if (mErrorView != null) {
            fl_content.removeView(mErrorView)
            mErrorView = null
        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null && mAdapter.data.size == 0) {
                mErrorView = getErrorLayout()
                fl_content.addView(mErrorView)
            }
        } else {
            dismissLoading()
        }
    }

    companion object {

        fun getInstance(): ReleaseCommentFragment {
            val fragment = ReleaseCommentFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}