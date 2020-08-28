package com.tinytiger.titi.ui.mine.me.collect

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.UserCollectWikiList
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.OnItemCheckListener
import com.tinytiger.titi.adapter.mine.CollectionListAdapter
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.presenter.center.MineCollectsPresenter
import kotlinx.android.synthetic.main.mine_fragment_collection.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 我的收藏 Fragment 咨询
*/
class CollectNewsFragment : BaseFragment(), MineCollectContract.View {

    override fun batchCancelCollect() {
        showToast(getString(R.string.collect_succeed_hint))
        page = 1
        mAdapter.isShowEditStatus = false
        start()

        rlSelect.visibility = View.GONE

    }

    override fun getUserFollow(uid: String, is_mutual: Int) {

    }

    override fun showAmwayCollectList(bean: UserGameAmwayList.Data) {

    }

    companion object {

        fun getInstance(): CollectNewsFragment {
            val fragment = CollectNewsFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mPresenter by lazy { MineCollectsPresenter() }
    private val mAdapter by lazy { CollectionListAdapter(mOnItemCheckListener) }

    override fun getLayoutId(): Int = R.layout.mine_fragment_collection


    override fun initView() {
        mPresenter.attachView(this)
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.collectList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.collectList(page)
        }



        tvCancel.setOnClickListener {
            var ids = ""
            for (i in mAdapter.data) {
                if (i.isSelected) {
                    ids = "$ids,${i.id}"
                }
            }

            if (ids.length < 1) {
                //showToast("")
            } else {
                mPresenter.batchCancelCollectNews(ids)
            }
        }
        cb_check.setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                if (is_auto) {
                    for (i in mAdapter.data) {
                        i.isSelected = b
                    }
                    tvCancel.isEnabled = true
                    mAdapter.notifyDataSetChanged()
                }
            })


    }

    private var is_auto = true

    override fun onResume() {
        super.onResume()
        page = 1
        start()
    }

    private val mOnItemCheckListener: OnItemCheckListener = OnItemCheckListener { is_check ->
        var num = 0
        if (mAdapter.data.size > 0) {
            for (item in mAdapter.data) {
                if (item.isSelected) {
                    num++
                }
            }
            tvCancel.isEnabled = num >= 1
        }
        if (is_check) {
            if (num == mAdapter.data.size) {
                is_auto = false
                cb_check.isChecked = true
                is_auto = true
            }
        } else {
            is_auto = false
            cb_check.isChecked = false
            is_auto = true
        }
    }

    fun showEditStatus(is_show: Boolean) {

        if (is_show == mAdapter.isShowEditStatus) {
            return
        }
        if (is_show) {
            rlSelect.visibility = View.VISIBLE
        } else {
            rlSelect.visibility = View.GONE
        }
        cb_check.isChecked = false
        for (i in mAdapter.data) {
            i.isSelected = false
        }
        mAdapter.isShowEditStatus = is_show
        mAdapter.notifyDataSetChanged()
    }

    override fun start() {
        mPresenter.collectList(page)
    }


    override fun showNewsCollectList(bean: MyContentListBean.DataBean) {
        mAdapter.removeAllFooterView()
        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
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

        //无数据隐藏编辑按钮
        if (activity is MineCollectsActivity) {
            (activity as MineCollectsActivity).showEditButton(mAdapter.data.size,
                mAdapter.isShowEditStatus)
            (activity as MineCollectsActivity).showNumber(bean.total, 4)
        }
    }

    override fun showUserWikiCollectList(bean: UserCollectWikiList.Data) {

    }

    override fun showPostCollectList(bean: PostInfoBean) {
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