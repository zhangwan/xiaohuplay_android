package com.tinytiger.titi.ui.mine.me.history

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.HistoryListAdapter
import com.tinytiger.titi.mvp.contract.mine.HistoryContract
import com.tinytiger.titi.mvp.presenter.mine.HistoryPresenter
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import kotlinx.android.synthetic.main.mine_fragment_collection.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 历史记录 Fragment 资讯
*/
class HistoryNewsFragment : BaseFragment(), HistoryContract.View {

    override fun getUserFollow(uid: String, is_mutual: Int) {

    }

    override fun showAmwayHistory(bean: UserGameAmwayList.Data) {

    }


    companion object {

        fun getInstance(): HistoryNewsFragment {
            val fragment = HistoryNewsFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mPresenter by lazy { HistoryPresenter() }

    private val mAdapter by lazy { HistoryListAdapter() }

    override fun getLayoutId(): Int = R.layout.mine_fragment_collection


    override fun initView() {
        mPresenter.attachView(this)
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        mAdapter.setEmptyView(
            RefreshView.getEmptyView(
                context,
                "",
                recycler_view
            )
        )
        mAdapter.setOnItemClickListener { _, view, position ->
            isRefresh = true
            //#内容类型 1=图文 2=视频
            if (mAdapter.data[position].type == 1) {
                NewsDetailActivity.actionStart(
                    context!!, mAdapter.data[position].content_id.toString()
                )
            } else if (mAdapter.data[position].video_url != null) {
                VideoDetailActivity.actionStart(
                    context!!, mAdapter.data[position].content_id.toString(),
                    mAdapter.data[position].video_url
                )
            }

        }


        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getViewHistory(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getViewHistory(page)
        }


        start()
    }


    fun setPageSelected() {
        if (activity is MineHistoryActivity) {
            (activity as MineHistoryActivity).showEditButton(mAdapter.data.size)
            (activity as MineHistoryActivity).updateNumber(mAdapter.data.size,0)
        }
    }

    override fun start() {
        page = 1
        mPresenter.getViewHistory(page)
    }


    private var isRefresh = false
    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            start()
            isRefresh = false
        }

    }

    override fun showViewHistory(bean: MyContentListBean.DataBean) {


        if (bean.current_page == 1) {
//            if (bean.data == null || bean.data.size == 0) {
//                mAdapter.emptyView = RefreshView.getEmptyView(
//                    context,
//                    "暂无数据",
//                    recycler_view
//                )
//            }
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


    override fun showResult() {
    }


    fun clearAll() {
        mAdapter.setNewInstance(ArrayList())
        mAdapter.removeAllFooterView()
//        setPageSelected()
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        //if(mErrorView!=null){
        //  fl_content.removeView(mErrorView)
        //     mErrorView =null
        //  }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() })
                )
            }
        } else {
            dismissLoading()
        }
    }
}