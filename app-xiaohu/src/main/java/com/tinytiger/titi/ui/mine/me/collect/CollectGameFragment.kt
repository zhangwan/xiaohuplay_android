package com.tinytiger.titi.ui.mine.me.collect

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.basis.BasisFragment
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.mygame.MineCollectGameAdapter
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.mine.me.collect.vp.CollectGamePresenter
import kotlinx.android.synthetic.main.mine_fragment_collection.*
import kotlinx.android.synthetic.main.mine_fragment_collection.recycler_view
import kotlinx.android.synthetic.main.mine_fragment_collection.refreshLayout

/**
 * @author zwy
 * create at 2020/6/29 0029
 * description:
 */
class CollectGameFragment : BasisFragment<CollectGamePresenter>() {

    private val mAdapter by lazy { MineCollectGameAdapter() }
    private var is_auto = true
    companion object {

        fun getInstance(): CollectGameFragment {
            val fragment = CollectGameFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.mine_fragment_collection

    override fun initView() {
        //评论详情
        mAdapter.dp3 = ScreenUtil.getScreenWidth() - Dp2PxUtils.dip2px(activity, 30)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = mAdapter
//        mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
        initListener()

    }

    fun initListener() {
        refreshLayout.setOnRefreshListener {
            page = 1
            basePresenter?.getUserGameAmwayCollectList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            basePresenter?.getUserGameAmwayCollectList(page)
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
                    basePresenter?.batchCancelCollectNode(ids)
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

    fun showGameFollow(bean: MineGameListBean.Data) {
        mAdapter.removeAllFooterView()

        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
            }
            mAdapter.setNewInstance(bean.data)
        } else {
            mAdapter.addData(bean.data)
        }
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            mAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
        } else {
            refreshLayout.setEnableLoadMore(true)
        }
        if (activity is MineCollectsActivity) {
            (activity as MineCollectsActivity).showEditButton(mAdapter.data.size,mAdapter.showDeleteIcon)
            (activity as MineCollectsActivity).showNumber(bean.total,0)
        }

    }

    override fun onResume() {
        super.onResume()
        page = 1
        basePresenter?.getUserGameAmwayCollectList(page)
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

    fun batchCancelCollect() {
        showToast(getString(R.string.collect_succeed_hint))
        page = 1
        mAdapter.showDeleteIcon = false
        basePresenter?.getUserGameAmwayCollectList(page)
        rlSelect.visibility = View.GONE

    }
}