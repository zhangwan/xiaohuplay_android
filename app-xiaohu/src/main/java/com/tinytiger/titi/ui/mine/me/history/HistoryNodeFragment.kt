package com.tinytiger.titi.ui.mine.me.history

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.basis.BasisFragment
import com.tinytiger.common.http.response.PageVo
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.ui.mine.me.collect.CollectGameFragment
import com.tinytiger.titi.ui.mine.me.collect.vp.CollectGamePresenter
import com.tinytiger.titi.ui.mine.me.history.vp.HistoryNodePresenter
import kotlinx.android.synthetic.main.fragment_history_node.*
import kotlinx.android.synthetic.main.fragment_history_node.recycler_view
import kotlinx.android.synthetic.main.fragment_history_node.refreshLayout
import kotlinx.android.synthetic.main.mine_fragment_collection.*


class HistoryNodeFragment : BasisFragment<HistoryNodePresenter>() {
    private val mAdapter by lazy { CommonPostNodeAdapter(activity!!, ArrayList()) }
    private var total = 0
    private var data = arrayListOf<PostBean>()

    companion object {

        fun getInstance(): HistoryNodeFragment {
            val fragment = HistoryNodeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_history_node

    override fun initView() {
        mAdapter.pageIndex = ConstantsUtils.NOTE.PAGE_DEFAULT
        mAdapter.mEmptyView = (RefreshView.getEmptyView(activity, "", recycler_view))
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        basePresenter?.getPostList(page)
        initListener()
    }

    fun initListener() {
        refreshLayout.setOnRefreshListener {
            page = 1
            basePresenter?.getPostList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            basePresenter?.getPostList(page)
        }
    }

    fun showAnswersNodeList(list: PostInfoBean) {
        var postBean = list.data
        total = postBean.total
        if (postBean.current_page == 1) {
            data = postBean.data
        } else {
            data?.addAll(postBean.data)

        }
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

        if (postBean.current_page >= postBean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter?.mBottomView = RefreshView.getNewFooterView(context, "", recycler_view)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
        mAdapter.data = data
        mAdapter.notifyDataSetChanged()
        setPageSelected()
    }

    fun clearAll() {
        mAdapter.data = arrayListOf()
        mAdapter.notifyDataSetChanged()
    }
    fun setPageSelected() {
        if (activity is MineHistoryActivity) {
            (activity as MineHistoryActivity).showEditButton(mAdapter.data.size)
            (activity as MineHistoryActivity).updateNumber(mAdapter.data.size,2)
        }
    }
}