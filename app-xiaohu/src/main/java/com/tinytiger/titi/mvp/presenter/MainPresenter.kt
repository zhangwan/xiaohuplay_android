package com.tinytiger.titi.mvp.presenter


import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.bumptech.glide.Glide
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nim.uikit.common.UserUtil
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.NimIntent
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.DataReportEvent
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.AdStopServerDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.im.DemoCache
import com.tinytiger.titi.im.config.preference.Preferences
import com.tinytiger.titi.im.config.preference.UserPreferences
import com.tinytiger.titi.im.session.SessionHelper
import com.tinytiger.titi.mvp.contract.MainContract
import com.tinytiger.titi.mvp.model.MainModel
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.game.wiki.GameWikiDetailActivity
import com.tinytiger.titi.ui.main.MainActivity
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.mine.other.ReportActivity
import com.tinytiger.titi.ui.msg.CommentActivity
import com.tinytiger.titi.ui.msg.LikeActivity
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import com.tinytiger.titi.ui.web.WebActivity
import com.tinytiger.titi.utils.BannerStartUtils
import com.tinytiger.titi.utils.ShareUtils
import com.tinytiger.titi.widget.dialog.AvdDialog
import com.umeng.socialize.bean.SHARE_MEDIA
import java.util.*

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val mModel: MainModel by lazy {
        MainModel()
    }
    var activity: MainActivity? = null
    var context: Context? = null
    var ivLine:ImageView?=null
    override fun startApp() {
        val disposable = mModel.startApp().subscribe({ issue ->
            mRootView?.apply {
                if (issue.code == 200) {
                    if (issue.data.open_page != null) {
                        //启屏页广告
                        SpUtils.saveSP(R.string.open_page,
                            JSON.toJSON(issue.data.open_page).toString())
                        Glide.with(ivLine!!.context).load(issue.data.open_page[0].image).into(ivLine!!)
                    }

                    if (issue.data.popup != null) {
                        //弹框广告
                        SpUtils.saveSP(R.string.open_popup,
                            JSON.toJSON(issue.data.popup).toString())
                    }

                    if (issue.data.update_version != null && issue.data.update_version.update != 0) {
                        //更新apk
                        getConfig(issue.data.update_version)
                        SpUtils.saveSP(R.string.update_version,
                            JSON.toJSON(issue.data.update_version).toString())
                    } else {
                        //无更新弹框,显示广告
                        startAvdDialog()
                    }

                    if (issue.data.config != null) {
                        SpUtils.saveSP(R.string.tool_config, issue.data.config.is_show)
                        SpUtils.saveSP(R.string.tool_config_url, issue.data.config.url)
                    } else {
                        SpUtils.saveSP(R.string.tool_config, 0)
                    }

                }
            }
        }, {})
        addSubscription(disposable)
    }


    /**
     * 数据上报
     */
    fun changeViewLength(event: DataReportEvent) {
        val disposable =
            mModel.changeViewLength(event.view_log_id, event.view_time, event.view_video_time)
                .subscribe()
        addSubscription(disposable)
    }

    fun clickAdRecord(ad_id: String, position_id: Int) {
        val disposable = mModel.clickAdRecord(ad_id, position_id).subscribe()
        addSubscription(disposable)
    }

    /**
     * im通知调用
     */
    fun parseNotifyIntentIMMessage(context: Context, messages: ArrayList<IMMessage>) {
        NIMClient.getService(MsgService::class.java)
            .clearUnreadCount(messages[0].sessionId, SessionTypeEnum.P2P)
        if (messages[0].sessionId.contains("titixhdj")) {
            //系统账号
            when (messages[0].sessionId) {
                context.getString(R.string.im_like) -> {
                    LikeActivity().actionStart(context)
                }
                context.getString(R.string.im_comment) -> {
                    CommentActivity().actionStart(context)
                }
                context.getString(R.string.im_secretary) -> {
                    //小秘书
                    SessionHelper.startP2PSession(context, messages[0].sessionId)
                }
                else -> {
                    NIMClient.getService(MsgService::class.java)
                        .clearUnreadCount(messages[0].sessionId, SessionTypeEnum.P2P)
                }
            }
        } else {
            SessionHelper.startP2PSession(context, messages[0].sessionId)
        }
    }

    private var sysTime = 0.toLong()
    /**
     * 跳转activity
     */
    fun startClass(context: Context, event: ClassEvent) {
        if (System.currentTimeMillis() - sysTime < 500) {
            return
        }

        sysTime = System.currentTimeMillis()
        when (event.mclass) {
            "startP2PSession" -> {
                if (event.element1.equals(Preferences.getUserAccount())) {
                    ToastUtils.show(context, "聊天不能选择自己")
                    return
                }
                SessionHelper.startP2PSession(context, event.element1)
            }
            "ReportActivity" -> {
                ReportActivity.actionStart(context, event.type, event.element1, event.user_id)
            }
            "HomepageActivity" -> {
                HomepageActivity.actionStart(context, event.element1)
            }
            "VideoDetailActivity" -> {
                VideoDetailActivity.actionStart(context, event.element1, "")
            }
            "GameWikiDetailActivity" -> {
                GameWikiDetailActivity.actionStart(context, event.element1)
            }
            "NewsDetailActivity" -> {
                NewsDetailActivity.actionStart(context, event.element1)
            }
            "StopActivity" -> {
                AdStopServerDialog.getInstance()
                    .builder(AppManager.getAppManager().currentActivity(), event.element1).show()
            }
            "WebActivity" -> {
                if (event.type == 1) {
                    try {
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(event.element1)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    WebActivity.actionStart(context, event.element1)
                }
            }
        }
    }

    /**
     * 登陆云信
     */
    fun loginyx() {
        val neteaseid = Preferences.getUserAccount()
        if (TextUtils.isEmpty(neteaseid)) {
            return
        }
        val neteasetoken = Preferences.getUserToken()

        // 在这里直接使用同步到云信服务器的帐号和token登录。
        NimUIKit.login(LoginInfo(neteaseid, neteasetoken), object : RequestCallback<LoginInfo> {
            override fun onSuccess(param: LoginInfo) {
                DemoCache.setAccount(neteaseid)
                Preferences.saveUserAccount(neteaseid)
                Preferences.saveUserToken(neteasetoken)
                initNotificationConfig()

                if (context != null) {
                    UserUtil.setMessageNotify(context!!.getString(R.string.no_molecule1), false)
                    UserUtil.setMessageNotify(context!!.getString(R.string.no_molecule2), false)
                }
            }

            override fun onFailed(code: Int) {

            }

            override fun onException(exception: Throwable) {
            }
        })
    }

    /**
     * 云信初始化消息提醒
     */
    private fun initNotificationConfig() {
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle())
        var statusBarNotificationConfig = UserPreferences.getStatusConfig()
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig()
            UserPreferences.setStatusConfig(statusBarNotificationConfig)
        }
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig)
    }

    /**
     * 分享图片
     */
    fun ShareImageEvent(context: Activity, event: ShareEvent) {
        when (event.type) {
            "WEIXIN_CIRCLE" -> {
                ShareUtils.shareImage(event.shareBitmapImage, SHARE_MEDIA.WEIXIN_CIRCLE)
                // TCAgent.onEvent(context, "分享-微信朋友圈")
            }
            "QQ" -> {
                ShareUtils.shareImage(event.shareBitmapImage, SHARE_MEDIA.QQ)
                // TCAgent.onEvent(context, "分享-QQ")
            }
            "QQZONE" -> {
                ShareUtils.shareImage(event.shareBitmapImage, SHARE_MEDIA.QZONE)
                // TCAgent.onEvent(context, "分享-QQ空间")
            }

            else -> {
                ShareUtils.shareImage(event.shareBitmapImage, SHARE_MEDIA.WEIXIN)
                // TCAgent.onEvent(context, "分享-微信")
            }
        }

    }


    /**
     * 第三方分享及分享回调处理
     */
    fun ShareEvent(context: Activity, event: ShareEvent) {
        when (event.type) {
            "WEIXIN_CIRCLE" -> {
                ShareUtils.shareWeb(event.class_name, event.share_url, event.share_title,
                    event.share_desc, event.share_image, R.mipmap.ic_launcher,
                    SHARE_MEDIA.WEIXIN_CIRCLE)
            }
            "QQ" -> {
                ShareUtils.shareWeb(event.class_name, event.share_url, event.share_title,
                    event.share_desc, event.share_image, R.mipmap.ic_launcher, SHARE_MEDIA.QQ)
            }
            "QQZONE" -> {
                ShareUtils.shareWeb(event.class_name, event.share_url, event.share_title,
                    event.share_desc, event.share_image, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE)
            }
            "Copy" -> {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText(null, event.share_url))

                ToastUtils.show(context, "成功复制链接")
            }
            "Callback" -> {
                event.contentId = ""
            }
            else -> {
                ShareUtils.shareWeb(event.class_name, event.share_url, event.share_title,
                    event.share_desc, event.share_image, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN)
            }
        }
        if (event.contentId.isEmpty()) {
            return
        }
        addSubscription(mModel.addShare(event.class_name, event.contentId, event.type).subscribe())
    }

    /**
     * web页唤起app跳转打开页
     */
    fun startIntent(intent: Intent, context: Activity) {
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            //消息跳转
            val messages =
                intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT) as ArrayList<IMMessage>
            if (messages.size > 0) {
                parseNotifyIntentIMMessage(context, messages)
            }
        } else {
            // "tt://titi:8888/from?startType=4&id=游戏id"
            val startType = intent.data?.getQueryParameter("startType")
            val id = intent.data?.getQueryParameter("id")

            //类型1为图文 2为视频 3个人主页
            if (startType != null && id != null) {
                when (startType) {
                    "1" -> {
                        //新闻详情
                        NewsDetailActivity.actionStart(context, id)
                    }
                    "2" -> {
                        //视频详情
                        VideoDetailActivity.actionStart(context, id, "")
                    }
                    "3" -> {
                        //用户主页
                        HomepageActivity.actionStart(context, id)
                    }
                    "4" -> {
                        //游戏详情
                        GameDetailActivity.actionStart(context, id, 0)
                    }
                    "5" -> {
                        //点评详情
                        val key = intent.data?.getQueryParameter("key")
                        GameReviewsActivity.actionStart(context, id, key!!)
                    }
                    "6" -> {
                        //百科详情
                        GameWikiDetailActivity.actionStart(context, id)
                    }
                    "7" -> {
                        //圈子详情
                        CirclesDetailsActivity.actionStart(context, id, "", "")
                    }
                    "8" -> {
                        //帖子详情
                        PostActivity.actionStart(context, id)
                    }
                    "9" -> {
                        //圈子百科
                        val key = intent.data?.getQueryParameter("key")
                        if (!key.isNullOrEmpty()){
                            CirclesDetailsActivity.actionStart(context, key,"",
                                ConstantsUtils.Page.PAGE_WIKI)
                        }else{
                            CirclesDetailsActivity.actionStart(context, id,
                                ConstantsUtils.Page.PAGE_WIKI)
                        }
                    }
                    "10" -> {
                        //圈子资讯
                        val key = intent.data?.getQueryParameter("key")
                        if (!key.isNullOrEmpty()){
                            CirclesDetailsActivity.actionStart(context, key,"",
                                ConstantsUtils.Page.PAGE_WIKI)
                        }else{
                            CirclesDetailsActivity.actionStart(context, id,
                                ConstantsUtils.Page.PAGE_WIKI)
                        }
                    }
                    "11" -> {
                        val key = intent.data?.getQueryParameter("key")
                        GameCategoryDetailActivity.actionStart(context, key!!, id.toInt())
                    }
                    "12" -> {
                      WebActivity.actionStart(context,id)
                    }
                }
            }
        }
    }

    /**
     * 开屏广告获取
     */
    fun startADV(rlContent: RelativeLayout, activity: MainActivity) {

        val open_page = SpUtils.getString(R.string.open_page, "")
        if (open_page.length > 2) {
            //所有已使用的广告id
            val open_page_id = SpUtils.getString(R.string.open_page_id, "[]")

            val open_page_list = ArrayList<String>()
            if (open_page_id.length > 2) {
                JSON.parseArray(open_page_id).mapTo(open_page_list) { it.toString() }
            }

            val open_page = ArrayList<AdBean>(JSONArray.parseArray(open_page, AdBean::class.java))
            val time = System.currentTimeMillis()
            val list = ArrayList<AdBean>()
            //获取时间段内未使用的的广告
            for (i in open_page) {
                if (time in TimeZoneUtil.getStringToDate(i.start_time) until TimeZoneUtil.getStringToDate(i.end_time)) {
                    var t = false
                    for (j in open_page_list) {
                        if (j == i.id) {
                            t = true
                            continue
                        }
                    }

                    if (!t) {
                        list.add(i)
                    }
                }
            }
            if (list.size < 1) {
                startApp()
                return
            }else{
                addAdv(list[0], rlContent, activity)
                open_page_list.add(list[0].id)
                SpUtils.saveSP(R.string.open_page_id, JSON.toJSON(open_page_list).toString())

                if (list.size>1){
                    Glide.with(activity).load(list[1].image).into(ivLine!!)
                }
            }
        } else {
            startApp()
        }
    }

    /**
     * 开屏广告显示
     */
    private fun addAdv(bean: AdBean, rlContent: RelativeLayout, activity: MainActivity) {
        val advView = LayoutInflater.from(activity).inflate(R.layout.home_item_adv, null)
        advView!!.layoutParams = rlContent.layoutParams
        rlContent.addView(advView)

        val ivAdv = advView.findViewById<ImageView>(R.id.ivAdv)
        GlideUtil.loadImgUrl(ivAdv, bean.image)

        if (bean.extend != null && bean.extend.duration > 0) {
            duration = bean.extend.duration
        }
        val tvClos = advView.findViewById<TextView>(R.id.tvClos)
        tvClos.setOnClickListener { duration = 0 }
        ivAdv.setOnClickListener {
            duration = 0
            BannerStartUtils.setStartIntent(bean, 0)
            clickAdRecord(bean.id, 1)
        }
        setTime(rlContent, advView, tvClos)
    }

    var duration = 3
    private fun setTime(rlContent: RelativeLayout, advView: View, tvClos: TextView) {
        if (duration <= 0) {
            rlContent.removeView(advView)
            startApp()
            return
        }
        tvClos.text = "$duration 跳过"
        tvClos.postDelayed({
            duration -= 1
            setTime(rlContent, advView, tvClos)
        }, 1000)
    }


    /**
     * 弹框广告
     */
    fun startAvdDialog() {
        AvdDialog.create(activity!!.supportFragmentManager)
            .setAvdListener(object : AvdDialog.OnAvdListener {
                override fun onDismiss(id: String) {
                    clickAdRecord(id, 3)
                }
            }).startADV()
    }


}