package com.tinytiger.titi.ui.game.wiki

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.data.gametools.wiki.WikiStatusList
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.gametools.GameWikiContract
import com.tinytiger.titi.mvp.presenter.gametools.GameWikiPresenter
import com.tinytiger.titi.ui.web.WebActivity
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import kotlinx.android.synthetic.main.game_activity_wiki_detail.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 游戏详情-百科详情 Activity
*/
class GameWikiDetailActivity : BaseActivity(), GameWikiContract.View {

    override fun ApplyAdmin(msg: String) {

    }

    override fun showWikiStatusList(bean: WikiStatusList) {

    }

    private var wiki_id = "0"

    companion object {
        private const val EXTRA_WIKI_ID = "extra_wiki_id"

        fun actionStart(context: Context, wiki_id: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, GameWikiDetailActivity::class.java)
            intent.putExtra(EXTRA_WIKI_ID, wiki_id)
            context.startActivity(intent)
        }
    }


    private val mPresenter by lazy { GameWikiPresenter() }


    override fun layoutId(): Int = R.layout.game_activity_wiki_detail

    override fun initData() {
        mPresenter.attachView(this)
        wiki_id = intent.getStringExtra(EXTRA_WIKI_ID)
    }

    override fun initView() {
        title_view.setBackOnClick {
            finish()
        }

        start()
        //CircleAgentUtils.setWikeDetailWord(wiki_id,0)
    }

    override fun start() {
        mPresenter.getGameWikiDetail(wiki_id)
    }


    override fun showCollectGameWiki(collect: Int) {
        if (collect == 1) {
          //  CircleAgentUtils.setWikeDetailWord(wiki_id,2)
            showToast("收藏成功")
        } else {
            showToast("取消收藏成功")
        }
        showCollectWiki(collect)
    }

    private fun showCollectWiki(collect: Int) {
        is_collect = collect
        if (is_collect == 1) {
            ll_collect.text = "已收藏"
            ll_collect.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.mipmap.icon_collect_sel), null, null, null
            )

        } else {
            ll_collect.text = "收藏"
            ll_collect.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this, R.mipmap.icon_collect_nor), null, null, null
            )
        }
    }


    override fun showWikiModularList(bean: WikiModularList) {

    }

    override fun showWikiModularOtherList(bean: WikiModularOtherList) {

    }

    //#0未收藏，1已收藏
    private var is_collect = 0
    private var pedia_word_name = ""
    override fun showGameWikiDetail(bean: WikiDitailList) {
        pedia_word_name=bean.data.name
        title_view.centerTxt = bean.data.name
        if (bean.data.content.length > 10 && bean.data.content.substring(0, 4) == "http") {
            tv_content.loadUrl(bean.data.content)
        } else {
            tv_content.loadDataWithBaseURL(bean.data.content)
        }

        GlideUtil.loadImg(ivIcon, bean.data.game_info.logo)
        showCollectWiki(bean.data.is_collect)
        if (bean.data.game_info != null) tv_name.text = bean.data.game_info.name
        tv_edit_time.text = "该词条最后编辑于${TimeZoneUtil.getShortTimeShowString(bean.data.update_time)}"

        title_view.setRightIVIcon(R.mipmap.game_icon_share_black)
        title_view.setRightIVClick {
            ShareDialog.create(supportFragmentManager)
                .apply {
                    class_name = "WikiDetail"
                    share_url =   "${bean.data.url}?game_id=${bean.data.game_info.id}&wiki_id=${bean.data.id}"
                    share_title = "《${bean.data.game_info.name}》百科词条：${bean.data.name}"
                    share_desc = "加入小虎Hoo解锁更多精彩百科内容~"
                    share_image = bean.data.game_info.logo
                }.show()
          //  CircleAgentUtils.setWikeDetailWord(wiki_id,1)
        }

        //点击收藏
        ll_collect.setOnClickListener {
            mPresenter.collectGameWiki(bean.data.id, is_collect)
        }
        //点击纠错
        ll_recovery.setOnClickListener {
            if(isLoginStart()){
                WebActivity.actionStart(this,  "${bean.data.entry_error_url}?id=${bean.data.id}&game_id=${bean.data.game_info.id}", false)
            }
        }
        //查看编辑者
        tv_editor.setOnClickListener {
            WebActivity.actionStart(this,   "${bean.data.update_user_url}?content_id=$wiki_id"  , false)
        }
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        if (mErrorView != null) {
            fl_content.removeView(mErrorView)
            mErrorView = null
        }
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null) {
                mErrorView = getErrorLayout(true)
                fl_content.addView(mErrorView)
            }
        } else if (errorCode == 30021) {
            finish()
        }else{
            finish()
        }

    }


}