package com.tinytiger.titi.ui.mine.other

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.mine.CatListBean
import com.tinytiger.common.net.data.mine.FeedbackBean
import com.tinytiger.common.net.data.mine.FeedbackListBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.FeedbackListAdapter
import com.tinytiger.titi.mvp.contract.mine.FeedbackContract
import com.tinytiger.titi.mvp.presenter.mine.FeedbackPresenter
import kotlinx.android.synthetic.main.base_recycler.*


/*
* @author Tamas
* create at 2020/1/7 0007
* Email: ljw_163mail@163.com
* description: 
*/
class FeedbackFragment : BaseFragment(), FeedbackContract.View {
    override fun showCateList(bean: List<CatListBean.CateBean>) {

    }


    override fun getLayoutId(): Int = R.layout.base_recycler


    private val mAdapter by lazy { FeedbackListAdapter() }

    private val mPresenter by lazy { FeedbackPresenter() }


    private var type = 0

    companion object {

        fun getInstance(type: Int): FeedbackFragment {
            val fragment = FeedbackFragment()
            val bundle = Bundle()
            fragment.type = type
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun initView() {
        mPresenter.attachView(this)
        val dp7=Dp2PxUtils.dip2px(activity,7)
        recycler_view.setPadding(dp7,0,dp7,0)

        initRecyclerView()
        start()
    }

    private fun initRecyclerView() {
        recycler_view.setHasFixedSize(true)
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recycler_view.itemAnimator = null
        recycler_view.layoutManager = staggeredGridLayoutManager

        recycler_view.adapter = mAdapter

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                staggeredGridLayoutManager.invalidateSpanAssignments()
            }
        })

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getProposalList(type, page)
        }
    }


    override fun start() {
        page = 1
        mPresenter.getProposalList(type, page)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

//    override fun onResume() {
//        super.onResume()
//        mPresenter.getProposalList(type,page)
//
//    }


    /**
     * ================================================= Contract 接口 =================================================
     */

    override fun showProposalList(bean: FeedbackListBean.DataBean?) {

        refreshLayout.finishLoadMore()

        if (bean != null) {
            if (bean.total == 0) {
                mAdapter.setNewInstance(bean.data)
            } else {

                if (bean.current_page == 1) {
                    mAdapter.setNewInstance(bean.data)
                } else {
                    mAdapter.addData(bean.data)
                }
            }
            page = bean.current_page
            refreshLayout.setEnableLoadMore(bean.current_page < bean.last_page)
        }

    }

    override fun showResult() {
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
//        if (mErrorView != null) {
//            fl_content.removeView(mErrorView)
//            mErrorView = null
//        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
        showToast(errorMsg)
//        if (errorCode == ErrorStatus.NETWORK_ERROR) {
//            if (mErrorView == null && mAdapter.data.size == 0) {
//                mErrorView = getErrorLayout()
//                fl_content.addView(mErrorView)
//            }
//        } else {
//            dismissLoading()
//        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            val content = data!!.getStringExtra("feedback")

            val feedback = FeedbackBean()
            feedback.nickname = SpUtils.getString(R.string.nickname, "")
            feedback.avatar = SpUtils.getString(R.string.avatar, "")
            feedback.problem_desc = content
            mAdapter.addData(0, feedback)
            recycler_view.scrollToPosition(0)

        }
    }


     fun addFeedback(content: String) {
        val feedback = FeedbackBean()
        feedback.nickname = SpUtils.getString(R.string.nickname, "")
        feedback.avatar = SpUtils.getString(R.string.avatar, "")
        feedback.problem_desc = content
        feedback.medal_image=SpUtils.getString(R.string.medal_image,"")
        mAdapter.addData(0, feedback)
        if (recycler_view != null) {
            recycler_view.post {
                recycler_view.smoothScrollToPosition(0)
            }
        }

    }


}