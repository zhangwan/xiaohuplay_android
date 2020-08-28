package com.tinytiger.titi.ui.mine.setting


import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.app.NotificationManagerCompat
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.view.SwitchView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData

import com.tinytiger.titi.im.config.preference.UserPreferences
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.friend.FriendService
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseBackActivity
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.setting_activity_notice.*


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 通知
 */
class NoticeActivity : BaseBackActivity() {


    fun actionStart(context: Context) {
        val intent = Intent(context, NoticeActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.setting_activity_notice
    private val REQUEST_CODE_SETTING = 1

    init {
        //   mPresenter.attachView(this)
    }

    override fun initData() {

    }

    override fun initView() {
        tvTitle.setBackOnClick { finish() }


        rlNotive.setOnClickListener {
            //打开app权限列表
            AndPermission.with(this).runtime().setting().start(REQUEST_CODE_SETTING)
        }
        //声音
        sbSound.setOnStateChangedListener(object : SwitchView.OnStateChangedListener {
            override fun toggleToOff(view: SwitchView?) {
                if (!notif){
                    return
                }
                UserPreferences.setRingToggle(false)
                val config = UserPreferences.getStatusConfig()
                config.ring = false
                UserPreferences.setStatusConfig(config)
                NIMClient.updateStatusBarNotificationConfig(config)
                sbSound.isOpened = false
            }

            override fun toggleToOn(view: SwitchView?) {
                if (!notif){
                    return
                }
                UserPreferences.setRingToggle(true)
                val config = UserPreferences.getStatusConfig()
                config.ring = true
                UserPreferences.setStatusConfig(config)
                NIMClient.updateStatusBarNotificationConfig(config)
                sbSound.isOpened = true
            }
        })

        //震动
        sbShock.setOnStateChangedListener(object : SwitchView.OnStateChangedListener {
            override fun toggleToOff(view: SwitchView?) {
              if (!notif){
                  return
              }


                sbShock.isOpened = false
                UserPreferences.setVibrateToggle(false)
                val config = UserPreferences.getStatusConfig()
                config.vibrate = false
                UserPreferences.setStatusConfig(config)
                NIMClient.updateStatusBarNotificationConfig(config)
            }

            override fun toggleToOn(view: SwitchView) {
                if (!notif){
                    return
                }
                if (!notiveType) {
                    view.isOpened = false
                    return
                }
                view.isOpened = true
                UserPreferences.setVibrateToggle(true)
                val config = UserPreferences.getStatusConfig()
                config.vibrate = true
                UserPreferences.setStatusConfig(config)
                NIMClient.updateStatusBarNotificationConfig(config)
            }
        })

        //titixhdj86008d456b382b9044ee2b2  app评论
        //titixhdj0c2fd5a5d566c6505c91063  app点赞
        sbComment.setOnStateChangedListener(object : SwitchView.OnStateChangedListener {
            override fun toggleToOff(view: SwitchView) {
                if (!MyNetworkUtil.isNetworkAvailable(this@NoticeActivity)){
                    showToast("网络不可用")
                    return
                }
                if (!notif){
                    return
                }
                sbComment.isOpened = false
                setNotiveAccount(view, false, "titixhdj86008d456b382b9044ee2b2")
            }

            override fun toggleToOn(view: SwitchView) {
                if (!MyNetworkUtil.isNetworkAvailable(this@NoticeActivity)){
                    showToast("网络不可用")
                    return
                }
                if (!notif){
                    return
                }
                if (!notiveType) {
                    view.isOpened = false
                    return
                }
                view.isOpened = true
                setNotiveAccount(view, true, "titixhdj86008d456b382b9044ee2b2")
            }
        })

        sbLike.setOnStateChangedListener(object : SwitchView.OnStateChangedListener {
            override fun toggleToOff(view: SwitchView) {
               if (!MyNetworkUtil.isNetworkAvailable(this@NoticeActivity)){
                  showToast("网络不可用")
                   return
               }


                if (!notif){
                    return
                }
                sbLike.isOpened = false
                setNotiveAccount(view, false, "titixhdj0c2fd5a5d566c6505c91063")
            }

            override fun toggleToOn(view: SwitchView) {
                if (!MyNetworkUtil.isNetworkAvailable(this@NoticeActivity)){
                    showToast("网络不可用")
                    return
                }
                if (!notif){
                    return
                }
                if (!notiveType) {
                    view.isOpened = false
                    return
                }
                view.isOpened = true
                setNotiveAccount(view, true, "titixhdj0c2fd5a5d566c6505c91063")
            }
        })


        if (!isLogin()){
            ll_comment_like.visibility=View.GONE
        }
    }


    override fun start() {

    }

    var notif=false
    override fun onResume() {
        super.onResume()

        notif= NotificationManagerCompat.from(this).areNotificationsEnabled()
        if (notif){
            tvNotive.text = "已开启"
            notiveType = true
            setNotiveYes()
        }else{
            tvNotive.text = "已关闭"
            notiveType = false
            setNotiveNO()
        }

        if(MyUserData.isEmptyToken()){
            ll_comment_like.visibility= View.GONE
        }else{
            ll_comment_like.visibility= View.VISIBLE
        }

    }

    /**
     * 通知权限是否开启
     */
    private var notiveType = false


    private fun setNotiveYes() {
        sbSound.isOpened = UserPreferences.getRingToggle()
        sbShock.isOpened = UserPreferences.getVibrateToggle()

        sbComment.isOpened =NIMClient.getService(FriendService::class.java).isNeedMessageNotify("titixhdj86008d456b382b9044ee2b2")
        sbLike.isOpened = NIMClient.getService(FriendService::class.java).isNeedMessageNotify("titixhdj0c2fd5a5d566c6505c91063")
    }

    private fun setNotiveNO() {
        sbSound.isOpened = false
        sbShock.isOpened = false

        sbComment.isOpened = false
        sbLike.isOpened = false
    }

    private fun setNotiveAccount(view: SwitchView, checkState: Boolean, account: String) {
        NIMClient.getService(FriendService::class.java).setMessageNotify(account, checkState)
            .setCallback(object : RequestCallback<Void> {
                override fun onSuccess(param: Void?) {
                    view.isOpened = checkState
                    Logger.d("----onSuccess----------------")
                }

                override fun onFailed(code: Int) {
                    Logger.d("----onFailed----------------"+code)
                    if (code == 408) {
                        showToast(R.string.network_is_not_available)
                    } else {
                        showToast("操作失败")
                    }
                    view.isOpened = !checkState
                }

                override fun onException(exception: Throwable) {
                    Logger.d("-----onException---------------"+exception.toString())
                }
            })
    }

}
