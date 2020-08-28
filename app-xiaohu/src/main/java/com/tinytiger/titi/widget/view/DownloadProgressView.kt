package com.tinytiger.titi.widget.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.orhanobut.logger.Logger
import com.steven.fileprovider.FileProvider8
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.sql.SQLiteDB
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.utils.MarketUtil
import com.tinytiger.titi.widget.dialog.DownloadDialog
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import java.io.File


/**
 * @author zhw_luke
 * @date 2020/4/14 0014 上午 10:02
 * @Copyright 小虎互联科技
 * @doc 文件下载控件 由于第三方数据库无适配字段,需要增加自己的下载数据存储SQLiteDB
 * @since 2.1.0
 */
class DownloadProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var progressBar: ProgressBar? = null
    private var tvDownload: TextView? = null
    var rlContent: RelativeLayout? = null

    /**
     * 下载状态
     */
    var status = DownloadEntry.Status.IDLE

    /**
     * 是否显示下载大小
     * 0，通用页
     * 1.游戏详情页显示
     * 2 首页非特殊頁
     */
    private var showPackageSize = 0

    init {
        init(context)
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_progress_dowload, this)
        progressBar = view.findViewById(R.id.progressBar)
        tvDownload = view.findViewById(R.id.tvDownload)
        rlContent = view.findViewById(R.id.rlContent)

        tvDownload?.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }

            when (status) {
                DownloadEntry.Status.IDLE -> {
                    startdownload()
                    var download_location = 2

                    if (showPackageSize == 1) {
                        download_location = 1
                    }
                    GameAgentUtils.setGameDetailDownload(
                        apkItem!!.id, download_location,
                        apkItem!!.package_name_android, 0
                    )
                }
                DownloadEntry.Status.WAITING -> {
                    pauseDownload()
                }
                DownloadEntry.Status.CONNECTING -> {
                    pauseDownload()
                }
                DownloadEntry.Status.CONNECT_SUCCESSFUL -> {
                    pauseDownload()
                }
                DownloadEntry.Status.DOWNLOADING -> {
                    pauseDownload()
                }
                DownloadEntry.Status.PAUSED -> {
                    startdownload()
                }
                DownloadEntry.Status.CANCELLED -> {
                    startdownload()
                }
                DownloadEntry.Status.COMPLETED -> {
                    Logger.d(apkInfo)
                    when (apkInfo) {
                        1 -> {
                            MarketUtil.openPackage(context, apkItem?.package_name_android)
                        }
                        2 -> {
                            startdownload()
                            var download_location = 2
                            if (showPackageSize == 1) {
                                download_location = 1
                            }
                            GameAgentUtils.setGameDetailDownload(
                                apkItem!!.id, download_location,
                                apkItem!!.package_name_android, 1
                            )
                        }
                        3 -> {
                            FastClickUtil.lastClickTime = 0
                            GameDetailActivity.actionStart(context, "" + apkItem?.id, 0)
                        }
                        else -> {
                            if (downloadEntry != null) {
                                installApk(File(downloadEntry!!.filePath))
                            }
                        }
                    }
                }
                DownloadEntry.Status.ERROR -> {
                    startdownload()
                }
            }
        }
    }

    /**
     * 设置下载框ui
     */
    fun setPackageUi(int: Int) {
        showPackageSize = int
        if (showPackageSize == 1) {
            progressBar?.progressDrawable =
                ContextCompat.getDrawable(context, R.drawable.progress_download_5)
            tvDownload?.gravity = Gravity.CENTER
        } else if (showPackageSize == 2) {
            tvDownload?.gravity = (Gravity.CENTER or Gravity.RIGHT)
        } else {
            tvDownload?.gravity = Gravity.CENTER
        }
    }

    /**
     * 设置下载字体大小（单位dp）
     */
    fun setTextSize(size: Int) {
        this.tvDownload!!.textSize = size.toFloat()
    }

    /**
     * 设置下载按钮高度（单位dp）
     */
    fun setViewHeight(height: Int) {
        this.tvDownload!!.height = Dp2PxUtils.dip2px(context, height)

        val lp = progressBar!!.layoutParams
        lp.height = Dp2PxUtils.dip2px(context, height)
        this.progressBar!!.layoutParams = lp

        val lp2 = rlContent!!.layoutParams
        lp2.height = Dp2PxUtils.dip2px(context, height)
        this.rlContent!!.layoutParams = lp2
    }

    /**
     * 开始下载
     */
    private fun startdownload() {
        if (downloadEntry == null) {
            var id=apkItem!!.package_name_android+apkItem!!.package_size_android
            id=id.replace(".","")+".apk"
            downloadEntry = DownloadEntry(apkItem!!.download_url, id)
        }

        if (downloadEntry!!.status == DownloadEntry.Status.IDLE || downloadEntry!!.status == DownloadEntry.Status.CANCELLED || downloadEntry!!.status == DownloadEntry.Status.PAUSED || downloadEntry!!.status == DownloadEntry.Status.ERROR) {
            // 在查询异常
            val entry = QuietDownloader.queryById(downloadEntry!!.id)
            if (entry != null) {
                QuietDownloader.resume(entry)
            } else {
                QuietDownloader.download(downloadEntry)
            }
            //wifi网络判断
            if (!MyNetworkUtil.currentNetWorkStatusIsWifi(context)) {
                tvDownload?.postDelayed({
                    QuietDownloader.pauseAll()
                    DownloadDialog.getInstance()
                        .builder(AppManager.getAppManager().currentActivity()).show()
                }, 500)
            }
            progressBar?.progress = 0
            tvDownload?.background = null
            tvDownload?.setTextColor(ContextCompat.getColor(context, R.color.gray33))
        } else if (downloadEntry!!.status == DownloadEntry.Status.COMPLETED) {
            installApk(File(downloadEntry!!.filePath))
        } else {

        }
    }

    /**
     * 暂停下载
     */
    private fun pauseDownload() {
        if (downloadEntry != null) {
            try {
                QuietDownloader.pause(downloadEntry)
            } catch (e: NullPointerException) {
                Logger.d(e.toString())
            }
        }
    }


    /**
     * 下载数据信息
     */
    var downloadEntry: DownloadEntry? = null

    /**
     * 元数据信息
     */
    var apkItem: MineGameBean? = null
    var channel = false

    /**
     * 设置数据源
     */
    fun setDownloadUrl(item: MineGameBean) {
        apkItem = item
        channel = SpUtils.getBoolean(R.string.download_apk, false)
        //初始化下载按钮，防止窜图
        apkInfo = 0
        status = DownloadEntry.Status.IDLE
        progressBar?.progress = 0
        tvDownload?.background = null
        tvDownload?.setTextColor(ContextCompat.getColor(context, R.color.gray33))
        visibility = View.VISIBLE
        //是否存在包信息
        if (item.package_name_android.isNullOrEmpty()) {
            if (showPackageSize == 1) {
                visibility = View.GONE
            } else {
                status = DownloadEntry.Status.COMPLETED
                tvDownload?.text = "进入"
                apkInfo = 3
                progressBar?.progress = 100
            }
            return
        }
        if (item.download_url.isNullOrEmpty()) {
            status = DownloadEntry.Status.COMPLETED
            tvDownload?.text = "进入"
            apkInfo = 3
            progressBar?.progress = 100
            return
        }

        //判断渠道
        if (!channel) {
            //非官方渠道
            if (showPackageSize == 1) {
                //游戏详情页面,隐藏打开按钮
                visibility = View.GONE
            } else {
                apkInfo = 3
                status = DownloadEntry.Status.COMPLETED
                tvDownload?.text = "进入"
                progressBar?.progress = 100
            }
        } else {
            SQLiteDB.getInstance(context).addApk(apkItem)

            var id=item.package_name_android+item.package_size_android
            id=id.replace(".","")+".apk"
            //包名为
            val entry = QuietDownloader.queryById(id)
            //存在下载历史
            if (entry != null) {
                downloadEntry = entry
                status = entry.status
            } else {
                downloadEntry = DownloadEntry(item.download_url, id)
            }

            //是否安装
            val apk = MarketUtil.getAppInstalled(context, item.package_name_android)
            if (apk != null) {
                setResolveInfo(apk)
            } else {
                setDownloadUI()
            }
        }
    }


    /**
     * 更新下载数据
     */
    fun setDownloadData(data: DownloadEntry) {
        if (downloadEntry != null && downloadEntry?.id != data.id) {
            return
        }
        downloadEntry = data
        status = data.status
        setDownloadUI()
        apkInfo = 0
    }

    /**
     * 更新下载状态
     */
    private fun setDownloadUI() {
        when (status) {
            DownloadEntry.Status.IDLE -> {
                if (showPackageSize == 1) {
                    tvDownload?.text =
                        "下载 (${StringUtils.formatMemorySize(apkItem?.package_size_android)})"
                } else {
                    tvDownload?.text = "下载"
                }
                progressBar?.progress = 0
            }
            DownloadEntry.Status.WAITING -> {
                tvDownload?.text = "等待中"
                progressBar?.progress = 0
            }
            DownloadEntry.Status.CONNECTING -> {
                tvDownload?.text = "连接"
            }
            DownloadEntry.Status.CONNECT_SUCCESSFUL -> {
                tvDownload?.text = "连接"
            }
            DownloadEntry.Status.DOWNLOADING -> {
                val length: Float =
                    downloadEntry!!.currentLength * 1.0f / downloadEntry!!.totalLength
                progressBar?.progress = (length * 100).toInt()
                if (showPackageSize == 1) {
                    tvDownload?.text = "暂停 (${(length * 100).toInt()}%)"
                } else {
                    tvDownload?.text = "暂停"
                }
            }
            DownloadEntry.Status.PAUSED -> {
                val length: Float =
                    downloadEntry!!.currentLength * 1.0f / downloadEntry!!.totalLength
                progressBar?.progress = (length * 100).toInt()
                if (showPackageSize == 1) {
                    tvDownload?.text = "继续 (${(length * 100).toInt()}%)"
                } else {
                    tvDownload?.text = "继续"
                }
            }
            DownloadEntry.Status.CANCELLED -> {
                if (showPackageSize == 1) {
                    tvDownload?.text =
                        "下载 ${StringUtils.formatMemorySize(apkItem?.package_size_android)}"
                } else {
                    tvDownload?.text = "下载"
                }
            }
            DownloadEntry.Status.COMPLETED -> {
                tvDownload?.text = "安装"
                if (showPackageSize == 1) {
                    tvDownload?.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.solid_gradient_5_36e0a4_19be6b
                    )
                } else if (showPackageSize == 2) {
                    tvDownload?.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.solid_gradient_15_36e0a4_19be6b
                    )
                } else {
                    tvDownload?.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.solid_gradient_15_36e0a4_19be6b
                    )
                }

                tvDownload?.setTextColor(ContextCompat.getColor(context, R.color.white))
                if (apkItem != null) {
                    GameAgentUtils.setGameDetailDownloadOver(
                        apkItem!!.id,
                        apkItem!!.package_name_android
                    )
                }
            }

            DownloadEntry.Status.ERROR -> {
                //数据包损坏
                tvDownload?.text = "继续"
                if (FastClickUtil.isFastClick()) {
                    return
                }
                var download_fail = 2
                if (showPackageSize == 1) {
                    download_fail = 1
                }
                if (apkItem != null) {
                    GameAgentUtils.setGameDetailDownloadError(apkItem!!.id, download_fail)
                }
            }
        }
    }

    /**
     * 安装
     * 适配多个版本
     */
    private fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        FileProvider8.setIntentDataAndType(
            context, intent,
            "application/vnd.android.package-archive", file, true
        )
        AppManager.getAppManager().currentActivity().startActivity(intent);
    }

    /**
     * 是否安装
     * 0 无
     * 1 打开
     * 2 更新
     * 3 查看详情
     */
    var apkInfo = 0
    private var mResolveInfo: PackageInfo? = null

    /**
     * 设置apk包信息
     */
    private fun setResolveInfo(item: PackageInfo) {
        mResolveInfo = item
        status = DownloadEntry.Status.COMPLETED

        if (item.versionName == apkItem!!.version) {
            //版本相同,打开游戏
            apkInfo = 1
            tvDownload?.text = "打开"
            progressBar?.progress = 100
        } else {
            if (channel) {
                //官方渠道,更新版本
                visibility = View.VISIBLE
                apkInfo = 2
                if (showPackageSize == 1) {
                    tvDownload?.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.solid_gradient_5_36e0a4_19be6b
                    )
                } else if (showPackageSize == 2) {
                    tvDownload?.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.solid_gradient_15_36e0a4_19be6b
                    )
                } else {
                    tvDownload?.background = ContextCompat.getDrawable(
                        context,
                        R.drawable.solid_gradient_15_36e0a4_19be6b
                    )
                }
                tvDownload?.setTextColor(ContextCompat.getColor(context, R.color.white))
                tvDownload?.text = "更新"
            } else {
                //非官方,打开游戏
                apkInfo = 3
                tvDownload?.text = "进入"
                progressBar?.progress = 100
            }
        }
    }

    /**
     * 更新安装信息
     * 页面重新显示更新安装信息
     */
    fun newinstallApk() {
        if (status != DownloadEntry.Status.COMPLETED) {
            return
        }
        if (context == null) {
            return
        }
        if (apkItem == null) {
            return
        }
        val apk = MarketUtil.getAppInstalled(context, apkItem!!.package_name_android)
        if (apk != null) {
            setResolveInfo(apk)
        }
    }

}