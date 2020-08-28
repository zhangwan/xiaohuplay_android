package com.tinytiger.titi.ui.mine.me.collect

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.UserCollectWikiList
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.presenter.center.MineCollectsPresenter
import com.tinytiger.titi.widget.dialog.FullPostDialog
import kotlinx.android.synthetic.main.mine_fragment_collection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/*
* @author zwy
* create at 2020/5/18 0013
* description: 我的收藏 Fragment 帖子
*/
class CollectNodeFragment : BaseFragment(), MineCollectContract.View,
    FullPostDialog.OnFullSheetListener {
    private val mPresenter by lazy { MineCollectsPresenter() }
    private val mAdapter by lazy { CommonPostNodeAdapter(activity!!, ArrayList()) }
    private var is_auto = true
    private var total = 0
    private var data = arrayListOf<PostBean>()

    override fun getLayoutId(): Int = R.layout.mine_fragment_collection

    override fun batchCancelCollect() {
        showToast(getString(R.string.collect_succeed_hint))
        page = 1
        mAdapter.showDeleteIcon = false
        start()

        rlSelect.visibility = View.GONE

    }

    override fun getUserFollow(uid: String, is_mutual: Int) {

    }

    override fun showAmwayCollectList(bean: UserGameAmwayList.Data) {

    }

    companion object {
        fun getInstance(): CollectNodeFragment {
            val fragment = CollectNodeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        //评论详情
        mAdapter.dp3 = ScreenUtil.getScreenWidth() - Dp2PxUtils.dip2px(activity, 30)
        mAdapter.pageIndex = ConstantsUtils.NOTE.PAGE_COLLECT
        //评论详情
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        initListener()
        start()

    }

    private fun initListener() {
        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getPostList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getPostList(page)
        }


        tvCancel.setOnClickListener {
            var ids = ""
            for (i in mAdapter.data) {
                if (i.isSelected) {
                    ids += "${i.id},"
                }
            }
            if (!TextUtils.isEmpty(ids)) {
                ids = ids.take(ids.length - 1)

                if (ids.isNotEmpty()) {
                    mPresenter.batchCancelCollectNode(ids)
                }
            }

        }
        cb_check.setOnCheckedChangeListener { _, b ->
            if (is_auto) {
                for (i in mAdapter.data) {
                    i.isSelected = b
                }
                tvCancel.isEnabled = true
                mAdapter.notifyDataSetChanged()
            }
        }
        mAdapter.shareListener = {
            ShareDialog.create(childFragmentManager).apply {
                    class_name = "Post"
                    share_url = "${it.share_url}?post_id=${it.id}"
                    share_title = "来自「${it.nickname}」"
                    share_desc = it.content
                    if (it.img_url != null && it.img_url.size > 0) {
                        share_image = it.img_url[0]
                    }
                    contentId = it.id
                    id = it.circle_id
                }.show()
        }
        mAdapter.likeListener = {
            (activity as MineCollectsActivity).mPresenter.likePost(it.id)
        }
        mAdapter.attentionListener = { item, isMutual ->
            mPresenter.doFollow(isMutual, item.user_id)
        }
        mAdapter.readListener = {
            FullPostDialog().apply {
                postBean = it
                listener = this@CollectNodeFragment
            }.show(childFragmentManager)
        }
        mAdapter.checkListener = {
            var num = 0
            if (mAdapter.data.size > 0) {
                for (item in mAdapter.data) {
                    if (item.isSelected) {
                        num++
                    }
                }
                tvCancel.isEnabled = num >= 1
            }
            if (it.isSelected) {
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAttentionEvent(event: AttentionEvent) {
        start()
    }

    private var isRefresh = false
    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            isRefresh = false

            start()
        }
        if (activity is MineCollectsActivity) {
            (activity as MineCollectsActivity).showEditButton(mAdapter.data.size,
                mAdapter.showDeleteIcon)
        }
    }


    fun showEditStatus(is_show: Boolean) {

        if (is_show == mAdapter.showDeleteIcon) {
            return
        }
        if (is_show) {
            rlSelect.visibility = View.VISIBLE
        } else {
            rlSelect.visibility = View.GONE
        }
        cb_check.isChecked = false
        for (i in mAdapter.data) {
            i.isSelected = false
        }
        mAdapter.showDeleteIcon = is_show
        mAdapter.notifyDataSetChanged()
    }


    override fun start() {
        mPresenter.getPostList(page)
    }

    override fun showNewsCollectList(bean: MyContentListBean.DataBean) {

    }

    override fun showUserWikiCollectList(bean: UserCollectWikiList.Data) {

    }

    override fun showPostCollectList(bean: PostInfoBean) {
        var postBean = bean.data
        total = postBean.total
        if (postBean.current_page == 1) {
            if (postBean.data == null || postBean.data.size == 0) {
                mAdapter.mEmptyView = (RefreshView.getEmptyView(activity, "", recycler_view))
            }

            data = postBean.data
        } else {
            data?.addAll(postBean.data)

        }
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

        if (postBean.current_page >= postBean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter?.mBottomView = RefreshView.getNewFooterView(context, "", recycler_view)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
        mAdapter.data = data
        mAdapter.notifyDataSetChanged()

        if (activity is MineCollectsActivity) {
            (activity as MineCollectsActivity).showEditButton(mAdapter.data.size,
                mAdapter.showDeleteIcon)
            (activity as MineCollectsActivity).showNumber(total, 1)
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
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.mEmptyView = (RefreshView.getNetworkView(activity, recycler_view) {
                    page = 1
                    start()
                })
                mAdapter?.notifyDataSetChanged()
            }
        } else {
            dismissLoading()
        }
    }

    override fun onDismiss(postid: String) {
        for (i in 0 until mAdapter.data.size) {
            if (mAdapter.data[i].id == postid) {
                mAdapter.notifyItemChanged(i)
            }
        }
    }
}