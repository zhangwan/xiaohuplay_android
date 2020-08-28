package com.tinytiger.titi.ui.game.info

import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.widget.NestedScrollView
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.gametools.GameInfoBean
import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.data.gametools.wiki.WikiStatusList
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.info.SoulPlanetAdapter
import com.tinytiger.titi.mvp.contract.gametools.GameWikiContract
import com.tinytiger.titi.mvp.presenter.gametools.GameWikiPresenter
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.game_fragment_wiki_bright.*
import kotlinx.android.synthetic.main.my_game_item_title.*

/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 上午 11:37
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 点亮百科页
 */
class GameWikiBrightFragment : BaseFragment(), GameWikiContract.View {
    var name=""
    var gameId=""
    var logo=""

    override fun ApplyAdmin(msg: String) {

    }

    override fun showWikiStatusList(bean: WikiStatusList) {
        if (bean.data != null) {
            if (bean.data.user_status == 1) {
                llBright.visibility = View.GONE
            }

            if (bean.data.list != null && bean.data.list.data != null) {
                val list = ArrayList<String>()
                for (i in bean.data.list.data) {
                    list.add(i.avatar)
                }
                mAdapter.data = list
                soulPlanetView.setAdapter(mAdapter)
                soulPlanetView.setOnTagClickListener { parent, view, position ->
                    HomepageActivity.actionStart(context!!, bean.data.list.data[position].user_id)
                }
                total = bean.data.list.total
//                tvSize.text = "当前已有${total}人点亮了王者荣耀百科"
            }
        }
    }

    var total = 0
    override fun showCollectGameWiki(is_collect: Int) {
        llBright.visibility = View.GONE
        mAdapter.data?.add(SpUtils.getString(R.string.avatar, ""))
        total += 1
        mAdapter.notifyDataSetChanged()
//        tvSize.text = "当前已有${total}人点亮了王者荣耀百科"
    }


    companion object {
        fun getInstance(game_id: String,logo:String,name:String): GameWikiBrightFragment {
            val fragment = GameWikiBrightFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.gameId = game_id
            fragment.logo=logo
            fragment.name=name
            return fragment
        }
    }

    private val mPresenter by lazy { GameWikiPresenter() }
    private val mAdapter by lazy { SoulPlanetAdapter() }

    override fun getLayoutId(): Int = R.layout.game_fragment_wiki_bright



    /**
     * 设置游戏信息
     */
    fun setGameInfo() {

        if(!TextUtils.isEmpty(logo.trim())){
            GlideUtil.loadImg(iv_avatar, logo)
        }else{
            iv_avatar.setBackgroundResource(R.mipmap.icon_error_down3)
        }

        tv_name.text = name
        mPresenter.getWikiStatus(gameId, 1)
    }


    override fun initView() {
        mPresenter.attachView(this)

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            iv_top.alpha = scrollY / topHeight
        })


        llBright.visibility = View.VISIBLE
        iv_submit.setOnClickListener {
            mPresenter.openWiki(gameId)
        }
        setGameInfo()
    }

    private var topHeight = 300.0f

    override fun start() {
    }

    override fun showWikiModularList(bean: WikiModularList) {

    }

    override fun showWikiModularOtherList(bean: WikiModularOtherList) {

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
    }

    fun setBlurryBG(bit: Bitmap){
        Blurry.with(context)
            .radius(10)
            .sampling(8)
            .async()
            .from(bit).into(iv_top)
    }

}