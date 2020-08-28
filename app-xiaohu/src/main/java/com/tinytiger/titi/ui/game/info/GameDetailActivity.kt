package com.tinytiger.titi.ui.game.info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import com.netease.nim.uikit.common.framework.infra.TaskExecutor
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.data.gametools.GameCommentList
import com.tinytiger.common.net.data.gametools.GameInfoDetailBean
import com.tinytiger.common.net.data.home2.GameBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.image.RealPathFromUriUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.event.AlbumEvent
import com.tinytiger.titi.mvp.contract.gametools.GameDetailContract
import com.tinytiger.titi.mvp.presenter.gametools.GameDetailPresenter
import com.tinytiger.titi.ui.game.listener.OnGameStatusListener
import com.tinytiger.titi.widget.dialog.EvaluationDialog
import com.xwdz.download.core.QuietDownloader
import kotlinx.android.synthetic.main.my_game_activity_detail.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 游戏详情 Activity
*/
class GameDetailActivity : BaseBackActivity(), GameDetailContract.View,
    OnGameStatusListener {

    var game_id = "0"
    var index = 0
    var tag = 0
    var pageType=""
    /**
     *  管理员wifi权限
     */
    var wiki_type = 0

    companion object {
        //跳转页面码
        private const val EXTRA_GAME_ID = "extra_game_id"
        private const val EXTRA_TAG = "extra_tag"
        private const val EXTRA_TYPE="extra_type"

        fun actionStart(context: Context, game_id: String, tag: Int) {
            actionStart(context,game_id,tag,"")
        }

        fun actionStart(context: Context, game_id: String, tag: Int,type:String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }

            val intent = Intent(context, GameDetailActivity::class.java)
            intent.putExtra(EXTRA_GAME_ID, game_id)
            intent.putExtra(EXTRA_TAG, tag)
            intent.putExtra(EXTRA_TYPE,type)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { GameDetailPresenter() }


    override fun layoutId(): Int = R.layout.my_game_activity_detail

    override fun initData() {
        mPresenter.attachView(this)
        game_id = intent.getStringExtra(EXTRA_GAME_ID)
        tag = intent.getIntExtra(EXTRA_TAG, 0)
        if (intent.hasExtra(EXTRA_TYPE)){
            pageType=intent.getStringExtra(EXTRA_TYPE)
        }
        useStatusBarColor = false
        setWindowFeature()
        setIsOnlyTrackingLeftEdge(true)
    }

    override fun initView() {

        iv_back.setOnClickListener {
            finish()
        }

        iv_share.setOnClickListener {
            if (mGameInfoBean == null) {
                return@setOnClickListener
            }

            ShareDialog.create(supportFragmentManager)
                .apply {
                    class_name = "GameDetail"
                    share_url = mGameInfoBean!!.app_game_details + "?game_id=" + game_id
                    share_title = mGameInfoBean!!.game_info.name
                    share_desc = mGameInfoBean!!.game_info.one_introduce
                    share_image = mGameInfoBean!!.game_info.logo
                    contentId = game_id
                }
                .show()
        }
        token = SpUtils.getString(R.string.token, "")
        start()
    }

    private var mMyGameDetailFragment: GameDetailFragment? = null
    private fun initMagicIndicator() {
        val transaction = supportFragmentManager.beginTransaction()
        mMyGameDetailFragment?.let {
            transaction.show(it)
        } ?: GameDetailFragment.getInstance(game_id,
            mGameInfoBean,
            this).let {
            mMyGameDetailFragment = it
            transaction.add(R.id.fl_container, it, "mFragment1")
        }
        transaction.commitAllowingStateLoss()

        mGameInfoBean!!.game_info.id = game_id
        mHandler.postDelayed({
            setRunBG(mGameInfoBean?.game_info!!.background)
        }, 400)
    }


    private var token = ""
    override fun start() {
        mPresenter.getGameInfo(game_id, SpUtils.getString(R.string.user_id, ""))
    }

    override fun onResume() {
        super.onResume()

        if (SpUtils.getString(R.string.token, "") != token) {
            token = SpUtils.getString(R.string.token, "")
            start()
        }
        QuietDownloader.recoverAll()
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
        if (errorCode == 30002) {
            finish()
        } else if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mErrorView == null && mGameInfoBean == null) {
                mErrorView = getErrorLayout(true)
                fl_content.addView(mErrorView)
            }
        }
    }


    private var mGameInfoBean: GameInfoDetailBean.Data? = null

    override fun showGameInfoData(bean: GameInfoDetailBean.Data) {
        mGameInfoBean = bean

        GlideUtil.loadImg(iv_top_layout, bean.game_info.background)
        GlideUtil.loadImg(iv_avatar, bean.game_info.logo)
        tv_title.text = bean.game_info.name
        if (bean.assess_info.show_score == 0) {
            ratingBar.visibility = View.GONE
            tvNoScore.visibility = View.VISIBLE
        } else {
            ratingBar.rating = bean.assess_info.average.toFloat()
        }

        if (mMyGameDetailFragment==null){
            initMagicIndicator()
        }else{
            //切换用户后处理
            val is_follow = mGameInfoBean!!.game_info!!.follow_status
            //mMyGameDetailFragment?.mMultiAdapter?.setGoneScore(mGameInfoBean!!.assess_info!!.is_assess)
            mMyGameDetailFragment?.start()
            mMyGameDetailFragment?.showGameFollow(is_follow, -1)
        }

        GameAgentUtils.setGameDetailClick(game_id, bean.game_info.package_name_android, tag)
    }

    var bgBitmap: Bitmap? = null
    private val executor = TaskExecutor("image", TaskExecutor.defaultConfig, true)
    private fun setRunBG(url: String) {
        executor.execute {
            try {
                bgBitmap = Glide.with(this).asBitmap().error(R.mipmap.ic_launcher).override(200, 100).load(url).submit()
                    .get(3000, TimeUnit.MILLISECONDS)
                mMyGameDetailFragment?.setBlurryBG(bgBitmap!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun showGameFollow(is_follow: Int, currentItem: Int) {
        EventBus.getDefault()
            .post(GameBean(game_id, mGameInfoBean!!.game_info.logo, mGameInfoBean!!.game_info.name, is_follow == 1))

        mGameInfoBean?.game_info?.follow_status = is_follow

        if (is_follow == 1) {
            GameAgentUtils.setGameDetailFollow(mGameInfoBean?.game_info!!.id)
        }

    }

    override fun showlikeAssess(position: Int, message: String, is_like: Int) {
    }

    override fun showFollowStatus(is_follow: Int, user_id: String) {
    }

    override fun addAppointSuccess() {
    }

    override fun showGameInfoCommentList(bean: GameCommentList.Data) {
    }

    /**
     * 关注游戏
     */
    override fun showAttention(is_follow: Int) {
        if (mGameInfoBean != null) {
            mPresenter.GameFollow(game_id, mGameInfoBean!!.game_info.follow_status)
        }
        mMyGameDetailFragment?.showGameFollow(is_follow, 0)
    }

    override fun onTitleShow(isShow: Boolean) {
        layout_title.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode and 0xffff == 9162 && data != null) {
                //游戏安利文评价页

                val realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.data)
                if (realPathFromUri != null) {
                    EvaluationDialog.count=0
                    EventBus.getDefault().post(AlbumEvent(realPathFromUri))
                } else {
                    showToast("获取图片失败")
                }
            } else if (requestCode == 99) {
                if (data == null) {
                    return
                }

                wiki_type = data.getIntExtra("wiki_type", 0)
            }
        }
    }



}