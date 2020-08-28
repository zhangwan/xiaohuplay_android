package com.tinytiger.titi.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.steven.fileprovider.FileProvider8
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.net.data.yungaem.InitVersionBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.R
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import java.io.File

/**
 *
 * @author zhw_luke
 * @date 2020/4/15 0015 下午 4:49
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 下载服务
 */
class VersionUpdataDialog private constructor() {

    private var dialog: Dialog? = null
    private var tvProgress: TextView? = null
    private var progressBar: ProgressBar? = null
    //1-主页 2-设置页面
    var pageType = 1

    fun builder(context: Context, info: InitVersionBean, file: String,
        listener: DialogInterface.OnDismissListener?): VersionUpdataDialog {
        if (isShowing) {
            return this
        }
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_upapp, null)
        val llInfo = view.findViewById<View>(R.id.llInfo)
        val llProgress = view.findViewById<View>(R.id.llProgress)
        val tvVerion = view.findViewById<TextView>(R.id.tvVerion)
        val tvDesc = view.findViewById<TextView>(R.id.tvDesc)
        val cancel = view.findViewById<TextView>(R.id.cancel)

        tvProgress = view.findViewById(R.id.tv_progress)
        progressBar = view.findViewById(R.id.pb)

        tvVerion.text = "v" + info.version
        tvDesc.text = info.update_desc
        if (info.update == 1) {
            cancel.text = "以后再说"
            cancel.setTextColor(ContextCompat.getColor(context, R.color.gray99))
        } else {
            cancel.text = "  退出  "
        }

        view.findViewById<View>(R.id.cancel).setOnClickListener {
            if (info.update == 2) {
                AppManager.getAppManager().finishAllActivity_1()
            } else {
                dismiss()
            }
        }

        view.findViewById<View>(R.id.define).setOnClickListener {
            if (info.update == 2 || pageType == 2) {
                llProgress.visibility = View.VISIBLE
                llInfo.visibility = View.GONE
                QuietDownloader.download(DownloadEntry(info.update_url, "hoo_v${info.version}.apk"))
            } else {
                installApk(File(file))
            }
        }

        dialog = Dialog(context, R.style.DialogStyle)
        dialog!!.setCanceledOnTouchOutside(false)
        if (info.update == 2) {
            cancel.visibility = View.GONE
            dialog!!.setCancelable(false)
        }

        dialog!!.setContentView(view)

        dialog!!.setOnDismissListener(listener)
        return this
    }

    fun show() {
        try {
            if (isShowing) {
                return
            }
            if (dialog != null) {
                dialog!!.show()
            }
        } catch (e: Exception) {
        }
    }


    val isShowing: Boolean
        get() = if (dialog != null) {
            dialog!!.isShowing
        } else {
            false
        }

    fun dismiss() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    companion object {
        var instance: VersionUpdataDialog? = null
            get() {
                if (field == null) {
                    synchronized(VersionUpdataDialog::class.java) {
                        if (field == null) {
                            field = VersionUpdataDialog()
                        }
                    }
                }
                return field
            }
            private set
    }

    fun setDownloadData(downloadEntry: DownloadEntry) {
        val length: Float = downloadEntry.currentLength * 1.0f / downloadEntry.totalLength
        progressBar?.progress = (length * 100).toInt()
        tvProgress?.text = "${(length * 100).toInt()}%"

        if (downloadEntry.status == DownloadEntry.Status.ERROR) {
            ToastUtils.show(BaseApp._instance, "下载失败")
            dismiss()
        }

    }

    private fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW);
        FileProvider8.setIntentDataAndType(BaseApp._instance, intent,
            "application/vnd.android.package-archive", file, true)

        AppManager.getAppManager().currentActivity().startActivity(intent);
    }
}