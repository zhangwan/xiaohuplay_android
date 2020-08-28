package com.tinytiger.titi.ui.props


import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager


import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.LoadingUtils

import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsBean

import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean

import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil


import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.props.PropsMeAdapter

import com.tinytiger.titi.mvp.contract.props.PropsContract
import com.tinytiger.titi.mvp.presenter.props.PropsPresenter

import kotlinx.android.synthetic.main.props_fragment_recycler.*


/**
 *
 * @Author luke
 * @Date 2020-02-03 16:33
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 我的道具列表
 *
 */
class PropsMeFragment : BaseFragment(), PropsContract.View {


    private var cate_id = ""

    companion object {
        fun getInstance(cate_id: String): PropsMeFragment {
            val fragment = PropsMeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.cate_id = cate_id
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.props_fragment_recycler

    private val mPresenter by lazy { PropsPresenter() }

    private val mAdapter by lazy { PropsMeAdapter() }


    override fun initView() {
        mPresenter.attachView(this)
        val manager = LinearLayoutManager(activity)
        recycler_view.layoutManager = manager

        initRecyclerView()
    }


    private var mposition = -1
    private fun initRecyclerView() {
        recycler_view.adapter = mAdapter


        refreshLayout.setOnRefreshListener { _ ->
            page = 1
            start()
        }

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            start()
        }
        mAdapter.listener = object : PropsMeAdapter.OnPropsListener {

            override fun onProps(data: PropsBean, position: Int) {
                if (FastClickUtil.isFastClickTiming()) {
                    return
                }

                if (!isLoginStart()) {
                    return
                }

                mposition = position
                showProgress()
                mPresenter.wearProps(data.tool_id,cate_id)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    override fun start() {
        mPresenter.getMyMallprops(cate_id, page)
    }


    override fun indexGoodsCate(bean: PropsTabListBean) {
    }

    override fun indexGoods(mBean: PropsListBean) {
        if (mBean.data.current_page == 1) {
            if (mBean.data.data != null && mBean.data.data.size > 0) {
                mAdapter.setNewInstance(mBean.data.data)
            } else {
                mAdapter.setEmptyView( RefreshView.getEmptyView(
                    activity,
                    "",
                    recycler_view
                ))
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setNewInstance(ArrayList())
            }
        } else {
            mAdapter.addData(mBean.data.data)
        }

        if (mBean.data.current_page >= mBean.data.last_page) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.resetNoMoreData()
        }
    }

    override fun wearProps(bean: BaseBean) {
        showProgress()
        page=1
        start()

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


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() }))
            }
        }
    }


}
