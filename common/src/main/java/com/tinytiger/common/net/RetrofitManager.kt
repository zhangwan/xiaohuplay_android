package com.tinytiger.common.net

import android.annotation.SuppressLint
import com.tinytiger.common.NetConstants_B
import com.tinytiger.common.R
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.net.api.ApiGameService
import com.tinytiger.common.net.api.ApiMomentService
import com.tinytiger.common.net.api.ApiOtherService
import com.tinytiger.common.net.api.ApiUserService
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.picbrowser.glide.SSLSocketClient

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 上午 11:09
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 网络请求
 */
object RetrofitManager {

    val service: ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiService::class.java)
    }

    //游戏表接口
    val serviceGame: ApiGameService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiGameService::class.java)
    }
    //圈子表接口
    val serviceMoment: ApiMomentService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiMomentService::class.java)
    }
    //用户表接口
    val serviceUser: ApiUserService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiUserService::class.java)
    }
    //其他表接口
    val serviceOther: ApiOtherService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(ApiOtherService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetConstants_B.getHttp())
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
                .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }

    }

    /**
     * 设置请求头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("token", SpUtils.getString(R.string.token, ""))
                .header("imei", SpUtils.getString(R.string.android_id, ""))
                .header("channel",SpUtils.getString(R.string.channel, "1"))
                .header("v", SpUtils.getString(R.string.version, ""))
                .method(originalRequest.method(), originalRequest.body())
            chain.proceed(requestBuilder.build())
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val response = chain.proceed(request)
            if (MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("nyn")
                    .build()
            }
            response
        }
    }


    private fun getOkHttpClient(): OkHttpClient {
        //添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        var time=6L
        if (NetConstants_B.LOG_TYPE) {
            time=160L
        }


       val builder= OkHttpClient.Builder()
            //    .addInterceptor(addQueryParameterInterceptor())  //公共参数添加
            .addInterceptor(addHeaderInterceptor()) // token过滤
            //     .addInterceptor(addCacheInterceptor())
          //  .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
            .cache(Cache(File(BaseApp._instance.cacheDir, "cache"), 1024 * 1024 * 50))//设置 请求的缓存的大小跟位置
           // .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), TrustAllCerts())
           .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.geX509tTrustManager())
            .connectTimeout(time, TimeUnit.SECONDS)
            .readTimeout(time, TimeUnit.SECONDS)
            .writeTimeout(time, TimeUnit.SECONDS)

        if (NetConstants_B.LOG_TYPE) {
            builder.addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
        }
        return builder.build()
    }

    class TrustAllCerts : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }
}

