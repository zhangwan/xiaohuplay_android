package com.tinytiger.titi.ui.search

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.wiki.MainWikiListBean

import com.tinytiger.common.net.data.wiki.WikiSearchListBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.ViewUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.sort.GameSearchHistoryAdapter
import com.tinytiger.titi.adapter.gametools.sort.GameWikiSearchAdapter
import com.tinytiger.titi.mvp.contract.wiki.MainWikiContract
import com.tinytiger.titi.mvp.presenter.wiki.MainWikiPresenter
import kotlinx.android.synthetic.main.game_activity_search.*
import kotlinx.android.synthetic.main.game_activity_search.tv_cancel


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 游戏百科搜索 Activity
*/
class GameWikiSearchActivity : BaseActivity(), MainWikiContract.View {

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, GameWikiSearchActivity::class.java)
            context.startActivity(intent)
        }

        private const val EXTRA_GAME_ID = "extra_game_id"

        fun actionStart(context: Context,game_id:String){
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, GameWikiSearchActivity::class.java)
            intent.putExtra(EXTRA_GAME_ID,game_id)
            context.startActivity(intent)
        }

    }

    private var game_id = ""

    private val mPresenter by lazy { MainWikiPresenter() }

    private val mHistoryAdapter by lazy { GameSearchHistoryAdapter(ArrayList()) }

    private val mAdapter by lazy { GameWikiSearchAdapter() }

    //private val mHistoryList: ArrayList<String> = ArrayList()

    override fun layoutId(): Int = R.layout.game_activity_search


    override fun initData() {

        mPresenter.attachView(this)

        if(intent.hasExtra(EXTRA_GAME_ID)){
            game_id= intent.getStringExtra(EXTRA_GAME_ID)
        }


    }

    override fun initView() {

        tv_cancel.setOnClickListener {
            finish()
        }
        if(game_id.isEmpty()){
            rv_history.visibility = View.VISIBLE
            et_search.hint ="搜索词条或游戏"
        }else{
            llHistory.visibility =  View.GONE
            rv_history.visibility =  View.INVISIBLE
            et_search.hint ="搜索游戏内的词条"
        }

        initRecyclerView()

        iv_delete.setOnClickListener {
            ll_delete.requestFocus()
            ll_delete.visibility = View.VISIBLE
            mHistoryAdapter.showDelete = true
            mHistoryAdapter.notifyDataSetChanged()
        }

        tv_delete_all.setOnClickListener {
            mHistoryAdapter.setNewInstance(ArrayList())
            llHistory.visibility = View.GONE
        }
        tv_complete.setOnClickListener {
            ll_delete.visibility = View.GONE
            mHistoryAdapter.showDelete = false
            mHistoryAdapter.notifyDataSetChanged()
        }


        et_search.setOnClickListener {
            if(mHistoryAdapter.showDelete){
                mHistoryAdapter.showDelete = false
                mHistoryAdapter.notifyDataSetChanged()
                ll_delete.visibility = View.GONE
            }
        }

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyWord = et_search.text.toString().trim()

                inputSearch(keyWord)
            }
            false
        }

        et_search.filters= ViewUtils.inputFilters

        et_search.addTextChangedListener {
            if (it.toString().trim().isNotEmpty()) {
                iv_close.visibility = View.VISIBLE
            } else {
                iv_close.visibility = View.GONE
            }
        }

        iv_close.setOnClickListener {
            refreshLayout.visibility = View.GONE
            llHistory.visibility = View.VISIBLE
            iv_close.visibility = View.GONE
            ll_delete.visibility = View.GONE
            et_search.setText("")
        }

    }



    private fun inputSearch(keyWord:String){
        if(keyWord.isEmpty()){
            showToast("搜索内容不能为空")
            return
        }
        page = 1
        mPresenter.searchWiki(keyWord,game_id,page)
        mAdapter.searchTxt=keyWord
        mHistoryAdapter.showDelete =false
        mHistoryAdapter.remove(keyWord)
        mHistoryAdapter.addData(0,keyWord)

    }

    private fun initRecyclerView() {

        val layoutManager = object : FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        rv_history.layoutManager = layoutManager
        rv_history.adapter = mHistoryAdapter


        val search_history = SpUtils.getString(R.string.search_history_wiki, "")
        if (!search_history.isNullOrEmpty()) {
            JSON.parseArray(search_history).mapTo(mHistoryAdapter.data) { it.toString() }
        }

        mHistoryAdapter.notifyDataSetChanged()

        if (mHistoryAdapter.data.size==0){
            llHistory.visibility=View.GONE
        }

        mHistoryAdapter.setOnItemClickListener { _, _, position ->
            if (mHistoryAdapter.showDelete) {
                mHistoryAdapter.remove(position)
                if (mHistoryAdapter.data.size==0){
                    llHistory.visibility=View.GONE
                }
            } else {
                //点击历史搜索
                et_search.setText(mHistoryAdapter.data[position])
                inputSearch(mHistoryAdapter.data[position])
            }
        }

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page ++
            mPresenter.searchWiki(et_search.text.toString().trim(),game_id,page)
        }


        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter

        mAdapter.setEmptyView(RefreshView.getEmptyView(this, "", recycler_view))
    }


    override fun start() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        if(game_id.isEmpty()) {
            SpUtils.saveSP(R.string.search_history_wiki, JSON.toJSON(mHistoryAdapter.data).toString())
        }
    }


    override fun showMainWiki(data: MainWikiListBean.DataBean) {

    }

    override fun showSearchWiki(bean: WikiSearchListBean.DataBean) {

        refreshLayout.visibility = View.VISIBLE
        llHistory.visibility = View.GONE
        mAdapter.removeAllFooterView()
        refreshLayout.finishLoadMore()
        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getEmptyView(this, "", recycler_view))
            }
            mAdapter.setNewInstance(bean.data)
        } else {
            mAdapter.addData(bean.data)
        }


        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(this, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }

    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }


}