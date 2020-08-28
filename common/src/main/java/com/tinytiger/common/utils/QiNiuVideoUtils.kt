package com.tinytiger.common.utils

import com.orhanobut.logger.Logger
import com.qiniu.android.storage.*
import com.qiniu.android.storage.persistent.FileRecorder
import com.qiniu.android.utils.UrlSafeBase64
import com.tinytiger.common.R
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.LoadingUtils
import io.reactivex.disposables.CompositeDisposable
import java.io.*


/**
 * @author zhw_luke
 * @date 2018/8/29 0029 下午 4:25
 * @Copyright 小虎互联科技
 * @doc 七牛图片上传工具类
 * @since 3.1.0
 */
class QiNiuVideoUtils private constructor() {

    private val dirPath = FileUtils.getSystemImagePath()

    interface OnQiniuListener {
        fun onResult(result: String, type: Boolean)

        fun onProgress(key: String, percent: Double)
    }

    companion object {
        var appQiniu: QiNiuVideoUtils? = null
            get() {
                if (field == null) {
                    synchronized(QiNiuVideoUtils::class.java) {
                        if (field == null) {
                            field = QiNiuVideoUtils()
                        }
                    }
                }
                return field
            }
            private set
    }

    private var mUploadManager: UploadManager

    init {
//        val zone: Zone = FixedZone(arrayOf("up-z1.qiniup.com"))
        val configuration =
            Configuration.Builder().chunkSize(256 * 1024)
                .putThreshhold(512 * 1024)
                .connectTimeout(30)
//                .zone(zone)
                .useHttps(false)
                .recorder(fileRecord(), keyGenerator())
                .responseTimeout(30).build()
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        mUploadManager = UploadManager(configuration)
    }

    /**
     * @param dirPath 断点记录文件保存的文件夹位置
     */
    fun fileRecord(): Recorder? {
        //断点记录文件保存的文件夹位置
        var recorder: Recorder? = null
        try {
            recorder = FileRecorder(FileUtils.getSystemImagePath())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return recorder
    }

    fun keyGenerator(): KeyGenerator? {
        //默认使用key的url_safe_base64编码字符串作为断点记录文件的文件名
        //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
        val keyGen = KeyGenerator { key, file ->
            // 不必使用url_safe_base64转换，uploadManager内部会处理
            // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
            val path = key.toString() + "_._" + StringBuffer(file.absolutePath).reverse()
            val f = File(dirPath, UrlSafeBase64.encodeToString(path))
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(FileReader(f))
                var tempString: String? = null
                var line = 1
                try {
                    tempString = reader.readLine()
                    while (tempString != null) {
                        line++
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        reader.close()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            path
        }
        return keyGen
    }

    private var qiniu_token = ""
    private var compositeDisposable = CompositeDisposable()
    fun loadQiniuToken() {
        //上传类型 1=图片(默认) 2=视频
        val disposable = RetrofitManager.service.getQiniuToken(2)
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
        mListener: OnQiniuListener,
        breakUpload: Boolean = false
    ) {
//        LoadingUtils.getInstance().show(AppManager.getAppManager().currentActivity())
        this.breakUpload = breakUpload
        this.mListener = mListener

        keyFile = key + System.currentTimeMillis() + SpUtils.getString(
            R.string.user_id,
            ""
        )
        val last = data.lastIndexOf(".")
        if (last > 0) {
            keyFile += data.substring(last)
        }
        dataFile = data
        if (qiniu_token.isEmpty()) {
            loadQiniuToken()
            return
        } else {
            startFileUp()
        }
    }

    //是否在断点续传
    private var breakUpload = false

    //是否暂停上传
    private var isCancel = false

    //保存临时的Key,用于取同一个断点续传的文件名
    private var tempKeyFile = ""
    private var keyFile = ""
    private var dataFile = ""
    private var mListener: OnQiniuListener? = null

    /**
     * 是否在断点续传
     */
    fun setBreakUpload(upload: Boolean) {
        this.breakUpload = upload
    }

    /**
     * 是否暂停上传
     */
    fun setCancel(cancel: Boolean) {
        this.isCancel = cancel
    }

    fun getCancel(): Boolean {
        return this.isCancel
    }

    fun startFileUp() {
        if (breakUpload && tempKeyFile.isNotEmpty()) keyFile = tempKeyFile
        mUploadManager.put(
            dataFile, keyFile, qiniu_token,
            { key, info, response ->
//                LoadingUtils.getInstance().dismiss()
                mListener?.onResult("https://tinytiger-ugc-video.tinytiger.cn/$key", info.isOK)

                breakUpload = !info.isOK
                if (!info.isOK) {
                    Logger.d(info.error)
                    tempKeyFile = keyFile
                } else {
                    tempKeyFile = ""
                }
            }, UploadOptions(null, null, false, UpProgressHandler { key, percent ->
                mListener?.onProgress(key, percent)
                Logger.d(percent)
            }, UpCancellationSignal { isCancel })
        )
    }

}