package com.tinytiger.titi.ui.game

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.gametools.GameLibListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.GameLibrayAdapter
import com.tinytiger.titi.mvp.contract.gametools.GameLibContract
import com.tinytiger.titi.mvp.presenter.gametools.GameLibPresenter
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import com.xwdz.download.notify.DataUpdatedWatcher
import kotlinx.android.synthetic.main.game_fragment_library.*


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 4:26
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 游戏库 排行榜
 */
class GameLibraryFragment : BaseFragment(), GameLibContract.View {

    // 1->活跃版，2->热度版
    private var mType = 1

    private val mAdapter by lazy { GameLibrayAdapter() }
    private val mPresenter by lazy { GameLibPresenter() }

    companion object {
        fun getInstance(type: Int): GameLibraryFragment {
            val fragment = GameLibraryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mType = type
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.game_fragment_library

    override fun initView() {
        mPresenter.attachView(this)
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(activity)
        tv_desc.text = if (mType == 2) activity!!.getString(R.string.game_library_title_heat)
        else activity!!.getString(R.string.game_library_title_active)
        start()
    }

    override fun start() {
        // 1->活跃版，2->热度版
        mPresenter.getGameList(mType)
    }


    override fun showGameList(item: GameLibListBean) {
        if (item.data != null && item.data.data != null && item.data.data.size > 0) {
            mAdapter.setNewInstance(item.data.data)
        }

        if (mAdapter.data.size == 0) {
            mAdapter.setEmptyView(
                RefreshView.getEmptyView(activity, "", recycler_view)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        QuietDownloader.addObserver(watcher)
        mAdapter.installApk()
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
                mAdapter.setEmptyView(
                    RefreshView.getNetworkView(
                        activity,
                        recycler_view,
                        { start() })
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        QuietDownloader.removeObserver(watcher)
        mPresenter.detachView()
    }

    private val watcher = object : DataUpdatedWatcher() {
        override fun notifyUpdate(data: DownloadEntry) {
            mAdapter.notifyUpdate(data)

        }
    }
}