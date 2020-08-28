package com.tinytiger.titi.ui.circle

import android.content.Context
import android.content.Intent
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean

import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.CircleMeListAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesContract

import com.tinytiger.titi.mvp.presenter.circle.CirclesPresenter

import kotlinx.android.synthetic.main.circler_activity_me.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 4:26
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 我加入的圈子列表
 */
class CirclesMeActivity : BaseBackActivity(), CirclesContract.View {

    private val mAdapter by lazy { CircleMeListAdapter() }
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
            val intent = Intent(context, CirclesMeActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.circler_activity_me

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        tvTitle.centerTxt = "我加入的圈子"
        tvTitle.setBackOnClick {
            finish()
        }

        recycler_view.adapter = mAdapter


        refreshLayout.setOnRefreshListener {
            page=1
            start()
        }
        refreshLayout.setOnLoadMoreListener {
            page++
            start()
        }
        mAdapter.addChildClickViewIds(R.id.txt_delete)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.txt_delete -> {
                    TextDialog.create(supportFragmentManager)
                        .setMessage("确定退出该圈子吗？")
                        .setViewListener(object : TextDialog.ViewListener {
                            override fun click() {
                                mPresenter.joinCircle(mAdapter.data[position].id)
                            }
                        }).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        page = 1
        start()
    }

    override fun start() {
        mPresenter.myCircleList(page)
    }


    override fun showCircleList(bean: CircleMeBean) {
        if (bean.data.current_page == 1) {
            if (bean.data.data != null && bean.data.data.size > 0) {
                mAdapter.setNewInstance(bean.data.data)
            } else {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(
                        this,
                        "",
                        recycler_view
                    )
                )
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setNewInstance(ArrayList())
            }
        } else {
            mAdapter.addData(bean.data.data)
        }
        if (bean.data.current_page >= bean.data.last_page) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.resetNoMoreData()
        }
    }

    override fun showCircleRecommendedList(bean: CircleRecommendedBean) {
    }

    override fun showCircle(circle_id: String) {
        mAdapter.deleteData(circle_id)

        val bean = CircleBean()
        bean.is_join = 0
        bean.id=circle_id
        EventBus.getDefault().post(bean)

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