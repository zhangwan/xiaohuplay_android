package com.tinytiger.titi.ui.search

import android.os.Bundle
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.SearchEvent
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.widget.dialog.FullPostDialog
import kotlinx.android.synthetic.main.activity_base_recycler.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2019/11/14 0014 上午 10:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 帖子
 */
class SearchPostFragment : BaseFragment(), FullPostDialog.OnFullSheetListener {

    companion object {
        fun getInstance(type: Int): SearchPostFragment {
            val fragment = SearchPostFragment()
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
    private val mAdapter by lazy { CommonPostNodeAdapter(activity!!, ArrayList()) }
    private var dataList = arrayListOf<PostBean>()
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
        mAdapter.type=2
        mAdapter.shareListener = {
            ShareDialog.create(childFragmentManager)
                .apply {
                    class_name = "Post"
                    share_url = "${it.share_url}?post_id=${it.id}"
                    share_title = "来自「${it.nickname}」"
                    share_desc = it.content
                    if (it.img_url != null && it.img_url.size > 0) {
                        share_image = it.img_url[0]
                    }
                    contentId = it.id
                    id=it.circle_id
                }
                .show()
        }
        mAdapter.attentionListener = { item, is_mutual ->
            EventBus.getDefault().post(SearchEvent(item.user_id, 0, is_mutual))
        }
         mAdapter.readListener={
             FullPostDialog().apply {
                 postBean = it
                 listener = this@SearchPostFragment
             }.show(childFragmentManager)
         }
        mAdapter.likeListener={
            (activity as SearchActivity).mPresenter.likePost(it.id)
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

    fun getData(key: String, mBean: PostInfoBean) {

        keyWords = key
        mAdapter.searchTxt=keyWords
        if (mBean.data==null){
            mAdapter.data= ArrayList<PostBean>()
            mAdapter.notifyDataSetChanged()
            mAdapter.mEmptyView=(
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
            dataList=mBean.data.data
            if (mBean.data.data.size == 0) {
                mAdapter.mEmptyView=(
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
            dataList.addAll(mBean.data.data)
        }
        mAdapter.data=dataList
        mAdapter.notifyDataSetChanged()
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
        if(mAdapter.data.size!=null){
            for (i in 0 until mAdapter.data.size) {
                if (mAdapter.data[i].user_id == uid) {
                    mAdapter.data[i].is_mutual = is_mutual
                    mAdapter.notifyItemChanged(i)
                }
            }
        }

    }

    override fun onDismiss(postid: String) {
        if(mAdapter.data.size!=null) {
            for (i in 0 until mAdapter.data.size) {
                if (mAdapter.data[i].id == postid) {
                    mAdapter.notifyItemChanged(i)
                }
            }
        }
    }
}
