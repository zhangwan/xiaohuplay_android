package com.tinytiger.titi.ui.search

import android.os.Bundle
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.SearchEvent
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.search.SearchAllBean
import com.tinytiger.common.net.data.search.SearchBeanMulti
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.search.SearchListAdapter
import com.tinytiger.titi.widget.dialog.FullPostDialog
import kotlinx.android.synthetic.main.activity_base_recycler.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2019/11/14 0014 上午 10:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索所有分类
 */
class SearchAllFragment : BaseFragment(),FullPostDialog.OnFullSheetListener  {

    companion object {
        fun getInstance(type: Int): SearchAllFragment {
            val fragment = SearchAllFragment()
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
    private val mAdapter by lazy { SearchListAdapter(ArrayList()) }
    /**
     * 初始化 ViewI
     */
    override fun initView() {

        recycler_view.adapter = mAdapter

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableLoadMore(false)
        mAdapter.dp5 = Dp2PxUtils.dip2px(activity, 5)



        initAdapterCallBack()
    }
    private fun initAdapterCallBack(){
        mAdapter.shareListener={
            ShareDialog.create(childFragmentManager)
                .apply {
                    class_name = "Post"
                    share_url ="${it.share_url}?post_id=${it.id}"
                    share_title = "来自「${it.nickname}」"
                    share_desc = it.content
                    if (it.img_url!=null&&it.img_url.size>0){
                        share_image =it.img_url[0]
                    }
                    contentId=it.id
                }
                .show()
        }
        mAdapter.listener = object : SearchListAdapter.OnHomePushListener {

            override fun onRead(item: PostBean) {
                FullPostDialog().apply {
                    postBean = item
                    listener = this@SearchAllFragment
                }.show(childFragmentManager)
            }

            override fun onLike(item: PostBean) {
                (activity as SearchActivity).mPresenter.likePost(item.id)
            }

            override fun onAttention(is_mutual: Int, userid: String) {
                (activity as SearchActivity).mPresenter.follow(is_mutual,userid)
            }
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
    fun getData(key: String, mBean: SearchAllBean) {
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


        val datas = ArrayList<SearchBeanMulti>()
        if (mBean.data.game.size > 0 || mBean.data.game_category.size > 0) {
            datas.add(SearchBeanMulti("游戏"))
            if (mBean.data.game_category != null && mBean.data.game_category.size > 0) {
                for (i in mBean.data.game_category) {
                    datas.add(SearchBeanMulti(i, 2))
                }
            }
            if (mBean.data.game != null && mBean.data.game.size > 0) {
                for (i in mBean.data.game) {
                    datas.add(SearchBeanMulti(i))
                }
            }
        }else{
            SearchAgentUtils.setSearchWordNo(keyWords)
        }


        if (mBean.data.post != null && mBean.data.post.size > 0) {
            datas.add(SearchBeanMulti("帖子"))
            for (i in mBean.data.post) {
                datas.add(SearchBeanMulti(i))
                datas.add(SearchBeanMulti())
            }
            datas.removeAt(datas.size - 1)
        }
        if (mBean.data.circle != null && mBean.data.circle.size > 0) {
            datas.add(SearchBeanMulti("圈子"))
            for (i in mBean.data.circle) {
                datas.add(SearchBeanMulti(i))
            }
        }

        if (mBean.data.user != null && mBean.data.user.size > 0) {
            datas.add(SearchBeanMulti("用户"))
            for (i in mBean.data.user) {
                datas.add(SearchBeanMulti(i))
            }
        }


        if (mBean.data.content != null && mBean.data.content.size > 0) {
            datas.add(SearchBeanMulti("资讯"))
            for (i in mBean.data.content) {
                datas.add(SearchBeanMulti(i))
            }
        }
        mAdapter.searchTxt = keyWords
        mAdapter.setNewInstance(datas)
        if (datas.size == 0) {
            mAdapter.setEmptyView(
                RefreshView.getEmptyView(
                    activity,
                    "呀，咋搜不到呢？",R.mipmap.icon_search_empty,
                    recycler_view
                )
            )
        }
        refreshLayout.finishRefresh()
    }


    fun setUserFollow( uid: String, is_mutual: Int) {

        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].itemType == 4&& mAdapter.data[i].mPostBean.user_id == uid) {
                //帖子
                mAdapter.data[i].mPostBean.is_mutual = is_mutual
                mAdapter.notifyItemChanged(i)
            } else if (mAdapter.data[i].itemType == 5&& mAdapter.data[i].mSearchUserInfo.user_id == uid) {
                //用户
                mAdapter.data[i].mSearchUserInfo.is_mutual = is_mutual
                mAdapter.notifyItemChanged(i)
            }
        }
    }
    override fun onDismiss(postid: String) {
        for (i in 0.. mAdapter.data.size-1){
            if (mAdapter.data[i].itemType == 4&& mAdapter.data[i].mPostBean.id == postid) {
                //帖子
                mAdapter.notifyItemChanged(i)
            }
        }
    }
}
