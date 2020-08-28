package com.tinytiger.titi.ui.game.info

import android.graphics.Bitmap
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.gametools.*
import com.tinytiger.common.net.data.home2.NewsBeanMulti
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.home2.HomeGameAdapter
import com.tinytiger.titi.mvp.contract.gametools.GameContentContract
import com.tinytiger.titi.mvp.presenter.gametools.GameContentPresenter
import com.tinytiger.titi.ui.game.listener.OnGameStatusListener
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.my_game_fragment_content.*
import java.util.ArrayList


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 游戏详情 Fragment 游戏资讯页
*/
class GameContentFragment : BaseFragment(), GameContentContract.View {

    private var game_id: String = "0"
    private var logo:String=""
    private var nameTitle:String=""
    private val mData = ArrayList<NewsBeanMulti>()

    private val mMultiAdapter by lazy { HomeGameAdapter(mData) }

    private var mOnTitleStatusListener: OnGameStatusListener? = null

    companion object {

        fun getInstance(game_id: String,logo:String,name:String,listener: OnGameStatusListener): GameContentFragment {
            val fragment = GameContentFragment()
            val bundle = Bundle()
            fragment.game_id = game_id
            fragment.logo=logo
            fragment.nameTitle=name
            fragment.arguments = bundle
            fragment.mOnTitleStatusListener = listener
            return fragment
        }
    }

    private val mPresenter by lazy { GameContentPresenter() }


    override fun getLayoutId(): Int = R.layout.my_game_fragment_content


    override fun initView() {
        mPresenter.attachView(this)
        initRecyclerView()
        start()
    }


    private fun initRecyclerView() {
        recycler_view.adapter = mMultiAdapter
        mMultiAdapter.logo=logo
        mMultiAdapter.name=nameTitle
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnLoadMoreListener {
            page++
            mPresenter.getGameDetailContentData(game_id, page)
        }

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalDy += dy
                if (totalDy < topHeight) {
                    iv_top.alpha = 1 - (topHeight - totalDy) / topHeight
                } else {
                    iv_top.alpha = 1f
                }
            }
        })

        mMultiAdapter.mOnGameItemListener = object : HomeGameAdapter.OnGameItemListener {
            override fun setGameFollow(follow_status: Int) {
                (activity as GameDetailActivity).showAttention(follow_status)
            }
        }
    }

    private var topHeight = 300.0f
    private var totalDy = 0

    override fun start() {
        page = 1
        mPresenter.getGameDetailContentData(game_id, page)
    }


    override fun getGameDetailContentData(bean: GameContentListBean.Data) {
        mMultiAdapter.addData(NewsBeanMulti(bean.game_info))

        showUserWikiCollectList(bean.content_info)
    }

    fun showGameFollow(is_follow: Int, currentItem: Int) {
        if (mData.size > 0) {
            mData[0].gameInfoBean.follow_status = is_follow
            if (mMultiAdapter.afgvFollow != null) {
                mMultiAdapter.afgvFollow!!.setFollowGame(is_follow)
            }
        }
    }

    private fun showUserWikiCollectList(bean: GameContentList) {
        if (bean.data != null && bean.data.size > 0) {
            for (item in bean.data) {
                mData.add(NewsBeanMulti(item))
            }
        }

        refreshLayout.finishLoadMore()
        mMultiAdapter.removeAllFooterView()
        if (bean.current_page >= bean.last_page) {
            refreshLayout.setEnableLoadMore(false)
            if (mMultiAdapter.data.size == 1) {
                mData.add(NewsBeanMulti())
            } else {
                mMultiAdapter.addFooterView(RefreshView.getNewFooterView(context, "", recycler_view), 0)
            }
        } else {
            refreshLayout.setEnableLoadMore(true)
        }

        mMultiAdapter.notifyDataSetChanged()
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
            if (mMultiAdapter.data.size == 0) {
                mMultiAdapter.setEmptyView( RefreshView.getNetworkView(context, recycler_view, { start() }))
            }
        }
    }


    fun setBlurryBG(bit: Bitmap){
        if(iv_top==null){
            return
        }
        Blurry.with(context)
            .radius(10)
            .sampling(8)
            .async()
            .from(bit).into(iv_top)
    }

}