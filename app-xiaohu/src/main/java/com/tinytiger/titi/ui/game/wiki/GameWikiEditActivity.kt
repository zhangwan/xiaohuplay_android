package com.tinytiger.titi.ui.game.wiki

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.gametools.GameLibListBean
import com.tinytiger.common.net.data.gametools.GameWikiEditBean
import com.tinytiger.common.net.data.gametools.GameWikiListBean
import com.tinytiger.common.net.data.gametools.Wiki_info
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.wiki.GameWikiEditAdapter
import com.tinytiger.titi.mvp.contract.game.GameWikiEditContract
import com.tinytiger.titi.mvp.presenter.game.GameWikiEditPresenter
import kotlinx.android.synthetic.main.activity_game_wiki_edit.*
import kotlinx.android.synthetic.main.activity_game_wiki_edit.recycler_view
import kotlinx.android.synthetic.main.activity_game_wiki_edit.refreshLayout
import kotlinx.android.synthetic.main.activity_game_wiki_edit.title_view
import kotlinx.android.synthetic.main.activity_message_comment.*
import kotlinx.android.synthetic.main.base_recycler.*
import kotlinx.android.synthetic.main.content_fragment_comment.*
import kotlinx.android.synthetic.main.mine_fragment_collection.*
import kotlinx.android.synthetic.main.mine_fragment_fans.*
import kotlinx.android.synthetic.main.my_game_fragment_detail.*

/**
 * @author lmq001
 * @date 2020/06/01 11:10
 * @copyright 小虎互联科技
 * @doc 查看词条编辑者
 */
class GameWikiEditActivity : BaseBackActivity(), GameWikiEditContract.View {

    private lateinit var content_id: String
    private val mAdapter by lazy { GameWikiEditAdapter() }
    private val mPresenter by lazy { GameWikiEditPresenter() }

    companion object {
        fun actionStart(context: Context, content_id: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, GameWikiEditActivity::class.java)
            intent.putExtra("content_id", content_id)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.activity_game_wiki_edit

    override fun initData() {
        mPresenter.attachView(this)
        content_id = intent.getStringExtra("content_id")
    }

    override fun initView() {
        title_view.centerTxt = "词条编辑者"
        title_view.setBackOnClick {
            finish()
        }

        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        refreshLayout.setOnRefreshListener { _ ->
            start()
        }
        refreshLayout.setOnLoadMoreListener { _ ->
            mPresenter.getGameWikiList(content_id, page)
        }
        showLoading()
        start()
    }

    override fun start() {
        page = 1
        mPresenter.getGameWikiList(content_id, page)
    }

    override fun showGameWikiList(bean: GameWikiEditBean) {
        if (bean.data.current_page == 1) {
            mAdapter.setNewInstance(bean.data.wiki_info)
            if (bean.data.wiki_info != null && bean.data.wiki_info.size > 0) {
                mAdapter.setNewInstance(bean.data.wiki_info)
            } else {
                mAdapter.setEmptyView(RefreshView.getEmptyView(this, "", recycler_view))
                refreshLayout.setEnableLoadMore(false)
                mAdapter.setNewInstance(ArrayList())
            }
        } else {
            mAdapter.addData(bean.data.wiki_info)
        }

        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()
        mAdapter.removeAllFooterView()

        if (bean.data.current_page >= bean.data.last_page) {
            refreshLayout.setEnableLoadMore(false)
            if (bean.data.total > 0) {
                mAdapter.addFooterView(RefreshView.getNewFooterView(this, "", recycler_view), 0)
            }
        } else {
            refreshLayout.setEnableLoadMore(true)
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

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mAdapter.data.size == 0) {
                mAdapter.setEmptyView(RefreshView.getNetworkView(this, recycler_view) {
                    start()
                })
            }
        }
    }

}