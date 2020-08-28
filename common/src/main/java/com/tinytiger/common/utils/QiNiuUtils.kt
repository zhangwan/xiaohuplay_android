package com.tinytiger.common.utils

import com.orhanobut.logger.Logger
import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager

import com.tinytiger.common.R
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.LoadingUtils
import io.reactivex.disposables.CompositeDisposable

/**
 * @author zhw_luke
 * @date 2018/8/29 0029 下午 4:25
 * @Copyright 小虎互联科技
 * @doc 七牛图片上传工具类
 * @since 3.1.0
 */
class QiNiuUtils private constructor() {

    interface OnQiniuListener {
        fun onResult(result: String, type: Boolean)
    }

    companion object {
        var appQiniu: QiNiuUtils? = null
            get() {
                if (field == null) {
                    synchronized(QiNiuUtils::class.java) {
                        if (field == null) {
                            field = QiNiuUtils()
                        }
                    }
                }
                return field
            }
            private set
    }

    private var mUploadManager: UploadManager

    init {

        //   val zone: Zone = FixedZone(arrayOf("up-z2.qiniup.com"))
        //自动识别上传区域
        //AutoZone.autoZone

        val configuration =
            Configuration.Builder().chunkSize(256 * 1024)
                .putThreshhold(512 * 1024)
                .connectTimeout(10)
                .zone(FixedZone.zone2)
                .useHttps(false)
                .responseTimeout(10).build()
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        mUploadManager = UploadManager(configuration)
    }

    private var qiniu_token = ""
    private var compositeDisposable = CompositeDisposable()
    fun loadQiniuToken() {
        val disposable = RetrofitManager.service.getQiniuToken()
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ issue ->
                if (issue.code == 200) {
                    qiniu_token = issue.data.qiniu_token
                    startFileUp()
                } else {
                    mListener?.onResult("", false)
                    LoadingUtils.getInstance().dismiss()
                }
            }, { _ ->
                mListener?.onResult("", false)
                LoadingUtils.getInstance().dismiss()
            })
        compositeDisposable.add(disposable)
    }

    /**
     * @param token 七牛tocken
     * @param key   文件标记,指定七牛服务上的文件名
     * @param data  File对象、或 文件路径、或 字节数组
     */
    fun uploadManager(
        key: String,
        data: String,
        mListener: OnQiniuListener
    ) {
        LoadingUtils.getInstance().show(AppManager.getAppManager().currentActivity())
        this.mListener = mListener
        keyFile = key + System.currentTimeMillis() + SpUtils.getString(
            R.string.user_id,
            ""
        )
        val last = data.lastIndexOf(".")
        if (last > 0) {
            if (data.substring(last) == ".album") {
                keyFile += ".png"
            } else {
                keyFile += data.substring(last)
            }
        }
        dataFile = data
        if (qiniu_token.isEmpty()) {
            loadQiniuToken()
            return
        } else {
            startFileUp()
        }
    }

    //文件七牛标记
    private var keyFile = ""

    //文件本地标记
    private var dataFile = ""
    private var mListener: OnQiniuListener? = null

    /**
     * 文件上传操作
     */
    private fun startFileUp() {
        mUploadManager.put(
            dataFile, keyFile, qiniu_token,
            { key, info, response ->
//                LoadingUtils.getInstance().dismiss()
                mListener?.onResult("https://cdn.tinytiger.cn/$key", info.isOK)
                if (!info.isOK) {
                    qiniu_token = ""
                    Logger.d(info.error)
                }
            }, null
        )
    }


}