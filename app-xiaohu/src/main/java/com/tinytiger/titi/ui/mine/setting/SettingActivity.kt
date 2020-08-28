package com.tinytiger.titi.ui.mine.setting


import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.FileUtils
import com.tinytiger.common.utils.image.ImageUtil

import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.titi.data.MyUserData

import com.tinytiger.titi.im.config.LogoutHelper
import com.tinytiger.titi.ui.login.LoginActivity
import com.tinytiger.titi.ui.mine.setting.user.SettingAccountActivity
import com.tinytiger.titi.ui.mine.setting.user.SettingUserInfoActivity

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallbackWrapper

import com.netease.nimlib.sdk.misc.DirCacheFileType
import com.netease.nimlib.sdk.misc.MiscService

import kotlinx.android.synthetic.main.mine_activity_setting.*
import java.util.ArrayList
import com.netease.nimlib.sdk.RequestCallback
import com.steven.fileprovider.FileProvider8
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.net.data.InitBean
import com.tinytiger.common.net.data.yungaem.InitVersionBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.widget.dialog.VersionUpdataDialog
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import com.xwdz.download.notify.DataUpdatedWatcher
import org.greenrobot.eventbus.EventBus
import java.io.File


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 设置 Activity
*/
class SettingActivity : BaseBackActivity() {

    //    private var isLoginStatus = false
    private var versionUpdataDialog: VersionUpdataDialog? = null
    private var updateInfo: InitVersionBean? = null

    companion object {
        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.mine_activity_setting

    override fun initData() {

    }

    override fun initView() {
        var versionJson = SpUtils.getString(R.string.update_version, "")
        if (versionJson != null) {
            updateInfo = JSON.parseObject(versionJson, InitVersionBean::class.java)
            if (updateInfo != null) {
                item_update.setContentText("v"+updateInfo?.version)
                item_update.setDotVisible(View.VISIBLE)
                item_update.visibility = View.VISIBLE
            } else {
                item_update.visibility = View.GONE
            }
        }

        title_view.centerTxt = getString(R.string.setting)
        title_view.setBackOnClick {
            finish()
        }

        item_account.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(this, "无网络")
                return@setOnClickListener
            }
            SettingAccountActivity.actionStart(this)
        }

        item_user_info.setOnClickListener {

            SettingUserInfoActivity.actionStart(this)
        }
        item_update.setOnClickListener {
            sendRequest()
        }

        btn_logout.setOnClickListener {
            //登出
            if (isLogin()) {
                TextDialog.create(supportFragmentManager).setMessage("确定退出登录？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            MyUserData.setUserClear()
                            LogoutHelper.logout()
                            EventBus.getDefault().post(MainLoginEvent( false))
                            LoginActivity.actionStart(this@SettingActivity)
                            finish()
                        }
                    }).show()
            } else {
                MyUserData.setUserClear()
                LogoutHelper.logout()
                EventBus.getDefault().post(MainLoginEvent( false))

                LoginActivity.actionStart(this@SettingActivity)
                finish()
            }
        }

        item_privacy.setOnClickListener {
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(this, "无网络")
                return@setOnClickListener
            }
            PrivacyActivity().actionStart(this)
        }

        item_clear.setOnClickListener {
            TextDialog.create(supportFragmentManager).setMessage("确定清除缓存?")
                .setViewListener(object : TextDialog.ViewListener {
                    override fun click() {
                        FileUtils.DeleteFile(File(cacheDir.absolutePath + "/cache"))
                        FileUtils.DeleteFile(File(ImageUtil.CACHE_IMG))
                        clearSDKDirCache()
                        cacheSizeim = 0
                        cacheSize = 0
                        cacheSizefile = 0
                        item_clear.setContentText(getIndexCacheSize())
                    }
                }).show()
        }

        item_notice.setOnClickListener {
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(this, "无网络")
                return@setOnClickListener
            }
            NoticeActivity().actionStart(this)
        }

        cacheSize = FileUtils.getFolderSize(cacheDir)
        cacheSizefile = FileUtils.getFolderSize(File(ImageUtil.CACHE_IMG))
        getSizeOfDirCache()

    }

    private fun sendRequest() {
        if (updateInfo?.update == 0) {
            return
        }
        versionUpdataDialog =
            VersionUpdataDialog.instance!!.builder(AppManager.getAppManager().currentActivity(),
                updateInfo!!, "",null)

        versionUpdataDialog?.pageType = 2
        versionUpdataDialog!!.show()

    }

    private val watcher = object : DataUpdatedWatcher() {
        override fun notifyUpdate(data: DownloadEntry) {
            if (updateInfo?.update_url == data.id) {
                if (data.status == DownloadEntry.Status.COMPLETED) {
                    if(updateInfo?.update==1){
                        versionUpdataDialog?.dismiss()
                    }
                    installApk(File(data.filePath))
                } else {
                    versionUpdataDialog?.setDownloadData(data)
                }
            } else if (data.status == DownloadEntry.Status.COMPLETED) {
                //下载完成,安装
                installApk(File(data.filePath))
            }
        }
    }

    private fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        FileProvider8.setIntentDataAndType(this, intent, "application/vnd.android.package-archive",
            file, true)
        startActivity(intent);
    }

    override fun onResume() {
        super.onResume()
        QuietDownloader.addObserver(watcher)
        item_clear.setContentText(getIndexCacheSize())

        ll_user.visibility = if (MyUserData.isEmptyToken()) View.GONE else View.VISIBLE

        if (isLogin()) {
            btn_logout.text = "退出登录"
            btn_logout.background =
                ContextCompat.getDrawable(this, R.drawable.stroke_rectangle_20_aaaaaa)
            btn_logout.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
        } else {
            btn_logout.text = "立即登录/注册"
            btn_logout.background =
                ContextCompat.getDrawable(this, R.drawable.solid_gradient_20_ffcc03)
            btn_logout.setTextColor(ContextCompat.getColor(this, R.color.gray33))
        }
    }

    override fun start() {

    }


    private var cacheSizeim = 0.toLong()
    private var cacheSize = 0.toLong()
    private var cacheSizefile = 0.toLong()
    private fun getIndexCacheSize(): String {
        return String.format("%.2f",
            (cacheSizeim + cacheSize + cacheSizefile) / (1024.0f * 1024.0f)) + "M"
    }

    private fun getSizeOfDirCache() {
        if (!isLogin()) {
            return
        }
        val types = ArrayList<DirCacheFileType>()
        types.add(DirCacheFileType.AUDIO)
        types.add(DirCacheFileType.THUMB)
        types.add(DirCacheFileType.IMAGE)
        types.add(DirCacheFileType.VIDEO)
        types.add(DirCacheFileType.OTHER)

        NIMClient.getService(MiscService::class.java).getSizeOfDirCache(types, 0, 0)
            .setCallback(object : RequestCallback<Long> {
                override fun onSuccess(size: Long) {
                    cacheSizeim = size
                    item_clear.setContentText(getIndexCacheSize())
                }

                override fun onFailed(code: Int) {}

                override fun onException(exception: Throwable) {}
            })
    }


    /**
     * 清理云信缓存
     */
    private fun clearSDKDirCache() {
        if (!isLogin()) {
            return
        }
        val types = ArrayList<DirCacheFileType>()
        types.add(DirCacheFileType.AUDIO)
        types.add(DirCacheFileType.THUMB)
        types.add(DirCacheFileType.IMAGE)
        types.add(DirCacheFileType.VIDEO)
        types.add(DirCacheFileType.OTHER)
        NIMClient.getService(MiscService::class.java).clearDirCache(types, 0, 0)
            .setCallback(object : RequestCallbackWrapper<Void>() {

                override fun onResult(code: Int, result: Void?, exception: Throwable?) {
                    cacheSizeim = 0
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            QuietDownloader.removeObserver(watcher)
        } catch (e: Exception) {

        }
    }

}