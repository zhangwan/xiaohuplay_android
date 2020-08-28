package com.tinytiger.titi.ui.mine.me.circle

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.basis.BasisFragment
import com.tinytiger.common.http.response.PageVo
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.net.data.circle.CircleHistoryBean
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.AddCircleAdapter
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.mine.me.circle.vp.AddCirclePresenter
import com.tinytiger.titi.ui.mine.me.circle.vp.CircleView
import kotlinx.android.synthetic.main.fragment_add_circle.*
import kotlinx.android.synthetic.main.fragment_add_circle.recycler_view
import kotlinx.android.synthetic.main.fragment_add_circle.refreshLayout
import kotlinx.android.synthetic.main.mine_fragment_collection.*


/**
 * 我加入的圈子
 */
class MyAddCircleFragment: BasisFragment<AddCirclePresenter>(),CircleView {

    companion object {

        fun getInstance(): MyAddCircleFragment {
            val fragment = MyAddCircleFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    private val mAdapter by lazy { AddCircleAdapter() }
    var dataList= arrayListOf<CircleHistoryBean>()

    override fun getLayoutId(): Int = R.layout.fragment_add_circle

    override fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = mAdapter
        mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
        loadData()
        initListener()
    }

    private fun loadData(){
        basePresenter?.getJoinList(page)
    }
    fun initListener(){

        refreshLayout.setOnRefreshListener {
            page = 1
            loadData()
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            loadData()
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            CirclesDetailsActivity.actionStart(context!!, dataList[position]?.id!!, "0", "0")
        }
    }
    override fun setHistoryList(list: PageVo<CircleHistoryBean>?) {
         if(list?.data==null){
             return
         }
         dataList= list?.data!!
         if(list.current_page==1){
             mAdapter.setNewInstance(dataList)
         }else{
             mAdapter.addData(dataList)
         }
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        if (list.current_page!! >= list.last_page!!) {
            refreshLayout.setEnableLoadMore(false)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }

    }


}