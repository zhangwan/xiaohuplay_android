package com.tinytiger.titi.app


import android.os.Build
import android.os.Process
import android.text.TextUtils
import android.webkit.WebView
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nim.uikit.api.UIKitOptions
import com.netease.nim.uikit.business.contact.core.query.PinYin
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.util.NIMUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseApp
import com.tinytiger.titi.im.DemoCache
import com.tinytiger.titi.im.NIMInitManager
import com.tinytiger.titi.im.config.preference.Preferences
import com.tinytiger.titi.im.config.preference.UserPreferences
import com.tinytiger.titi.im.event.DemoOnlineStateContentProvider
import com.tinytiger.titi.im.mixpush.DemoPushContentProvider
import com.tinytiger.titi.im.session.NimDemoLocationProvider
import com.tinytiger.titi.im.session.SessionHelper
import com.xwdz.download.DownloadConfig
import com.xwdz.download.core.QuietDownloader
import io.reactivex.plugins.RxJavaPlugins


/**
 * @author zhw_luke
 * @date 2019/10/21 0021 上午 11:47
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
class TitiApplication : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        DemoCache.setContext(this)
        NIMClient.init(this, loginInfo, NimSDKOptionConfig.getSDKOptions(this))
        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {
            PinYin.init(this)
            PinYin.validate()
            initUIKit()
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle())
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true)
        }

        //web
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WebView.setDataDirectorySuffix(Process.myPid().toString() + "")
        }

        //全局捕获rx中https请求异常
        RxJavaPlugins.setErrorHandler {
             Logger.d("onRxJavaErrorHandler ---->: $it")
        }

        setdownload()
        InitializeService.start(this)
    }

    private fun initUIKit() {

        val options = UIKitOptions()
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this)
        // 初始化
        NimUIKit.init(this, options)
        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(NimDemoLocationProvider())
        SessionHelper.init()
        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        NimUIKit.setCustomPushContentProvider(DemoPushContentProvider())
        NimUIKit.setOnlineStateContentProvider(DemoOnlineStateContentProvider())
    }

    private val loginInfo: LoginInfo?
        get() {
            val account = Preferences.getUserAccount()
            val token = Preferences.getUserToken()

            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
                DemoCache.setAccount(account.toLowerCase())
                return LoginInfo(account, token)
            } else {
                return null
            }
        }


    private fun setdownload() {
        val downloadConfig = DownloadConfig(this)
        downloadConfig.maxDownloadTasks = 2
        downloadConfig.isOpenRetry=true
        downloadConfig.maxRetryCount=10
      //  downloadConfig.readTimeoutMillis=5000
        QuietDownloader.initializeDownloader(downloadConfig)
    }


/*    private var proxy: HttpProxyCacheServer? = null
    fun getProxy(): HttpProxyCacheServer {
        if (proxy == null){
            proxy=HttpProxyCacheServer(_instance)
        }
        return proxy!!
    }*/

}
