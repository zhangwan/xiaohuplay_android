package com.tinytiger.titi.ui.game.info

import android.graphics.Bitmap
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.gametools.GameInfoBean
import com.tinytiger.common.net.data.gametools.wiki.*
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList.WikiModular
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.info.MultiGameWikiAdapter
import com.tinytiger.titi.data.game.MultiMyGameWikiBean
import com.tinytiger.titi.mvp.contract.gametools.GameWikiContract
import com.tinytiger.titi.mvp.presenter.gametools.GameWikiPresenter
import com.tinytiger.titi.ui.game.wiki.GameWikiApplyActivity
import com.tinytiger.common.utils.umeng.BannerAgentUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.my_game_fragment_wiki.*
import java.util.*


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 上午 10:12
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 游戏详情 Fragment 百科页
 */
class GameWikiFragment : BaseFragment(), GameWikiContract.View {


    override fun ApplyAdmin(msg: String) {

    }

    override fun showWikiStatusList(bean: WikiStatusList) {

    }

    override fun showCollectGameWiki(is_collect: Int) {

    }

    private var game_id: String = "0"
    private var logo:String=""
    private var game_name: String = ""
    private var nameTitle:String=""

    companion object {
        fun getInstance(game_id: String,logo:String,name:String): GameWikiFragment {
            val fragment = GameWikiFragment()
            val bundle = Bundle()
            fragment.game_id = game_id
            fragment.logo=logo
            fragment.nameTitle=name
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mPresenter by lazy { GameWikiPresenter() }
    private val mAdapter by lazy { MultiGameWikiAdapter() }
    override fun getLayoutId(): Int = R.layout.my_game_fragment_wiki


    override fun initView() {
        mPresenter.attachView(this)
        initRecyclerView()
        mAdapter.game_id = game_id
        mAdapter.logo=logo
        mAdapter.name=nameTitle
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn)
        start()
        setGameInfo()

    }

    private fun initRecyclerView() {
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.addOnScrollListener(mOnScrollListener)

        mAdapter.mOnGameWikiItemListener = object : MultiGameWikiAdapter.OnGameWikiItemListener {
            override fun setGameFollow(follow_status: Int) {
            }

            override fun setWikiApply() {
                GameWikiApplyActivity.actionStart(
                    (activity as CirclesDetailsActivity),
                    (activity as CirclesDetailsActivity).wiki_type,
                    game_id, game_name
                )

            }

            override fun setBannerId(id: String) {
                mPresenter.bannerClick(id)
            }
        }
    }

    private var totalDy = 0
    private var topHeight = 300.0f
    private val mOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            totalDy += dy

            if (totalDy < topHeight) {
                iv_top.alpha = 1 - (topHeight - totalDy) / topHeight
            } else {
                iv_top.alpha = 1f
            }
        }
    }


    override fun start() {
        mPresenter.modularInfo(game_id)
    }

    fun setGameInfo() {
        game_name =nameTitle
       // CircleAgentUtils.setWikeDetail(game_name)
    }

    /**
     * 未加载数据集列表
     */
    private var dataWiki = ArrayList<WikiModular>()
    private var wiki = 0
    /**
     * 获取所有模块数据
     */
    override fun showWikiModularList(bean: WikiModularList) {
        val list = ArrayList<MultiMyGameWikiBean>()
        list.add(MultiMyGameWikiBean(0))
        if (bean.data == null || bean.data.size == 0) {
            //无数据
            list.add(MultiMyGameWikiBean(9))
            mAdapter.setNewInstance(list)
        } else {
            mAdapter.setNewInstance(list)
            //数据分类
            dataWiki.addAll(bean.data)
            setADdata()
        }

    }

    /**
     * 分模块数据
     */
    override fun showWikiModularOtherList(bean: WikiModularOtherList) {
        val list = ArrayList<MultiMyGameWikiBean>()
        if (bean.data != null) {
            if (mAdapter.data.size > 2) {
                //add分隔条
                list.add(MultiMyGameWikiBean(10))
            }
            //数据体
            for (i in bean.data) {
                //一级目录数据
                if (i.son_category != null && i.son_category.size > 0) {

                    val contentList = ArrayList<WikiCategoryBean>()
                    for (z in i.son_category) {
                        if (z.content_list != null && z.content_list.size > 0) {
                            contentList.add(z)
                        }
                    }
                    i.son_category = contentList
                }

                if (i.son_category != null && i.son_category.size > 0) {
                    list.add(MultiMyGameWikiBean(i, dataWiki[wiki].template_id))
                    list.add(MultiMyGameWikiBean(10))
                }
            }
            if (list.size > 1) {
                list.removeAt(list.size - 1)
                mAdapter.addData(list)
            }
        }
        wiki += 1

        setADdata()
    }


    private fun setADdata() {
        if (dataWiki.size <= wiki) {
            return
        }
        if (dataWiki[wiki].template_id > 2 && dataWiki[wiki].template_id < 6) {
            mPresenter.otherModularInfo("" + dataWiki[wiki].id)
        } else {
            if (dataWiki[wiki].template_id == 1 || dataWiki[wiki].template_id == 2) {
                var banner_id=""
                for (i in dataWiki[wiki].child_data){
                    banner_id="$banner_id,${i.id}"
                }

                if (banner_id.length>1){
                    banner_id=banner_id.substring(1,banner_id.length)
                }
              //  BannerAgentUtils.setBannerDetailShow(banner_id)
            }
            mAdapter.addData(MultiMyGameWikiBean(dataWiki[wiki]))
            wiki += 1
            setADdata()
        }
    }


    override fun showGameWikiDetail(bean: WikiDitailList) {

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
                mAdapter.setEmptyView(RefreshView.getNetworkView(context, recycler_view, { start() }))
            }
        }
    }

    fun setBlurryBG(bit: Bitmap) {
        Blurry.with(context)
            .radius(10)
            .sampling(8)
            .async()
            .from(bit).into(iv_top)
    }

}