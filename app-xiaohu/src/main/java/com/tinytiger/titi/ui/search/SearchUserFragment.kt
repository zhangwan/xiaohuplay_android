package com.tinytiger.titi.ui.search

import android.os.Bundle
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.SearchEvent
import com.tinytiger.common.net.data.search.SearchUserBean
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.search.SearchUserAdapter
import kotlinx.android.synthetic.main.activity_base_recycler.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2019/11/14 0014 上午 10:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索用户
 */
class SearchUserFragment : BaseFragment() {

    companion object {
        fun getInstance(type: Int): SearchUserFragment {
            val fragment = SearchUserFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.searchType = type
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.base_recycler

    /**
     * 搜索类型
     */
    private var searchType = 0
    private val mAdapter by lazy { SearchUserAdapter() }
    /**
     * 初始化 ViewI
     */
    override fun initView() {
        recycler_view.adapter = mAdapter
        refreshLayout.setEnableRefresh(false)

        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            EventBus.getDefault().post(SearchEvent(0, page))
        }
    }

    override fun start() {

    }

    fun setData(key: String) {
        if (key != keyWords) {
            page = 1
            EventBus.getDefault().post(SearchEvent(0, page))
        }
    }

    private var keyWords = ""

    fun getData(key: String, mBean: SearchUserBean) {
        keyWords = key
        if (mBean.data==null){
            mAdapter.setNewInstance(mutableListOf())
            mAdapter.setEmptyView(
                RefreshView.getEmptyView(
                    activity,
                    "呀，咋搜不到呢？",R.mipmap.icon_search_empty,
                    recycler_view
                )
            )
            refreshLayout.finishRefresh()
            return
        }
        if (mBean.data.current_page == 1) {
            mAdapter.searchTxt = key
            mAdapter.setNewInstance(mBean.data.data)
            if (mBean.data.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(
                        activity,
                        "呀，咋搜不到呢？",R.mipmap.icon_search_empty,
                        recycler_view
                    )
                )
                SearchAgentUtils.setSearchWordNo(keyWords)
            } else {
                refreshLayout.resetNoMoreData()
            }
        } else {
            mAdapter.addData(mBean.data.data)

        }
        if (mBean.data.current_page >= mBean.data.last_page) {
            if (mAdapter.data.size > 0) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.setEnableLoadMore(false)
            }
        } else {
            refreshLayout.finishLoadMore()
        }
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    fun setUserFollow(uid: String, is_mutual: Int) {
        if(mAdapter.data!=null){
            for (i in 0 until mAdapter.data.size) {
                if (mAdapter.data[i].user_id == uid) {
                    mAdapter.data[i].is_mutual = is_mutual
                    mAdapter.notifyItemChanged(i)
                }
            }
        }

    }
}
