package com.tinytiger.titi.ui.mine.me.collect

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.UserCollectWikiList
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.HomePush2Adapter
import com.tinytiger.titi.adapter.mine.user.OnItemCheckListener
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.presenter.center.MineCollectsPresenter
import kotlinx.android.synthetic.main.mine_fragment_collection.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 我的收藏 Fragment 安利
*/
class CollectAmwayFragment : BaseFragment(), MineCollectContract.View {

    var isSelected: Boolean = false
    override fun batchCancelCollect() {
        showToast(getString(R.string.collect_succeed_hint))
        page = 1
        start()
        mAdapter.isShowEditStatus = false

        rlSelect.visibility = View.GONE

    }

    override fun getUserFollow(uid: String, is_mutual: Int) {
        for (i in mAdapter.data) {
            if (uid == i.user_id) {
                i.follow = is_mutual
            }
        }
        mAdapter.notifyDataSetChanged()
    }


    companion object {

        fun getInstance(): CollectAmwayFragment {
            val fragment = CollectAmwayFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mAdapter by lazy { HomePush2Adapter() }
    private val mPresenter by lazy { MineCollectsPresenter() }


    override fun getLayoutId(): Int = R.layout.mine_fragment_collection


    override fun initView() {
        mPresenter.attachView(this)
        //评论详情
        mAdapter.dp3 = ScreenUtil.getScreenWidth() - Dp2PxUtils.dip2px(activity, 30)
        recycler_view.adapter = mAdapter
        mAdapter.mListener = mOnItemCheckListener
        recycler_view.layoutManager = LinearLayoutManager(context)



        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getUserGameAmwayCollectList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getUserGameAmwayCollectList(page)
        }

        tvCancel.setOnClickListener {
            var ids = ""
            for (i in mAdapter.data) {
                if (i.isSelected) {
                    ids = "$ids,${i.id}"
                }
            }

            if (ids.length < 1) {
                //showToast("")
            } else {
                mPresenter.batchCancelCollectGameAssess(ids)
            }
        }
        cb_check.setOnCheckedChangeListener { _, b ->
            if (is_auto) {
                for (i in mAdapter.data) {
                    i.isSelected = b
                    isSelected = true
                }
                tvCancel.isEnabled = true
                mAdapter.notifyDataSetChanged()
            }

        }

        mAdapter.listener = object : HomePush2Adapter.OnHomePushListener {
            override fun onRefresh() {
                isRefresh = true
            }

            override fun onShare(mBean: AmwayBean) {
                ShareDialog.create(childFragmentManager)
                    .apply {
                        class_name = "GameAssess"
                        share_url = mBean.share_url + "?game_id=${mBean.game_id}&assess_id=${mBean.id}"
                        share_title = "《${mBean.game_name}》的评价"
                        share_desc = mBean.title
                        share_image = mBean.thumbnail
                        contentId = mBean.id
                        id=mBean.game_id
                    }
                    .show()
            }

            override fun onLike(mBean: AmwayBean) {
                mPresenter.likeAssessOrTag(mBean.game_id, mBean.id, "0")
            }

            override fun onAttention(mBean: AmwayBean) {
                mPresenter.follow(mBean.user_id, mBean.follow)
            }
        }

        start()
    }

    override fun start() {
        page = 1
        mPresenter.getUserGameAmwayCollectList(page)
    }

    //自动点击
    private var is_auto = true


    private val mOnItemCheckListener: OnItemCheckListener =
        OnItemCheckListener { is_check ->
            var num = 0
            if (mAdapter.data.size > 0) {
                for (item in mAdapter.data) {
                    if (item.isSelected) {
                        num++
                    }
                }
                tvCancel.isEnabled = num >= 1
            }
            if (is_check) {
                if (num == mAdapter.data.size) {
                    is_auto = false
                    cb_check.isChecked = true
                    is_auto = true
                }
            } else {
                is_auto = false
                cb_check.isChecked = false
                is_auto = true
            }
        }


    fun showEditStatus(is_show: Boolean) {

//        if(activity is MineCollectsActivity){
//            (activity as MineCollectsActivity).showEditButton(mAdapter.data.size)
//        }


        if (is_show == mAdapter.isShowEditStatus) {
            return
        }

        if (is_show && mAdapter.data.size > 0) {
            rlSelect.visibility = View.VISIBLE
        } else {
            rlSelect.visibility = View.GONE
        }
        cb_check.isChecked = false

        for (i in mAdapter.data) {
            i.isSelected = false
        }

        mAdapter.isShowEditStatus = is_show
        mAdapter.notifyDataSetChanged()


    }

    private var total = 0

    override fun showAmwayCollectList(bean: UserGameAmwayList.Data) {
        total = bean.total
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
            }
            mAdapter.setNewInstance(bean.data)
        } else {
            mAdapter.addData(bean.data)
        }

        mAdapter.removeAllFooterView()
        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }

        if (activity is MineCollectsActivity) {
            (activity as MineCollectsActivity).showEditButton(mAdapter.data.size,mAdapter.isShowEditStatus)
            (activity as MineCollectsActivity).showNumber(total,3)
        }
        if (mAdapter.data.size == 0) {
            rlSelect.visibility = View.GONE
        }
    }


    override fun showNewsCollectList(bean: MyContentListBean.DataBean) {

    }

    override fun showUserWikiCollectList(bean: UserCollectWikiList.Data) {

    }

    override fun showPostCollectList(bean: PostInfoBean) {
    }


    private var isRefresh = false

    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            isRefresh = false

            start()
        }
        if (activity is MineCollectsActivity) {
            (activity as MineCollectsActivity).showEditButton(mAdapter.data.size,mAdapter.isShowEditStatus)
        }
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()

    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        showToast(errorMsg)
        dismissLoading()
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() })
                )
            }
        } else {

        }
    }
}