package com.tinytiger.titi.ui.home2


import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger

import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.UserStatusEvent
import kotlinx.android.synthetic.main.home_fagment_recommend.*
import com.tinytiger.titi.R


import com.tinytiger.common.net.data.home2.*
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog

import com.tinytiger.titi.adapter.home2.HomePushAdapter
import com.tinytiger.titi.event.AttentionEvent

import com.tinytiger.titi.mvp.contract.gametools.GameHomeContract
import com.tinytiger.titi.mvp.presenter.gametools.GameHomePresenter
import com.tinytiger.common.utils.umeng.HomeAgentUtils
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.main.MainActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 *
 * @author zhw_luke
 * @date 2019/10/30 0030 下午 1:51
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页 安利文
 */
class HomeAmwayFragment : BaseFragment(), GameHomeContract.View {

    override fun getUserFollow(uid: String, is_mutual: Int) {
        for (i in mAdapter.data) {
            if (uid == i.user_id) {
                i.follow = is_mutual
            }
        }
        mAdapter.notifyDataSetChanged()
    }


    override fun loadCategoryList(bean: GameCategoryBean) {

    }

    override fun loadContentList(bean: ContentListBean) {

    }


    override fun getGameList(bean: GameListBean, type: Int) {

    }

    override fun loadAmwayWall(bean: AmwayWallListBean) {
        if (bean.data != null && bean.data.size > 0) {
            if (typeRefresh) {
                mAdapter.setNewInstance(bean.data)
            } else {
                mAdapter.addData(bean.data)
            }
        } else {
            mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
        }
    }


    companion object {
        fun getInstance(): HomeAmwayFragment {
            val fragment = HomeAmwayFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.home_fagment_recommend

    private val mPresenter by lazy { GameHomePresenter() }
    private val mAdapter by lazy { HomePushAdapter() }

    private var typeRefresh = true

    /**
     * 初始化 ViewI
     */
    override fun initView() {
        TAG = "HomeAmwayFragment"
        mPresenter.attachView(this)

        fl_content.background = ContextCompat.getDrawable(activity!!, R.color.color_bg)


        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        recycler_view.adapter = mAdapter
        refreshLayout.setOnRefreshListener { _ ->
            typeRefresh = true
            start()
        }

        refreshLayout.setOnLoadMoreListener { _ ->
            typeRefresh = false
            start()
        }

        mAdapter.listener = object : HomePushAdapter.OnHomePushListener {
            override fun onChecked(is_check: Boolean) {

            }

            override fun onShare(item: AmwayBean) {
                ShareDialog.create(childFragmentManager).apply {
                    class_name = "GameAssess"
                    share_url = item.share_url + "?game_id=${item.game_id}&assess_id=${item.id}"
                    share_title = "《${item.game_name}》的评价"
                    share_desc = item.title
                    share_image = item.thumbnail

                    contentId = item.id
                    id = item.game_id
                }.show()
            }

            override fun onLike(mBean: AmwayBean) {
                mPresenter.likeAssessOrTag(mBean.game_id, mBean.id, mBean.is_like)
            }

            override fun onAttention(mBean: AmwayBean) {
                mPresenter.follow(mBean.user_id, mBean.follow)
            }
        }

        var totalDy = 0
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalDy += dy
//                if (totalDy > 800) {
//                    (activity as MainActivity).setShowTop(View.VISIBLE)
//                }else{
//                    (activity as MainActivity).setShowTop(View.GONE)
//                }
            }
        })
    }

    override fun start() {
        mPresenter.amwayWallRecommend("amway")
    }

    override fun onResume() {
        super.onResume()
        // TCAgent.onPageStart(activity, "页面-首页-安利文")
        if (mAdapter.data.size == 0) {
            HomeAgentUtils.setAmwayShow()
            start()
        }
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(activity, recycler_view, { start() }))
            } else {
                showToast(errorMsg)
            }
        } else {
            showToast(errorMsg)
        }
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    /**
     * 黑名单列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserStatusEvent(event: UserStatusEvent) {
        if (event.type == 1) {
            val ls = ArrayList<AmwayBean>()
            for (i in mAdapter.data) {
                if (i.user_id == event.user_id) {
                    ls.add(i)
                }
            }
            for (i in ls) {
                mAdapter.data.remove(i)
            }

            mAdapter.notifyDataSetChanged()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAttentionEvent(event: AttentionEvent) {
        for (i in 0..mAdapter.data.size - 1) {
            if (mAdapter.data[i].user_id == event.userId) {
                mAdapter.data[i].follow = event.isMutual
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    /**
     * 双击刷新
     */
    fun setDoubleRefresh() {
        startTop()
        refreshLayout.autoRefresh()
    }
    fun startTop(){
        RefreshView.smoothMoveToPosition(recycler_view, 0)
    }
}
