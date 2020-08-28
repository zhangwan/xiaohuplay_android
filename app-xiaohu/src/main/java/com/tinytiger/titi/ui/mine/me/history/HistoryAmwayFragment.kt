package com.tinytiger.titi.ui.mine.me.history

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.HomePush2Adapter
import com.tinytiger.titi.mvp.contract.mine.HistoryContract
import com.tinytiger.titi.mvp.presenter.mine.HistoryPresenter
import kotlinx.android.synthetic.main.mine_fragment_collection.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 历史记录 Fragment 安利文
*/
class HistoryAmwayFragment : BaseFragment(), HistoryContract.View {
    override fun getUserFollow(uid: String, is_mutual: Int) {
        for (i in mAdapter.data) {
            if (uid == i.user_id) {
                i.follow = is_mutual
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    private var total = 0

    fun setPageSelected() {
        if (activity is MineHistoryActivity) {
            (activity as MineHistoryActivity).updateNumber(total,0)
            (activity as MineHistoryActivity).showEditButton(total)
        }
    }

    companion object {

        fun getInstance(): HistoryAmwayFragment {
            val fragment = HistoryAmwayFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mPresenter by lazy { HistoryPresenter() }

    private val mAdapter by lazy { HomePush2Adapter() }

    override fun getLayoutId(): Int = R.layout.mine_fragment_collection


    override fun initView() {
        mPresenter.attachView(this)
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        mAdapter.setEmptyView(  RefreshView.getEmptyView(
            context,
            "",
            recycler_view
        ))
        mAdapter.listener = object : HomePush2Adapter.OnHomePushListener {
            override fun onRefresh() {
                isRefresh = true
            }

            override fun onShare(mBean: AmwayBean) {
                ShareDialog.create(childFragmentManager)
                    .apply {
                        class_name = "GameAssess"
                        share_url = mBean.share_url + "?game_id=${mBean.game_id}&assess_id=${mBean.id}"
                        share_title = "《${mBean.game_name}》的评价"
                        share_desc = mBean.title
                        share_image = mBean.thumbnail
                        contentId = mBean.id
                        id=mBean.game_id
                    }
                    .show()
            }

            override fun onLike(mBean: AmwayBean) {
                mPresenter.likeAssessOrTag(mBean.game_id, mBean.id, "0")
            }

            override fun onAttention(mBean: AmwayBean) {
                mPresenter.follow(mBean.user_id, mBean.follow)
            }
        }


        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getUserGameAmwayViewHistory(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getUserGameAmwayViewHistory(page)
        }


        start()
    }


    override fun start() {
        page = 1
        mPresenter.getUserGameAmwayViewHistory(page)
    }

    private var isRefresh = false

    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            isRefresh = false
            start()
        }
    }


    override fun showAmwayHistory(bean: UserGameAmwayList.Data) {
        total = bean.total
        if (bean.current_page == 1) {
            mAdapter.setNewInstance(bean.data)

        } else {
            mAdapter.addData(bean.data)
        }
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        mAdapter.removeAllFooterView()

        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }


        setPageSelected()
    }


    override fun showViewHistory(bean: MyContentListBean.DataBean) {


    }

    override fun showResult() {

    }


    fun clearAll() {
        total = 0
        mAdapter.setNewInstance(ArrayList())
        mAdapter.removeAllFooterView()
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() }))
            }
        } else {
            dismissLoading()
        }
    }
}