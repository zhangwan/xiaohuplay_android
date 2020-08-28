package com.tinytiger.common.http
import io.reactivex.disposables.Disposable
import okhttp3.RequestBody

/**
 * Created by zwy on 2020/6/5.
 */
object HttpRequestUtil {
    val api = RetrofitRenewManager.retrofitInstance.api
    fun <T> onGet(url: String, params: Map<String, String?>, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doGet(url, params), callback)
    }

    fun <T> onGet(url: String, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doGet(url), callback)
    }

    fun <T> onPost(url: String, params: Map<String, String?>, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doBody(url, params), callback)
    }

    fun <T> onPost(url: String, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doPost(url), callback)
    }

    fun <T> onPostQuery(url: String, params: Map<String, String?>, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doPost(url, params), callback)
    }

    fun <T> onBodyQuery(url: String, query: Map<String, String?>, body: Any?, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doQueryBody(url, query, body), callback)
    }

    fun <T> onBody(url: String, params: Any?, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doBody(url, params), callback)
    }

    fun <T> onBody(url: String, headers: Map<String, String?>, params: Any?, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.doBody(url, headers, params), callback)
    }

    fun <T> uploadFile(url: String, params: Map<String, RequestBody>, callback: HttpCallback<T>): Disposable {
        return HttpUtils.http(api.uploadFile(url, params), callback)
    }

}