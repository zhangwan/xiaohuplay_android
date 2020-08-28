package com.tinytiger.titi.ui.mine.setting


import android.content.Context
import android.content.Intent
import com.tinytiger.common.net.data.mine.UserBlackBean
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.BlackUserAdapter
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.widget.dialog.TextDialog

import com.tinytiger.titi.mvp.contract.mine.SettingBlackContract
import com.tinytiger.titi.mvp.presenter.mine.SettingBlackPresenter
import com.netease.nim.uikit.common.UserUtil
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseBackActivity

import kotlinx.android.synthetic.main.activity_base_recycler.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 黑名单列表
 */
class BlackListActivity : BaseBackActivity(), SettingBlackContract.View {

    override fun showCancelBlack() {

        for (a in 0 ..(mAdapter.data.size - 1)) {
            if (mAdapter.data[a].user_id == userid) {
                UserUtil.removeFromBlack(mAdapter.data[a].netease_id)
                mAdapter.remove(a)
                break
            }
        }

        if (mAdapter.data.size == 0) {
            mAdapter.setEmptyView(RefreshView.getEmptyView(
                this,
                "无黑名单记录",
                recycler_view
            ))
        }
    }

    override fun getUserBlackList(mBean: UserBlackBean) {
        if (mBean.data.current_page == 1) {
            if (mBean.data != null && mBean.data.data.size > 0) {
                mAdapter.setNewInstance(mBean.data.data)
            } else {
                mAdapter.setNewInstance(mBean.data.data)
                mAdapter.setEmptyView( RefreshView.getEmptyView(
                    this,
                    "无黑名单记录",
                    recycler_view
                ))
                refreshLayout.setEnableLoadMore(false)
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


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size==0){
                mAdapter.setEmptyView(RefreshView.getNetworkView(this, recycler_view, {
                    showLoading()
                    start() }))
            }
        }
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    override fun showLoading() {
        showProgress()
    }

    fun actionStart(context: Context) {
        val intent = Intent(context, BlackListActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.activity_base_recycler
    private val mAdapter by lazy { BlackUserAdapter() }
    private val mPresenter by lazy { SettingBlackPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

    }

    override fun initView() {
        tvTitle.centerTxt = "黑名单"
        tvTitle.setBackOnClick { finish() }

        recycler_view.adapter = mAdapter
        refreshLayout
        refreshLayout.setOnRefreshListener { _ ->
            page = 1
            start()
        }

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            start()
        }

        mAdapter.listener = object : BlackUserAdapter.OnBlackUserListener {
            override fun onType(uid: String, pos: Int) {

                TextDialog.create(supportFragmentManager)
                    .setMessage("确定解除拉黑？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            userid = uid
                            mPresenter.loadCancelBlack(uid)
                        }
                    }).show()
            }
        }
        showProgress()
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    private var userid = ""

    override fun start() {
        mPresenter.loadUserBlackList(page)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}
