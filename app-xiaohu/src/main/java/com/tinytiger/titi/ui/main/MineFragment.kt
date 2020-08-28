package com.tinytiger.titi.ui.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.net.data.mine.ShareAppBean
import com.tinytiger.common.net.data.mine.UserCenterBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.mine.MineContract
import com.tinytiger.titi.mvp.presenter.mine.MinePresenter
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.mine.me.collect.MineCollectsActivity
import com.tinytiger.titi.ui.mine.me.fans.FriendActivity
import com.tinytiger.titi.ui.mine.me.history.MineHistoryActivity
import com.tinytiger.titi.ui.mine.me.mygame.MineGameDownloadActivity
import com.tinytiger.titi.ui.mine.me.release.MineReleaseActivity
import com.tinytiger.titi.ui.mine.other.AboutActivity
import com.tinytiger.titi.ui.mine.other.FeedbackActivity
import com.tinytiger.titi.ui.mine.other.GameCloudActivity
import com.tinytiger.titi.ui.mine.other.MineMeritActivity
import com.tinytiger.titi.ui.mine.setting.SettingActivity
import com.tinytiger.titi.ui.mine.setting.user.SettingUserInfoActivity
import com.tinytiger.titi.ui.mine.talent.TalentActivity
import com.tinytiger.titi.ui.msg.ImActivity
import com.tinytiger.titi.ui.yungame.YunGameActivity
import com.tinytiger.titi.utils.CommonUtils
import kotlinx.android.synthetic.main.home_fragment_mine.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 3:56
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 个人中心页
 */
class MineFragment : BaseFragment(), MineContract.View {

    companion object {
        fun getInstance(): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.home_fragment_mine

    private val mPresenter by lazy { MinePresenter() }

    /**
     * 初始化 View
     */
    override fun initView() {
        mPresenter.attachView(this)

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        tv_about.setText(
            "v" + activity!!.packageManager
                .getPackageInfo(activity!!.packageName, PackageManager.GET_CONFIGURATIONS).versionName
        )

        rl_content.setOnClickListener {
            isLoginStart()
        }

        //我的主页
        iv_homepage.setOnClickListener {
            refreshData = true
            HomepageActivity.actionStart(context!!, MyUserData.getUserId())
        }

        //编辑资料
        iv_edit_info.setOnClickListener {
            refreshData = true
            SettingUserInfoActivity.actionStart(context!!)
        }

        //编辑资料
        avUser.setOnClickListener {
            refreshData = true
            SettingUserInfoActivity.actionStart(context!!)
        }

        //我的收藏
        tv_mine_collection.setOnClickListener {
            MineCollectsActivity.actionStart(context!!)
        }
        //历史记录
        tv_history_record.setOnClickListener {
            MineHistoryActivity.actionStart(context!!)
        }

        //产品建议墙
        tv_suggestion.setOnClickListener {
            FeedbackActivity.actionStart(context!!)
        }
        //分享
        tv_invite.setOnClickListener {
            if (shareBean == null) {
                mPresenter.shareApp()
            } else {
                showShareDialog()
            }
        }
        //云游戏秒玩
        tv_game.setOnClickListener {
            GameCloudActivity.actionStart(context!!)
        }
        //功勋墙
        tv_merit.setOnClickListener {

            refreshData = true
            MineMeritActivity.actionStart(context!!)
        }

        //达人认证
        tv_talent.setOnClickListener {
            TalentActivity.actionStart(context!!)
        }

        //关于
        rl_about.setOnClickListener {
            AboutActivity().actionStart(context!!)
        }

        //设置
        tv_setting.setOnClickListener {
            SettingActivity.actionStart(context!!)
        }

        //我的游戏
        tv_mine_games.setOnClickListener {
            MineGameDownloadActivity.actionStart(context!!)
        }
        //我的作品
        tv_mine_works.setOnClickListener {
            MineReleaseActivity.actionStart(context!!)
        }

        //我的关注
        tv_mine_friend.setOnClickListener {
            FriendActivity.actionStart(context!!,0)
        }
        //消息页
        tv_mine_msg.setOnClickListener {
            ImActivity.actionStart(activity!!)
        }

        //道具商城首页
        rl_mall.visibility=View.GONE
        rl_mall.setOnClickListener {
            // PropsActivity.actionStart(context!!)
            // EventBus.getDefault().post(ClassEvent("WebActivity", 0, "http://xiaohu.ittun.com/"))
            // EventBus.getDefault().post(ClassEvent("WebActivity", 0, "http://192.168.1.249/game_toolbox/js_bridge/"))

            val testGamePackageName= "com.tencent.tmgp.sgame";
            YunGameActivity.actionStart(activity!!,"2486",testGamePackageName)
        }
        rl_mall.text="测试H5"


        vLogin.setOnClickListener {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
        }

        meLogin(false)
        start()
    }


    private var refreshData = false
    override fun onResume() {
        super.onResume()
        if (refreshData) {
            refreshData = false
            start()
        }
        setMsg()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainLoginEvent(event: MainLoginEvent) {
        refreshData = true
        if (!event.logintype){
            tvMsg.visibility=View.GONE
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            start()
        }
    }


    override fun start() {
        if (MyUserData.isEmptyToken()) {
            mUserCenter = null
        }

        if (mUserCenter == null) {
            showProgress()
        }

        mPresenter.getUserCenter()
    }

    private var mUserCenter: UserCenterBean.DataBean? = null

    override fun showUserCenter(login_status: Boolean, bean: UserCenterBean.DataBean?) {
        meLogin(login_status)
        mUserCenter=null
        if (login_status) {
            mUserCenter = bean
            if (bean!!.userinfo.avatar != null) {
                SpUtils.saveSP(R.string.avatar, bean.userinfo.avatar)
                avUser.setAvatar(bean.userinfo.avatar,bean.userinfo.master_type)
            }
            tv_nickname.text=bean.userinfo.nickname

            if (bean.userinfo.medal_image != null && bean.userinfo.medal_image.isNotEmpty()) {
                GlideUtil.loadImg(iv_medal, bean.userinfo.medal_image)
                iv_medal.visibility = View.VISIBLE
            } else {
                iv_medal.visibility = View.GONE
            }
            if (bean.userinfo.gender != 0) {
                if (bean.userinfo.gender == 1){
                    tvMeInfo.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(activity!!, R.mipmap.icon_gender_male), null, null, null);
                }else{
                    tvMeInfo.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(activity!!, R.mipmap.icon_gender_girl), null, null, null);
                }
            } else {
                tvMeInfo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            var info=""
            if (bean.userinfo.citycn.isNullOrEmpty()) {

            } else {
                info = if (CommonUtils.isLimitedCity(bean.userinfo.provcn)) bean.userinfo.provcn else "${bean.userinfo.provcn}·${bean.userinfo.citycn}"
            }

            if (bean.userinfo.constellation.isNullOrEmpty()) {

            } else {
                info="$info  ${bean.userinfo.constellation}"
            }
            tvMeInfo.text=info
            tv_desc.text=(if (bean.userinfo.resume.isNullOrEmpty()) "介绍一下自己吧~" else bean.userinfo.resume)

            SpUtils.saveSP(R.string.props_user, bean.userinfo.shop_privilege)
            SpUtils.saveSP(R.string.master_type, bean.userinfo.master_type)
            SpUtils.saveSP(R.string.medal_image, bean.userinfo.medal_image)
        }
    }

    /**
     * 用户展示view
     * login_status true 登录 false 未登录
     */
    private fun meLogin(login_status: Boolean){
        if (login_status) {
            tv_login_or_register.visibility = View.GONE
            iv_edit_info.visibility = View.VISIBLE
            rlName.visibility = View.VISIBLE
            tvMeInfo.visibility = View.VISIBLE
            tv_desc.visibility = View.VISIBLE
            iv_homepage.visibility = View.VISIBLE
            tv_game.visibility=View.VISIBLE

            tv_mine_games.visibility = View.VISIBLE

            tv_merit.visibility = View.VISIBLE
            tv_talent.visibility = View.VISIBLE
            tv_invite.visibility = View.VISIBLE
            tv_mine_collection.visibility = View.VISIBLE
            tv_history_record.visibility = View.VISIBLE
            tv_suggestion.visibility = View.VISIBLE

            vLogin.visibility=View.GONE
            setMsg()
        } else {
            mUserCenter = null
            avUser.setAvatar(R.mipmap.icon_default_avatar)
            tv_login_or_register.visibility = View.VISIBLE
            iv_edit_info.visibility = View.GONE
            rlName.visibility = View.GONE
            tvMeInfo.visibility = View.GONE
            tv_desc.visibility = View.INVISIBLE
            iv_homepage.visibility = View.GONE
            tv_game.visibility=View.GONE

            if (SpUtils.getBoolean(R.string.download_apk, false)) {
                tv_mine_games.visibility = View.VISIBLE
            }else{
                tv_mine_games.visibility = View.GONE
            }

            tv_merit.visibility = View.GONE
            tv_talent.visibility = View.GONE
            tv_invite.visibility = View.GONE
            tv_mine_collection.visibility = View.GONE
            tv_history_record.visibility = View.GONE
            tv_suggestion.visibility = View.GONE
            tvMsg.visibility=View.GONE

            vLogin.visibility=View.VISIBLE
        }
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        when (errorCode) {
            ErrorStatus.LOGIN_ERROR -> {

            }
            ErrorStatus.NETWORK_ERROR -> {
                showToast(errorMsg)
                if (mUserCenter != null) {
                    return
                }
            }
        }
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private var shareBean: ShareAppBean? = null
    override fun getShareAppBean(bean: ShareAppBean) {
        shareBean = bean
        showShareDialog()
    }

    private fun showShareDialog() {
        ShareDialog.create(childFragmentManager)
            .apply {
                class_name = "no"
                share_url = shareBean!!.data.app_download_url
                share_title = shareBean!!.data.title
                share_desc = shareBean!!.data.slogan
                share_image = shareBean!!.data.logo_url
                userId = SpUtils.getString(com.tinytiger.common.R.string.user_id, "")
            }
            .show()
    }

    /**
     * 设置红点数据
     */
    fun setMsg(){
        val unreadNum= (activity as MainActivity).readNumMap[SpUtils.getString(R.string.user_id, "")] ?: 0
        if (unreadNum==0){
            tvMsg.visibility=View.GONE
        }else{
            tvMsg.visibility=View.VISIBLE
            if (unreadNum>99){
                tvMsg.text="99+"
            }else{
                tvMsg.text="$unreadNum"
            }
        }

    }
}
