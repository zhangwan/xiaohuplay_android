package com.tinytiger.titi.ui

import android.content.Context
import android.provider.Settings
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.utils.ChannelUtil
import com.tinytiger.common.utils.preference.SpUtils
import android.content.pm.PackageManager
import android.os.Build
import android.text.format.DateUtils
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.tinytiger.common.NetConstants_B
import com.tinytiger.common.adapter.CustomStartHolder
import com.tinytiger.titi.ui.main.MainActivity
import com.orhanobut.logger.Logger
import com.tinytiger.titi.ui.web.WebDialog
import com.tinytiger.titi.utils.image.MediaLoader
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc app启动页
 */
class SplashActivity : BaseActivity() {


    override fun layoutId(): Int = R.layout.activity_splash

    override fun initData() {
        setWindowFeature()
    }

    override fun initView() {
        //删除饺子播放器进度
        getSharedPreferences("JZVD_PROGRESS", Context.MODE_PRIVATE).edit().clear().apply()

        if (SpUtils.getString(R.string.android_id, "").isEmpty()) {
            SpUtils.saveSP(R.string.android_id,
                Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
        }

        ChannelUtil.getChannel(BaseApp.getContext())
        ChannelUtil.getChannelApk(BaseApp.getContext())

        SpUtils.saveSP(R.string.version, packageManager.getPackageInfo(packageName,
            PackageManager.GET_CONFIGURATIONS).versionName)

        if (!NetConstants_B.LOG_TYPE) {
            Logger.clearLogAdapters()
        } else {
            SpUtils.saveSP(R.string.channel, "1")
        }
        SpUtils.saveSP(R.string.network_download, false)
        Album.initialize(AlbumConfig.newBuilder(this).setAlbumLoader(MediaLoader()).setLocale(
            Locale.getDefault()).build())

        //app启动时间,非同一天
        if (!DateUtils.isToday(SpUtils.getLong(R.string.start_time, 0))) {
            //清空广告显示id
            SpUtils.saveSP(R.string.open_page_id, "")
            //清空游戏时长
            SpUtils.saveSP(R.string.game_yun_time, 0)
        }

        if (SpUtils.getBoolean(R.string.start_app, false)) {
            MainActivity().actionStart(this, 0)
            finish()
        } else {
            startBanner()

            tvStart.setOnClickListener {
                flProgress.visibility = View.VISIBLE
                MainActivity().actionStart(this, 0)
                finish()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvStart.postDelayed({
                    WebDialog.create(supportFragmentManager).show()
                        .setDismissListener(object : WebDialog.ViewListener {
                            override fun click() {
                                finish()
                            }
                        })
                }, 1000)
            }
        }

    }


    override fun start() {

    }

    override fun onDestroy() {
        super.onDestroy()
        SpUtils.saveSP(R.string.start_time, System.currentTimeMillis())
    }

    private var mPosition = 0
    private fun startBanner() {
        val mAdBean = ArrayList<Int>()
        mAdBean.add(R.mipmap.icon_start1)
        mAdBean.add(R.mipmap.icon_start2)
        mAdBean.add(R.mipmap.icon_start3)
        mAdBean.add(R.mipmap.icon_start4)
        banner.setAutoPlay(true).setLoop(false).setPages(mAdBean, CustomStartHolder()).start()

        var sizePosition = 0
        banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {
                if (position == 3) {
                    sizePosition += 1
                    if (sizePosition > 4) {
                        flProgress.visibility = View.VISIBLE
                        MainActivity().actionStart(this@SplashActivity, 0)
                        finish()
                    }
                } else {
                    sizePosition = 0
                }
            }

            override fun onPageSelected(position: Int) {
                mPosition = position
                if (position == 3) {
                    tvStart.visibility = View.VISIBLE
                } else {
                    tvStart.visibility = View.GONE
                }
            }
        })
    }


}
