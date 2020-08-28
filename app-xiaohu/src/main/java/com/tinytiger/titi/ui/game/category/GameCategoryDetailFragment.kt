package com.tinytiger.titi.ui.game.category


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.gametools.category.GameCategoryBannerBean

import com.tinytiger.common.net.data.gametools.category.GameCategoryDetailListBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryListBean
import com.tinytiger.common.net.exception.ErrorStatus

import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.sort.GameCategoryDetailAdapter
import com.tinytiger.titi.mvp.contract.gametools.GameCategoryContract
import com.tinytiger.titi.mvp.presenter.gametools.GameCategoryPresenter
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import com.xwdz.download.notify.DataUpdatedWatcher
import kotlinx.android.synthetic.main.base_recycler.*


/*
* @author Tamas
* create at 2020/1/7 0007
* Email: ljw_163mail@163.com
* description:  游戏分类详情 Fragment
*/
class GameCategoryDetailFragment : BaseFragment(), GameCategoryContract.View {


    override fun getLayoutId(): Int = R.layout.base_recycler


    private val mAdapter by lazy { GameCategoryDetailAdapter() }

    private val mPresenter by lazy { GameCategoryPresenter() }


    //1=热门 2=最新上架 3=评分最高 默认1
    private var type = 1
    private var cate_id = 0

    companion object {

        fun getInstance(cate_id: Int, type: Int): GameCategoryDetailFragment {
            val fragment = GameCategoryDetailFragment()
            val bundle = Bundle()
            fragment.type = type
            fragment.cate_id = cate_id
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun initView() {
        mPresenter.attachView(this)


        initRecyclerView()
        start()
    }

    private fun initRecyclerView() {


        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            GameDetailActivity.actionStart(context!!, mAdapter.data[position].id,4)
        }

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getGameCategoryDetailList(cate_id, type, page)
        }
    }


    override fun start() {
        page = 1
        mPresenter.getGameCategoryDetailList(cate_id, type, page)
    }


    override fun onResume() {
        super.onResume()
        QuietDownloader.addObserver(watcher)
        mAdapter.installApk()
    }




    override fun showGameCategory(data: GameCategoryListBean) {

    }

    override fun showGameCategoryDetailList(bean: GameCategoryDetailListBean.DataBean) {
//        refreshLayout.finishLoadMore()
//
//        if (bean != null) {
//            if (bean.total == 0) {
//                mAdapter.setNewInstance(bean.data)
//            } else {
//
//                if (bean.current_page == 1) {
//                    mAdapter.setNewInstance(bean.data)
//                } else {
//                    mAdapter.addData(bean.data)
//                }
//            }
//            page = bean.current_page
//            refreshLayout.setEnableLoadMore(bean.current_page < bean.last_page)
//        }


        mAdapter.removeAllFooterView()

        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(
                        context,
                        "暂无数据",
                        recycler_view
                    )
                )
                //  refreshLayout.setEnableLoadMore(false)
            }
            mAdapter.setNewInstance(bean.data)
        } else {
            mAdapter.addData(bean.data)
        }
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }


    }

    override fun showGameCategoryBanner(data: ArrayList<GameCategoryBannerBean.DataBean>) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
//        if (mErrorView != null) {
//            fl_content.removeView(mErrorView)
//            mErrorView = null
//        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishLoadMore()
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


    override fun onDestroy() {
        super.onDestroy()
        QuietDownloader.removeObserver(watcher)
        mPresenter.detachView()
    }


    private val watcher = object : DataUpdatedWatcher() {
        override fun notifyUpdate(data: DownloadEntry) {
            mAdapter.notifyUpdate(data)

        }
    }


}