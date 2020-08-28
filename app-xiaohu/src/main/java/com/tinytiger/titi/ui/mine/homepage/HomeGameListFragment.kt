package com.tinytiger.titi.ui.mine.homepage

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.net.data.user.UserDynamicList
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.home.HomeGameListAdapter
import com.tinytiger.titi.mvp.contract.center.HomeDynamicContract
import com.tinytiger.titi.mvp.presenter.center.HomeDynamicPresenter
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import kotlinx.android.synthetic.main.base_recycler.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 用户主页 Fragment 动态
*/
class HomeGameListFragment : BaseFragment(), HomeDynamicContract.View {


    private val mAdapter by lazy { HomeGameListAdapter() }

    private var user_id:String ="0"

    companion object {

        fun getInstance(user_id:String): HomeGameListFragment {
            val fragment = HomeGameListFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            fragment.user_id = user_id
            return fragment
        }
    }

    private val mPresenter by lazy { HomeDynamicPresenter() }

    override fun getLayoutId(): Int = R.layout.base_recycler

    override fun initView() {
        mPresenter.attachView(this)
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getGame(user_id,page)
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            GameDetailActivity.actionStart(context!!,mAdapter.data[position].id,0)
        }

        start()
    }



    override fun start() {
        page = 1
        mPresenter.getGame(user_id,page)
    }

    override fun showHomeDynamicList(bean: UserDynamicList) {

    }

    override fun showHomeGameList(bean: MineGameListBean.Data) {
        mAdapter.removeAllFooterView()

        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView( RefreshView.getEmptyView(
                    context,
                    "",
                    recycler_view
                ))
            } else {
                mAdapter.setNewInstance(bean.data)
            }
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

        }else{
            dismissLoading()
        }
    }
}