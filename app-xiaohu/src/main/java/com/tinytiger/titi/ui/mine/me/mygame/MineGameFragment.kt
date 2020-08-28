package com.tinytiger.titi.ui.mine.me.mygame

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.mygame.MineGameListAdapter
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.presenter.center.MineGamesPresenter
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.widget.popup.PopupGameInfo
import kotlinx.android.synthetic.main.base_recycler.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 我的游戏 Fragment 关注收藏
*/
class MineGameFragment : BaseFragment(), MineGameContract.View {

    companion object {
        //0  关注
        fun getInstance(): MineGameFragment {
            val fragment = MineGameFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mPresenter by lazy { MineGamesPresenter() }

    private val mAdapter by lazy { MineGameListAdapter() }

    override fun getLayoutId(): Int = R.layout.base_recycler


    override fun initView() {
        mPresenter.attachView(this)
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context!!)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            GameDetailActivity.actionStart(context!!, mAdapter.data[position].id,7)
        }

        mAdapter.addChildClickViewIds(R.id.tv_action,R.id.ivMore)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.tv_action -> {
                    if (mAdapter.data[position].has_application) {
                        val it =
                            context!!.packageManager.getLaunchIntentForPackage(mAdapter.data[position].package_name_android)
                        startActivity(it)
                    } else {
                        GameDetailActivity.actionStart(context!!, mAdapter.data[position].id,7)
                    }
                }
                R.id.ivMore -> {
                    clickMore(mAdapter.data[position].id,view)
                }
            }
        }


        refreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getUserFollowGameList(page)
        }

        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getUserFollowGameList(page)
        }

        start()
    }


    override fun start() {
        page = 1
        mPresenter.getUserFollowGameList(page)
    }


    private var isRefresh = false
    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            start()
            isRefresh = false
        }
    }


    override fun showFollowGameList(bean: MineGameListBean.Data) {
        mAdapter.removeAllFooterView()

        if (bean.current_page == 1) {
            if (bean.data == null || bean.data.size == 0) {
                mAdapter.setEmptyView(
                    RefreshView.getEmptyView(
                        context,
                        "",
                        recycler_view
                    )
                )
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

        (activity as MineGameDownloadActivity).showNumber(bean.total,0)
    }

    override fun showGameFollow(game_id: String) {
        refreshLayout.postDelayed({mAdapter.removeBean(game_id)
            (activity as MineGameDownloadActivity).showNumber(-1,0)
        },200)


    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
        showToast(errorMsg)
    }


    var mPopupWindow: PopupGameInfo? = null
    /**
     * 取消收藏pop
     */
    fun clickMore(game_id: String,rootView: View) {
        if (mPopupWindow == null) {
            mPopupWindow = PopupGameInfo(activity)
            mPopupWindow!!.setShowAnimation(
                createTranslateAnimation(1f, 0f, 0f, 0f))
                .dismissAnimation =
                createTranslateAnimation(0f, 1f, 0f, 0f)
            mPopupWindow!!.popupGravity = Gravity.END
        }
        mPopupWindow?.setPopInfo("取消收藏") {
            mPopupWindow?.dismiss()
            mPresenter.GameFollow(game_id,1)
        }

        val location = IntArray(2)
        rootView.getLocationInWindow(location)
        rootView.getLocationOnScreen(location)

        mPopupWindow!!.setBackground(null)
            .setBlurBackgroundEnable(false)
            .showPopupWindow(
                location[0] - Dp2PxUtils.dip2px(context, 80),
                location[1] + Dp2PxUtils.dip2px(context, 7)
            )
    }


    private fun createTranslateAnimation(fromX: Float, toX: Float, fromY: Float,
        toY: Float): Animation? {
        val animation: Animation =
            TranslateAnimation(Animation.RELATIVE_TO_SELF, fromX, Animation.RELATIVE_TO_SELF, toX,
                Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY)
        animation.duration = 500
        animation.interpolator = DecelerateInterpolator()
        return animation
    }
}