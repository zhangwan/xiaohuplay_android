package com.tinytiger.titi.ui.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.SearchEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.search.SearchBeanMulti
import com.tinytiger.common.net.data.search.SearchGameBean
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.search.MultiSearchGameAdapter
import kotlinx.android.synthetic.main.activity_base_recycler.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2019/11/14 0014 上午 10:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索游戏分类
 */
class SearchGameFragment : BaseFragment() {


    companion object {
        fun getInstance(type: Int): SearchGameFragment {
            val fragment = SearchGameFragment()
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
    private val mAdapter by lazy { MultiSearchGameAdapter(ArrayList()) }
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

    fun getData(key: String, mBean: SearchGameBean) {

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
        if (mBean.data.data != null && mBean.data.data.size > 0) {
            for (i in mBean.data.data) {
                datas.add(SearchBeanMulti(i))
            }
        }else{
            SearchAgentUtils.setSearchWordNo(keyWords)
        }

        if (mBean.data.current_page == 1) {
            mAdapter.searchTxt = key
            if (mBean.data.game_category != null && mBean.data.game_category.size > 0) {
                mAdapter.category.clear()
                for (i in mBean.data.game_category) {
                    mAdapter.category.add(SearchBeanMulti(i, 2))
                }
                if (mAdapter.category.size>5){

                    datas.addAll(0,mAdapter.category.subList(0,5))
                    datas.add(5,SearchBeanMulti(""))
                }else{
                    datas.addAll(0,mAdapter.category)
                }
            }

            mAdapter.setNewInstance(datas)
        }else{
            mAdapter.addData(datas)
        }

        if (mBean.data.current_page == 1 && mBean.data.data.size == 0) {
            mAdapter.setEmptyView(getEmptyView(keyWords))
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

    fun getEmptyView(keyWords: String): View {
        val view = activity!!.layoutInflater.inflate(
            com.tinytiger.common.R.layout.view_empty_game,
            recycler_view.parent as ViewGroup,
            false
        )
        view.findViewById<TextView>(com.tinytiger.common.R.id.empty_view_tv).text = "呀，咋搜不到呢？"
       view.findViewById<ImageView>(com.tinytiger.common.R.id.ivEmpty).setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.icon_search_empty))
        val tvAddGame = view.findViewById<TextView>(com.tinytiger.common.R.id.tvAddGame)
        tvAddGame.setOnClickListener {
            if (!MyNetworkUtil.isNetworkAvailable(activity!!)) {
                ToastUtils.show(activity, "当前网络不可用")
                return@setOnClickListener
            }
            tvAddGame.isClickable = false
            (activity as SearchActivity).setGameCollection(keyWords)

            tvAddGame.text = "已成功申请收录"
            tvAddGame.setTextColor(ContextCompat.getColor(activity!!, R.color.gray99))
        }
        return view
    }

}
