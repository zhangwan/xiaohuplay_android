package com.tinytiger.titi.ui.main


import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.netease.nim.uikit.support.permission.MPermission
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.orhanobut.logger.Logger
import com.steven.fileprovider.FileProvider8

import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.*
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.yungaem.InitVersionBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.web.TbsInit
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.MainContract
import com.tinytiger.titi.mvp.presenter.MainPresenter
import com.tinytiger.titi.ui.login.LoginActivity
import com.tinytiger.titi.utils.ShareUtils
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.widget.dialog.DownloadDialog
import com.tinytiger.titi.widget.dialog.VersionUpdataDialog
import com.tinytiger.titi.widget.view.MainButtomView
import com.umeng.commonsdk.statistics.common.DeviceConfig
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import com.xwdz.download.notify.DataUpdatedWatcher
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


/**
 *
 * @author zhw_luke
 * @date 2019/10/25 0025 下午 1:47
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 主页
 */
class MainActivity : BaseActivity(), MainContract.View, NetStateChangeObserver {

    fun actionStart(context: Context, currTabIndex: Int) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("currTabIndex", currTabIndex)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.activity_main

    private val mPresenter by lazy { MainPresenter() }

    /**
     * 当前打开页面
     */
    private var currTabIndex = -1

    private var mFragmentHome: HomeFragment? = null
    private var mFragmentCircle: CircleFragment? = null
    private var mFragmentMe: MineFragment? = null
    private var mFragmentTool: ToolFragment? = null

    init {
        mPresenter.attachView(this)
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        outState.putInt("currTabIndex", currTabIndex)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            mPresenter.startIntent(intent, this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            currTabIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
    }

    override fun initData() {
        setWindowFeature()
        mPresenter.activity = this
        mPresenter.context = this
    }

    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        //设置音量键
        SpUtils.saveSP("volume", 0)
        mPresenter.startIntent(intent, this)

        TbsInit.initX5()
        ShareUtils.getIm()
        requestBasicPermission()
        NetStateChangeReceiver.registerObserver(this)
        registerObservers(true)

        mPresenter.ivLine = ivLine
        mPresenter.startADV(rlContent, this)

        mbvView.setPixels()
        mbvView.mListener = object : MainButtomView.OnMainButtomViewListener {
            override fun onPostionView(title: String) {
                switchFragment(title)
            }

            override fun onClickDouble(title: String) {
                when (title) {
                    "home" -> {
                        mFragmentHome?.setDoubleRefresh()
                    }
                    "circle" -> {
                        mFragmentCircle?.start()
                    }
                }
            }

            override fun onClickTop(title: String) {
                when (title) {
                    "home" -> {
                        mFragmentHome?.topScorll()
                    }
                    "circle" -> {
                        mFragmentCircle?.topScorll()
                    }
                }
            }
        }
        mbvView.switchFragment("home")


        val a = DeviceConfig.getDeviceIdForGeneral(this)
        val b = DeviceConfig.getMac(this)
        Log.d("titi-log", "{\"device_id\":\"${a}\",\"mac\":\"${b}\"}")
       // val request = DownloadManager.Request(Uri.parse(""));
    }

    /**
     * 显示返回顶部按钮
     */
    fun setShowTop(boolean: Int) {
        mbvView.setTopShow(boolean)
    }

    override fun start() {

    }

    private var time: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - time > 2000) {
            showToast("再按一次退出")
            time = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        QuietDownloader.addObserver(watcher)
        mbvView?.setUnreadNum(readNumMap[SpUtils.getString(R.string.user_id, "")] ?: 0)
        QuietDownloader.recoverAll()
    }


    override fun onDestroy() {
        registerObservers(false)
        super.onDestroy()
        mPresenter.detachView()
        try {
            NetStateChangeReceiver.unRegisterReceiver(this)
            QuietDownloader.removeObserver(watcher)
        } catch (e: Exception) {

        }
    }

    private fun switchFragment(title: String) {
        val transaction = supportFragmentManager.beginTransaction()
        mFragmentHome?.let { transaction.hide(it) }
        mFragmentCircle?.let { transaction.hide(it) }
        mFragmentMe?.let { transaction.hide(it) }
        mFragmentTool?.let { transaction.hide(it) }
        when (title) {
            "circle" -> {
                mFragmentCircle?.let {
                    transaction.show(it)
                } ?: CircleFragment.getInstance().let {
                    mFragmentCircle = it
                    transaction.add(R.id.fl_container, it, "mFragment2")
                }
            }
            "mine" -> {
                mFragmentMe?.let {
                    transaction.show(it)
                } ?: MineFragment.getInstance().let {
                    mFragmentMe = it
                    transaction.add(R.id.fl_container, it, "mFragment3")
                }
            }
            "home" -> {
                mFragmentHome?.let {
                    transaction.show(it)
                } ?: HomeFragment.getInstance().let {
                    mFragmentHome = it
                    transaction.add(R.id.fl_container, it, "mFragment0")
                }
            }
            "tool" -> {
                mFragmentTool?.let {
                    transaction.show(it)
                } ?: ToolFragment.getInstance().let {
                    mFragmentTool = it
                    transaction.add(R.id.fl_container, it, "mFragmentTool")
                }
            }
        }
        transaction.commitAllowingStateLoss()
    }


    override fun getConfig(iteminfo: InitVersionBean) {
        sendRequest(iteminfo)
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClassEvent(event: ClassEvent) {
        if (event.mclass.equals("LoginActivity")) {
            if (event.type > 0) {
                AppManager.getAppManager().finishNotSpecifiedActivity(MainActivity::class.java)
                mbvView.switchFragment("home")
            }
            LoginActivity.actionStart(this, event.type)
        } else {
            mPresenter.startClass(this, event)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainLoginEvent(event: MainLoginEvent) {
        mbvView.switchFragment(event.page)

        if (event.logintype) {
            mPresenter.loginyx()
        } else {
            mbvView.setUnreadNum(0)
            mFragmentMe?.setMsg()
            mFragmentCircle?.setMsg()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onShareEvent(event: ShareEvent) {
        if (event.shareBitmapImage != null) {
            mPresenter.ShareImageEvent(this, event)
        } else {
            mPresenter.ShareEvent(this, event)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataReportEvent(event: DataReportEvent) {
        mPresenter.changeViewLength(event)
    }


    /**
     * 注册/注销最近联系人列表变化观察者
     * 未读消息状态
     */
    private fun registerObservers(register: Boolean) {
        NIMClient.getService(AuthServiceObserver::class.java).observeOnlineStatus(Observer { code ->
            if (code.wontAutoLogin()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity", 2))
            }
        }, register)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeRecentContact(Observer { recentContacts ->
                //新增消息数
                mbvView.setUnreadNum(recentContacts.size)
                var commentSize = 0
                for (i in recentContacts) {
                    if (i.contactId == getString(R.string.im_comment)) {
                        commentSize += 1
                    }
                }

                if (commentSize > 0) {
                    EventBus.getDefault()
                        .post(MsgEvent(commentSize, getString(R.string.im_comment)))
                }
                onMsgEvent(MsgEvent())
            }, register)
        if (register) {
            onMsgEvent(MsgEvent())
        }
    }

    /**
     * im未读数
     */
    var readNumMap = hashMapOf<String, Int>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(event: MsgEvent) {
        var unreadNum = 0
        if (isLogin()) {
            unreadNum = NIMClient.getService(MsgService::class.java).totalUnreadCount
            readNumMap[SpUtils.getString(R.string.user_id, "")] = unreadNum
        } else {
            readNumMap[SpUtils.getString(R.string.user_id, "")] = 0
        }
        unreadNum = readNumMap[SpUtils.getString(R.string.user_id, "")] ?: 0
        mbvView.setUnreadNum(unreadNum)
        mFragmentMe?.setMsg()
        mFragmentCircle?.setMsg()
    }

    private var versionUpdataDialog: VersionUpdataDialog? = null

    private var update_info: InitVersionBean? = null
    private fun sendRequest(info: InitVersionBean) {
        if (info.update == 0) {
            return
        }

        update_info = info
        if (info.update == 2) {
            versionUpdataDialog =
                VersionUpdataDialog.instance!!.builder(AppManager.getAppManager().currentActivity(),
                    info, "", null)
            versionUpdataDialog!!.show()
        } else {
            //存储地址解析有问题,每次需要重新下载
            QuietDownloader.download(DownloadEntry(info.update_url, "hoo_v_${info.version}.apk"))
        }
    }


    private val BASIC_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)

    private fun requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS)
        MPermission.with(this@MainActivity).setRequestCode(100).permissions(*BASIC_PERMISSIONS)
            .request()
    }

    @OnMPermissionGranted(100)
    fun onBasicPermissionSuccess() {
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS)
    }

    @OnMPermissionDenied(100)
    @OnMPermissionNeverAskAgain(100)
    fun onBasicPermissionFailed() {
        showToast("未全部授权，部分功能可能无法正常运行！")
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS)
    }

    override fun onNetDisconnected() {

    }

    override fun onNetConnected(networkType: NetworkType) {
        if (!MyNetworkUtil.currentNetWorkStatusIsWifi(this)) {
            val network_type = SpUtils.getBoolean(R.string.network_download, false)
            if (!network_type) {
                val list = QuietDownloader.queryAll()

                var type = false
                for (i in list) {
                    if (i.status == DownloadEntry.Status.DOWNLOADING) {
                        type = true
                    }
                }

                //暂停所有任务
                QuietDownloader.pauseAll()
                if (type) {
                    DownloadDialog.getInstance()
                        .builder(AppManager.getAppManager().currentActivity()).show()
                }
            }
        }
    }

    private val watcher = object : DataUpdatedWatcher() {
        override fun notifyUpdate(data: DownloadEntry) {
            if (update_info?.update_url == data.id) {
                if (data.status == DownloadEntry.Status.COMPLETED) {
                    if (update_info?.update == 2) {
                        installApk(File(data.filePath))
                    } else {
                        VersionUpdataDialog.instance!!.builder(
                            AppManager.getAppManager().currentActivity(), update_info!!,
                            data.filePath, DialogInterface.OnDismissListener {
                                update_info = null
                                mPresenter.startAvdDialog()
                            }).show()
                    }
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
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTestEvent(event: TestEvent) {
        val act = AppManager.getAppManager().currentActivity() as AppCompatActivity
        TextDialog.create(act.supportFragmentManager).setMessage(event.title).show()
    }

}
