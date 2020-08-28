package com.tinytiger.titi.ui.circle

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tinytiger.common.base.BaseApp


import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil

import com.tinytiger.common.net.data.circle.CircleBeanMutli
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean

import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.MultiCirclesAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesContract

import com.tinytiger.titi.mvp.presenter.circle.CirclesPresenter
import kotlinx.android.synthetic.main.activity_base_recycler.*



/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 4:26
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 搜鱼圈子列表
 */
class CirclesListActivity : BaseBackActivity(), CirclesContract.View {

    private val mAdapter by lazy { MultiCirclesAdapter(ArrayList()) }
    private val mPresenter by lazy { CirclesPresenter() }

    companion object {
        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, CirclesListActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.activity_base_recycler

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        tvTitle.centerTxt = "圈子列表"
        tvTitle.setBackOnClick {
            finish()
        }
        tvTitle.setIvLine(View.GONE)

        ll_content.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
       val dp15= Dp2PxUtils.dip2px(this,15)
        fl_content.setPadding(dp15,0,dp15,0)


        val gridManager = GridLayoutManager(this, 2)
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return mAdapter.data[position].spanSize
            }
        }
        recycler_view.layoutManager = gridManager
        recycler_view.adapter = mAdapter

      //  refreshLayout.setEnableLoadMore(false)
        refreshLayout.setEnableRefresh(false)

        refreshLayout.setOnLoadMoreListener {
            page++
            start()
        }

        mPresenter.recommendedCircleList()
        start()
    }

    override fun start() {
        mPresenter.allCircleList(page)
    }


    override fun showCircleList(bean: CircleMeBean) {
        val list = ArrayList<CircleBeanMutli>()
        if (bean.data.data != null && bean.data.data.size > 0) {
            if (bean.data.current_page == 1) {
                list.add(CircleBeanMutli("所有圈子"))
            }
            for (i in bean.data.data) {
                list.add(CircleBeanMutli(i))
            }

        }

        mAdapter.addData(list)
        if (bean.data.current_page >= bean.data.last_page) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.resetNoMoreData()
        }
    }

    override fun showCircleRecommendedList(bean: CircleRecommendedBean) {
        if (bean.data != null && bean.data.size > 0) {
            val list = ArrayList<CircleBeanMutli>()
            list.add(CircleBeanMutli("推荐圈子"))
            for (i in bean.data) {
                list.add(CircleBeanMutli(i))
            }
            mAdapter.addData(0, list)
        }
    }

    override fun showCircle(circle_id: String) {
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getNetworkView(this, recycler_view) { start() })
            }
        }
    }
}