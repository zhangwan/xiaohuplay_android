package com.tinytiger.titi.ui.video

import android.content.Context
import android.content.Intent
import android.view.View

import cn.jzvd.Jzvd
import com.tinytiger.common.basis.BasicNullPresenterActivity
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.net.NetStateChangeObserver
import com.tinytiger.titi.utils.net.NetStateChangeReceiver
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.widget.video.JZMediaExo
import kotlinx.android.synthetic.main.video_activity_info2.*


/**
 *
 * @author zhw_luke
 * @date 2020/3/9 0009 下午 5:38
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 视频查看页
 */
class VideoActivity : BasicNullPresenterActivity(), NetStateChangeObserver {

    private var videoUrl = ""
    /**
     * 0竖屏1横屏
     */
    private var screen = 0
    private var share: ShareEvent? = null


    companion object {
        fun actionStart(context: Context, url: String) {
            actionStart(context, url, 0, null)
        }

        fun actionStart(context: Context, url: String, screen: Int) {
            actionStart(context, url, screen, null)
        }

        fun actionStart(context: Context, url: String, screen: Int, share: ShareEvent?) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra("content_url", url)
            intent.putExtra("screen", screen)
            if (share != null) {
                intent.putExtra("share", share)
            }
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.video_activity_info2

    override fun initData() {
        videoUrl = intent.getStringExtra("content_url")
        screen = intent.getIntExtra("screen", 0)
        if (intent.hasExtra("share")) {
            share = intent.getParcelableExtra<ShareEvent>("share")
        }
    }

    override fun initView() {
        NetStateChangeReceiver.registerReceiver(this)
        NetStateChangeReceiver.registerObserver(this)

        // videoUrl=TitiApplication().getProxy().getProxyUrl(videoUrl)
        //   Logger.d(videoUrl)
        //   Logger.d(TitiApplication().getProxy().getProxyUrl(videoUrl))
        videoView.setUp(videoUrl, "", Jzvd.SCREEN_NORMAL, JZMediaExo::class.java)

        videoView.setShareType(share != null).setOnClickListener {
            setShare()
        }
        videoView.setShareMore(share != null).setOnClickListener {
            setShare()
        }
        //先播放在转换方向,否则会一直处于加载中的状态
        videoView.startVideo()
        if (screen == 1) {
            videoView.gotoFullscreen()
        }

        //   Logger.d(JZUtils.getSavedProgress(this,videoUrl))
    }

    override fun onResume() {
        super.onResume()
        Jzvd.goOnPlayOnResume()
        videoView.over_ll.visibility= View.GONE

        //  Jzvd.goOnPlayOnResume()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.goOnPlayOnPause()
        //Logger.d(JZUtils.getSavedProgress(this,videoUrl))

    }

    override fun onDestroy() {
        super.onDestroy()
        NetStateChangeReceiver.unRegisterObserver(this)
        NetStateChangeReceiver.unRegisterReceiver(this)
    }
    private fun setShare() {
        if (share == null) {
            return
        }
        ShareDialog.create(supportFragmentManager).apply {
            class_name = "no"
            share_url = share!!.share_url
            share_title = share!!.share_title
            share_desc = share!!.share_desc
            share_image = share!!.share_image
        }.show()
    }

    override fun onNetDisconnected() {

    }

    override fun onNetConnected(networkType: NetworkType?) {
        var type = 2
        if (networkType == NetworkType.NETWORK_NO) {
            type = 0
        } else if (networkType == NetworkType.NETWORK_WIFI) {
            type = 1
        }
        videoView.setType(type)

    }
}