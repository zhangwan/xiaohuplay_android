package com.tinytiger.common.http

import android.annotation.SuppressLint
import android.util.Log
import com.alibaba.fastjson.JSON
import com.tinytiger.common.NetConstants_B
import com.tinytiger.common.R
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.http.downloadFile.JsDownloadInterceptor
import com.tinytiger.common.http.downloadFile.JsDownloadListener
import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.utils.FileUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.InputStream

/**
 * Created by zwy on 2020/6/5.
 */
object HttpUtils {
    val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
    val READ_TIMEOUT = "READ_TIMEOUT"
    val WRITE_TIMEOUT = "WRITE_TIMEOUT"
    fun <T> handleFailure(errorCode: String, errorMsg: String, listener: HttpCallback<T>) {
        when (errorCode) {
            HttpConstants.Code.CUSTOMIZE_CODE_1000 -> {//停服通知
                EventBus.getDefault().post(ClassEvent("StopActivity", 1000, errorMsg))
            }
            HttpConstants.Code.CUSTOMIZE_CODE_3000 -> {//登录失效
                SpUtils.saveSP(R.string.token, "")
                EventBus.getDefault().post(ClassEvent("LoginActivity", 1))
            }
            HttpConstants.Code.CUSTOMIZE_CODE_3008 -> {//用戶登陸禁止
                ToastUtils.ToastLongCenter(BaseApp.getContext(), errorMsg)
                SpUtils.saveSP(R.string.token, "")
                EventBus.getDefault().post(ClassEvent("LoginActivity", 1))
            }
            else -> {
                listener.onFailure(errorCode, errorMsg ?: "")
            }
        }

    }

    fun <T> handleResponse(any: T, listener: HttpCallback<T>) {
        when (any) {
            is BaseResp<*> -> {
                if (any.code == HttpConstants.Code.SUCCESS)
                    listener.onResponse(any)
                else
                    handleFailure(any.code ?: "", any.msg ?: "", listener)
            }
            else -> listener.onResponse(any)
        }
    }

    @SuppressLint("CheckResult")
    fun <T> http(observable: Observable<ResponseBody>, callback: HttpCallback<T>): Disposable {
        var isResult = false
        callback.onStart()
        return observable
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map {
                var content = it.string()
                return@map JSON.parseObject(content, callback.getEntry()) as T
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                isResult = true
                handleResponse(result, callback)
            }, { error ->
                error.printStackTrace()
                if (isResult) handleFailure(HttpConstants.Code.CODE_700, error.message.toString(), callback)
                else handleFailure(HttpConstants.Code.TIME_OUT,"网络不可用", callback)
            })
    }

    @SuppressLint("CheckResult")
    fun downLoadFile(url: String, file: File, listener: JsDownloadListener): Disposable {
        val mInterceptor = JsDownloadInterceptor(listener)
        return RetrofitBuilder()
            .initTimeOut(HttpConstants.TIME_OUT)
            .initBaseUrl(NetConstants_B.getHttp())
            .addInterceptor(mInterceptor).build(Api::class.java)
            .download(url)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map(fun(responseBody: ResponseBody): InputStream {
                return responseBody.byteStream()
            })
            .observeOn(Schedulers.computation()) // 用于计算任务
            .doOnNext {
                FileUtils.writeFile(it, file)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, { error ->
                Log.e(
                    "error",
                    "error==" + error.cause?.printStackTrace() + "message==" + error.message + " error==" + error
                )
                listener.onFail(error.message)
            }, {
                listener.onFinishDownload()
            }
            )
    }
}