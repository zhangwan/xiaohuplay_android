package com.tinytiger.titi.ui.mine.other

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.center.UserMedalList
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.activity_base_recycler.*
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.adapter.mine.MineMeritListAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.center.MineMeritContract
import com.tinytiger.titi.mvp.presenter.center.MineMeritPresenter
import com.tinytiger.titi.ui.web.WebActivity
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 个人中心 - 我的功勋 Activity
*/
class MineMeritActivity : BaseBackActivity(), MineMeritContract.View {


    private val mPresenter by lazy { MineMeritPresenter() }

    private val mAdapter by lazy { MineMeritListAdapter() }

    companion object {
        fun actionStart(context: Context) {
            actionStart(context, "0")
        }

        fun actionStart(context: Context, id: String) {
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, MineMeritActivity::class.java)
            intent.putExtra("medal_id", id)
            context.startActivity(intent)
        }

    }


    override fun layoutId(): Int = R.layout.activity_base_recycler
    /**
     * 选中id
     */
    private var medal_id = "0"

    override fun initData() {
        mPresenter.attachView(this)
        medal_id = intent.getStringExtra("medal_id")
    }

    var sePosition = 0
    override fun initView() {
        tvTitle.centerTxt = "我的勋章"
        tvTitle.setBackOnClick {
            finish()
        }
        tvTitle.setRightIVIcon(R.mipmap.icon_question)
        tvTitle.setRightIVClick {
            if (medal_introduce_url.isNotEmpty()) {
                WebActivity.actionStart(this, medal_introduce_url)
            }
        }

        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        mAdapter.addChildClickViewIds(R.id.tv_action)
        mAdapter.setOnItemChildClickListener { _, view, position ->

            if (view.id == R.id.tv_action) {
                if (mAdapter.data[position].is_have != 0) {
                    var has_use = false

                    for (merit in mAdapter.data) {
                        if (merit.is_have == 1 && merit.is_use == 1) {
                            has_use = true
                        }
                    }

                    if (!has_use) {
                        mPresenter.wearMedal(mAdapter.data[position].id, position)
                    } else {
                        val msg =
                            if (mAdapter.data[position].is_use == 0) "佩戴该勋章后之前佩戴的勋章将会被替换，确定替换吗？" else "确定卸下该勋章吗？"

                        TextDialog.create(supportFragmentManager).setMessage(msg)
                            .setViewListener(object : TextDialog.ViewListener {
                                override fun click() {
                                    mPresenter.wearMedal(mAdapter.data[position].id, position)
                                }
                            }).show()
                    }
                }
            }
        }

        start()

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableLoadMore(false)

    }


    override fun start() {
        page = 1
        mPresenter.getMedalList(page)
    }


    private var medal_introduce_url = ""

    override fun showMedalList(bean: UserMedalList.DataBean) {
        mAdapter.removeAllFooterView()
        if (bean.list == null || bean.list.size == 0) {
            mAdapter.setEmptyView(RefreshView.getEmptyView(this, "暂无数据", recycler_view))
        } else {
            mAdapter.setNewInstance(bean.list)
            mAdapter.addFooterView(RefreshView.getNewFooterView(this, "", recycler_view), 0)
            for (i in 0..mAdapter.data.size - 1) {
                if (medal_id == mAdapter.data[i].id) {
                    sePosition = i
                }
            }
        }


        if (sePosition > 4) {
            RefreshView.smoothMoveToPosition(recycler_view, sePosition)
        }

        if (medal_introduce_url != null) {
            medal_introduce_url = bean.medal_introduce_url
        }
    }

    override fun showWearMedal(position: Int) {
        start()
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        if (mErrorView != null) {
            fl_content.removeView(mErrorView)
            mErrorView = null
        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null && mAdapter.data.size == 0) {
                mErrorView = getErrorLayout()
                fl_content.addView(mErrorView)
            }
        } else {
            dismissLoading()
        }
    }


}