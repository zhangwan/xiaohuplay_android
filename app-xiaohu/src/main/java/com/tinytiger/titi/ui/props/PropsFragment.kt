package com.tinytiger.titi.ui.props


import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.props.PropsAdapter
import com.tinytiger.titi.data.MyUserData

import com.tinytiger.titi.mvp.contract.props.PropsContract
import com.tinytiger.titi.mvp.presenter.props.PropsPresenter
import kotlinx.android.synthetic.main.props_fragment_recycler.*
import org.greenrobot.eventbus.EventBus

import kotlin.collections.ArrayList


/**
 *
 * @Author luke
 * @Date 2020-02-03 16:33
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具详情
 *
 */
class PropsFragment : BaseFragment(), PropsContract.View {


    private var cate_id = ""

    companion object {
        fun getInstance(cate_id: String): PropsFragment {
            val fragment = PropsFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.cate_id = cate_id
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.props_fragment_recycler
    private val mPresenter by lazy { PropsPresenter() }
    private val mAdapter by lazy { PropsAdapter() }

    override fun initView() {
        mPresenter.attachView(this)

        recycler_view.layoutManager = GridLayoutManager(context, 3)

        refreshLayout.setOnRefreshListener { _ ->
            page = 1
            start()
        }

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            start()
        }

        recycler_view.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnItemClickListener
            }

            PropsDetailActivity.actionStart(context!!,mAdapter.data.get(position).id)

        }

        showProgress()
        start()
    }



    override fun start() {
        mPresenter.loadindexGoods(cate_id,page)
    }


    override fun indexGoodsCate(bean: PropsTabListBean) {
    }

    override fun indexGoods(mBean: PropsListBean) {

        if (mBean.data.current_page == 1) {
            if (mBean.data.data!=null&& mBean.data.data.size > 0) {
                mAdapter.setNewInstance(mBean.data.data)
            }else{
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

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
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
