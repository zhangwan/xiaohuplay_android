package com.tinytiger.titi.ui.circle.post

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat


import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.postsend.*

import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R

import com.tinytiger.titi.adapter.circle.post.SelectModuleAdapter
import com.tinytiger.titi.mvp.contract.circle.CirclesPostContract
import com.tinytiger.titi.mvp.presenter.circle.CirclesPostPresenter
import kotlinx.android.synthetic.main.activity_base_recycler.*


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 4:26
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 选择发布帖子模块
 */
class SelectModuleActivity : BaseBackActivity(), CirclesPostContract.View {

    private val mAdapter by lazy { SelectModuleAdapter() }
    private val mPresenter by lazy { CirclesPostPresenter() }

    companion object {
        fun actionStart(context: Activity, circleId: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, SelectModuleActivity::class.java)
            intent.putExtra("circleId", circleId)
            context.startActivityForResult(intent, 92)
        }
    }


    override fun layoutId(): Int = R.layout.activity_base_recycler
    var circleId = ""
    override fun initData() {
        mPresenter.attachView(this)
        circleId = intent.getStringExtra("circleId")
    }

    override fun initView() {
        tvTitle.centerTxt = "选择发布模块"
        tvTitle.setBackOnClick {
            finish()
        }
        fl_content.background = ContextCompat.getDrawable(this, R.color.white)

        recycler_view.adapter = mAdapter
        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setEnableRefresh(false)

        mAdapter.addChildClickViewIds(R.id.ll_content)
        mAdapter.setOnItemChildClickListener { _, _, position ->
            val i = Intent()
            i.putExtra("ModuleName", mAdapter.data[position].name)
            i.putExtra("ModuleId", mAdapter.data[position].id)
            if (mAdapter.data[position].tag_type==2){
                i.putExtra("ModuleType", 1)
            }

            setResult(Activity.RESULT_OK, i)
            finish()
        }
        start()
    }

    override fun start() {
        mPresenter.indexCircleByModular1(circleId, page)
    }


    override fun showCircleByCate(bean: SelectCircler2Bean) {

    }

    override fun showCircleByCate(bean: SelectCircler1Bean) {

    }

    override fun showCircleByModular(bean: SelectCirclerNameBean) {

        if (bean.data != null && bean.data.size > 0) {
            mAdapter.setNewInstance(bean.data)
        } else {
            mAdapter.setEmptyView(
                RefreshView.getEmptyView(
                    this,
                    "",
                    recycler_view
                )
            )
        }

    }

    override fun showCircleByModular(bean: SelectCirclerName2Bean) {

    }

    override fun showUserList(bean: SelectFriendBean) {

    }

    override fun showFollowUserList(bean: SelectFriend2Bean) {

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
                mAdapter.setEmptyView(RefreshView.getNetworkView(this, recycler_view) { start() })
            }
        }
    }
}