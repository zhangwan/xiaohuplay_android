package com.tinytiger.titi.ui.circle.post

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.postsend.DraftDetailBean
import com.tinytiger.common.net.data.circle.postsend.DraftListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.DraftAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesPost1Contract
import com.tinytiger.titi.mvp.presenter.circle.CirclesPost1Presenter
import kotlinx.android.synthetic.main.circler_activity_me.*
import kotlinx.android.synthetic.main.circler_activity_post_circle.recycler_view
import kotlinx.android.synthetic.main.circler_activity_post_circle.refreshLayout


/**
 *
 * @author zhw_luke
 * @date 2020/4/29 0029 上午 10:38
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 草稿箱
 */
class DraftActivity : BaseBackActivity(), CirclesPost1Contract.View {


    companion object {
        fun actionStart(context: Activity) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, DraftActivity::class.java)
            context.startActivityForResult(intent, 94)
        }
    }


    private val mPresenter by lazy { CirclesPost1Presenter() }

    private val mAdapter by lazy { DraftAdapter() }


    override fun layoutId(): Int = R.layout.circler_activity_me


    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        tvTitle.centerTxt = "草稿箱"
        tvTitle.setIvLine(View.GONE)
        tvTitle.setBackOnClick {
            finish()
        }
        fl_content.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
        recycler_view.adapter=mAdapter
        mAdapter.addChildClickViewIds(R.id.txt_delete,R.id.rl_content)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.txt_delete -> {
                    mPresenter.delPost(mAdapter.data[position].id)
                }
                R.id.rl_content -> {
                    val i = Intent()
                    i.putExtra("draft", mAdapter.data[position].id)
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }
            }
        }


        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener { _ ->
            page++
            start()
        }

        start()
    }


    var keywords = ""
    override fun start() {
        mPresenter.indexDrafts(page)
    }


    override fun sendPost(type: Int, is_draft: Int, detailBean: PostBean?) {
        mAdapter.deleteData(type)
    }

    override fun getDrafts(bean: DraftListBean) {
        if (bean.data.current_page == 1) {
            if (bean.data.data != null && bean.data.data.size > 0) {
                mAdapter.setNewInstance(bean.data.data)
            } else {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(
                        this,
                        "",
                        recycler_view
                    )
                )
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setNewInstance(ArrayList())
            }
        } else {

            mAdapter.addData(bean.data.data)
        }
        if (bean.data.current_page >= bean.data.last_page) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.resetNoMoreData()
        }

    }

    override fun getDraftDetail(bean: DraftDetailBean) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getNetworkView(this, recycler_view, { start() }))
            }
        }
    }

}